package gzkj.easygroupmeal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.MapAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.bean.MapData;
import gzkj.easygroupmeal.utli.AndroidBug5497Workaround;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.StatusBarUtils;
import gzkj.easygroupmeal.utli.TextChange;

public class MapActivity extends AppCompatActivity implements AMapLocationListener, PoiSearch.OnPoiSearchListener {

    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.back)
    ImageView back;
    AMap aMap;
    @BindView(R.id.search_map_ed)
    EditText searchMapEd;
    @BindView(R.id.map_recycler)
    LRecyclerView mapRecycler;
    private MapAdapter mapAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private String cityCode;
    private AMapLocationClientOption option;
    private ArrayList<MapData> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content));

        StatusBarUtils.with(this)
                .init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        option = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //关闭缓存机制
        option.setLocationCacheEnable(false);
        //获取一次定位结果：
        //该方法默认为false。
        option.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        option.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(option);
        //启动定位
        mLocationClient.startLocation();

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        setData();
        setListener();
    }

    private void setData() {
        mapAdapter = new MapAdapter(MyApplication.getContextObject(), R.layout.map_item, "map");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mapAdapter);
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_1)
                .setColorResource(R.color.line_color)
                .build();
        mapRecycler.addItemDecoration(divider);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        mapRecycler.setLayoutManager(manager);
        mapRecycler.setAdapter(mLRecyclerViewAdapter);
    }

    private void setListener() {
        mapRecycler.setLoadMoreEnabled(false);
        mapRecycler.setPullRefreshEnabled(false);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().post(new MapData(data.get(position).getLatitude(), data.get(position).getLongitude(), data.get(position).getTitle(),data.get(position).getAddress(),data.get(position).getProvince(),data.get(position).getCity(),data.get(position).getArea() ,data.get(position).getSnippet()));
                finish();
            }
        });
        TextChange textChange = new OnTextChange();
        searchMapEd.addTextChangedListener(textChange);
    }

    class OnTextChange extends TextChange {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            searchAdress(searchMapEd.getText().toString().trim(), null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务
        mMapView.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        //rCode 为1000 时成功,其他为失败
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            // 解析result   获取搜索poi的结果
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(query)) {  // 是否是同一条

                    //自己创建的数据集合
                    data = new ArrayList<MapData>();
                    // 取得第一页的poiitem数据，页数从数字0开始
                    //poiResult.getPois()可以获取到PoiItem列表
                    List<PoiItem> poiItems = result.getPois();

                    //解析获取到的PoiItem列表
                    for (PoiItem item : poiItems) {
                        //获取经纬度对象
                        LatLonPoint llp = item.getLatLonPoint();
                        double lon = llp.getLongitude();
                        double lat = llp.getLatitude();
                        //返回POI的名称
                        String title = item.getTitle();
                        //返回POI的地址
                        String text = item.getSnippet();

                        data.add(new MapData(String.valueOf(lat), String.valueOf(lon), title, text,item.getProvinceName(),item.getCityName(),item.getAdName(),text));
                    }
                    mapAdapter.setDataList(data);
                }
            } else {
                Toast.makeText(MapActivity.this, "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("错误码", rCode + "");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.e("location经纬度", aMapLocation.getLongitude() + "维度" + aMapLocation.getLatitude());
        cityCode = aMapLocation.getCityCode();
        searchAdress("", aMapLocation);
    }

    public void searchAdress(String keyWord, AMapLocation aMapLocation) {
        query = new PoiSearch.Query(keyWord, "", cityCode);
//keyWord表示搜索字符串，
//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        poiSearch = new PoiSearch(MapActivity.this, query);
        poiSearch.setOnPoiSearchListener(MapActivity.this);
        if (aMapLocation != null) {
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(aMapLocation.getLatitude(),
                    aMapLocation.getLongitude()), 1000));//设置周边搜索的中心点以及半径
        }
        poiSearch.searchPOIAsyn();
    }
}
