package gzkj.easygroupmeal.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.meetsl.scardview.SCardView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.AdministratorsAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.bean.MapData;
import gzkj.easygroupmeal.bean.MemberJson;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.httpUtil.RetrofitBean;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.MD5Util;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.SingleClick;
import gzkj.easygroupmeal.utli.TextChange;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTeamActivity extends BaseActivity {


    @BindView(R.id.enterprise_name_ed)
    EditText enterpriseNameEd;
    @BindView(R.id.enterprise_legal_person_ed)
    EditText enterpriseLegalPersonEd;
    @BindView(R.id.creditcode_ed)
    EditText creditcodeEd;
    @BindView(R.id.enterprise_address_ed)
    EditText enterpriseAddressEd;
    @BindView(R.id.num_spinner)
    AppCompatSpinner numSpinner;
    @BindView(R.id.administrators_cardView)
    SCardView administratorsCardView;
    @BindView(R.id.membership_cardView)
    SCardView membershipCardView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.administrators_list_tv)
    TextView administratorsListTv;
    @BindView(R.id.membership_list_tv)
    TextView membershipListTv;
    @BindView(R.id.administrators_tv)
    TextView administratorsTv;
    @BindView(R.id.create_team)
    TextView createTeam;
    @BindView(R.id.team_cardview)
    SCardView teamCardview;
    @BindView(R.id.company_cardview)
    SCardView companyCardview;
    @BindView(R.id.membership_list_recycler)
    LRecyclerView membershipListRecycler;
    @BindView(R.id.administrators_list_recycler)
    LRecyclerView administratorsListRecycler;
    @BindView(R.id.restaurant_name_ed)
    EditText restaurantNameEd;
    @BindView(R.id.restaurant_school_ed)
    EditText restaurantSchoolEd;
    @BindView(R.id.restaurant_nickname_ed)
    EditText restaurantNicknameEd;
    @BindView(R.id.restaurant_address_ed)
    EditText restaurantAddressEd;
    @BindView(R.id.company_ed)
    EditText companyEd;
    private AdministratorsAdapter mAdministratorsAdapter, mMembershipAdapter;
    private LRecyclerViewAdapter mAdministratorsLRecyclerViewAdapter, mMembershipLRecyclerViewAdapter;
    private List<MemberJson> mDataMembership;
    private List<MemberJson> mDataAdministrators;
    private List<MemberJson> mDataAdministratorsMemberJson;

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,

    };
    private String postType;
    private CommitParam commitParam;
    private String address;
    private String province;
    private String city;
    private String area;
    private String latitude;
    private String longitude;
    private String sid;

    @Override
    public int intiLayout() {
        return R.layout.activity_create_team;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        createTeam.setEnabled(false);
        commitParam = new CommitParam();
        mShared = new SharedPreferencesHelper(this, "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        EventBus.getDefault().register(this);
        postType = getIntent().getStringExtra("posttype");
        if (HttpUrl.POSITION[0].equals(postType)) {
            teamCardview.setVisibility(View.GONE);
            companyCardview.setVisibility(View.VISIBLE);
            administratorsTv.setText(getString(R.string.administrators));
            administratorsCardView.setVisibility(View.GONE);
            membershipCardView.setVisibility(View.GONE);
        }
        if (HttpUrl.POSITION[1].equals(postType)) {
            teamCardview.setVisibility(View.VISIBLE);
            companyCardview.setVisibility(View.GONE);
            title.setText(getString(R.string.create_team));
            createTeam.setText(getString(R.string.create_team));
            administratorsTv.setText(getString(R.string.person_in_charge));
        }
        mDataAdministrators = new ArrayList<>();
        mDataMembership = new ArrayList<>();
        mDataAdministrators.add(new MemberJson("add", "", "新增", ""));
        mDataMembership.add(new MemberJson("add", "", "新增", ""));
        mAdministratorsAdapter = new AdministratorsAdapter(MyApplication.getContextObject(), R.layout.member_item, "member");
        mAdministratorsAdapter.setDataList(mDataAdministrators);
        mAdministratorsLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdministratorsAdapter);
        MyGridLayoutManager managerAdministrators = new MyGridLayoutManager(MyApplication.getContextObject(), 4, LinearLayoutManager.VERTICAL, false);
        managerAdministrators.setScrollEnabled(false);
        administratorsListRecycler.setLayoutManager(managerAdministrators);
        administratorsListRecycler.setAdapter(mAdministratorsLRecyclerViewAdapter);


        mMembershipAdapter = new AdministratorsAdapter(MyApplication.getContextObject(), R.layout.member_item, "member");
        mMembershipAdapter.setDataList(mDataMembership);
        mMembershipLRecyclerViewAdapter = new LRecyclerViewAdapter(mMembershipAdapter);
        MyGridLayoutManager managerMembership = new MyGridLayoutManager(MyApplication.getContextObject(), 4, LinearLayoutManager.VERTICAL, false);
        managerMembership.setScrollEnabled(false);
        membershipListRecycler.setLayoutManager(managerMembership);
        membershipListRecycler.setAdapter(mMembershipLRecyclerViewAdapter);
    }

    @Override
    public void setListener() {
        administratorsListRecycler.setLoadMoreEnabled(false);
        administratorsListRecycler.setPullRefreshEnabled(false);
        membershipListRecycler.setLoadMoreEnabled(false);
        membershipListRecycler.setPullRefreshEnabled(false);
        adapterListener();
        //监听所有的edittext
        TextChange textChange = new OnTextChange();
        enterpriseNameEd.addTextChangedListener(textChange);
        enterpriseLegalPersonEd.addTextChangedListener(textChange);
        creditcodeEd.addTextChangedListener(textChange);
        restaurantNameEd.addTextChangedListener(textChange);
        restaurantSchoolEd.addTextChangedListener(textChange);
    }

    public void adapterListener() {
        mAdministratorsLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                logger("postion" + position + "size" + mDataAdministrators.size());
                if (position == mDataAdministrators.size() - 1) {
                    Intent intent = new Intent(CreateTeamActivity.this, AddAdministratorsActivity.class);
                    intent.putExtra("sid", sid);
                    intent.putExtra("posttype", postType);
                    intent.putExtra("flag", getString(R.string.person_in_charge));
                    startActivity(intent);
                }
            }
        });
        mMembershipLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                logger("postion" + position + "size" + mDataMembership.size());
                if (position == mDataMembership.size() - 1) {
                    Intent intent = new Intent(CreateTeamActivity.this, AddAdministratorsActivity.class);
                    intent.putExtra("sid", sid);
                    intent.putExtra("posttype", postType);
                    intent.putExtra("flag", getString(R.string.add_member));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void toActivityData(String flag, String object) {
        Login login = new Gson().fromJson(object, Login.class);
        toastShort(login.getResultDesc());
        startActivity(new Intent(this, MainActivity.class));
    }

    @SingleClick
    @OnClick({R.id.restaurant_school_ed, R.id.enterprise_address_iv, R.id.restaurant_address_iv, R.id.restaurant_address_ed, R.id.enterprise_address_ed, R.id.administrators_relative, R.id.back, R.id.create_team})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.enterprise_address_ed:
            case R.id.enterprise_address_iv:
            case R.id.restaurant_address_iv:
            case R.id.restaurant_address_ed:
                getPermissions();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.restaurant_school_ed:
                startActivity(new Intent(this, SchoolActivity.class));
                break;
            case R.id.create_team:
                if (HttpUrl.POSITION[0].equals(postType)) {
                    if (enterpriseAddressEd.length() == 0) {
                        toastShort("请输入公司地址");
                        return;
                    }
                    String Timespan = getSecondTimestampTwo();
                    String Token = MD5Util.md5(HttpUrl.QCC_KEY + Timespan + HttpUrl.QCC_SECRETKEY).toUpperCase();
                    Call<ResponseBody> data = RetrofitBean.getApiQCC().getQCC(Token, Timespan, HttpUrl.QCC_KEY, creditcodeEd.getText().toString().trim(), enterpriseNameEd.getText().toString().trim()
                            , enterpriseLegalPersonEd.getText().toString(), "json");
                    data.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String result = response.body().string();
                                logger(result);
                                JSONObject jsonObject = new JSONObject(result);
                                if ("200".equals(jsonObject.getString("Status")) && "一致".equals(jsonObject.getString("Result"))) {
                                    //创建公司
                                    commitParam.setCompanyName(enterpriseNameEd.getText().toString().trim());
                                    commitParam.setBusinessCode(creditcodeEd.getText().toString().trim());
                                    commitParam.setMemberJson("");
                                    commitParam.setAddress(address);
                                    commitParam.setLatitude(latitude);
                                    commitParam.setLongitude(longitude);
                                    commitParam.setProvince(province);
                                    commitParam.setCity(city);
                                    commitParam.setArea(area);
                                    commitParam.setCompanyScore(numSpinner.getSelectedItem().toString());
                                    body = commitParam.getBody(sid, HttpUrl.CREATCOMPANYINTERFACE, commitParam);
                                    presenter = new Presenter(CreateTeamActivity.this);
                                    presenter.getData("POST", "创建公司", HttpUrl.CREATCOMPANY_URL);
                                } else {
                                    toastShort(jsonObject.getString("Result"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            toastShort(t.getMessage());
                        }
                    });
                }
                if (HttpUrl.POSITION[1].equals(postType)) {
                    if (restaurantAddressEd.length() == 0) {
                        toastShort("请输入餐厅地址");
                        return;
                    }
//                    if (mDataMembership.size() < 3) {
//                        toastShort("成员至少添加两位");
//                        return;
//                    }
                    commitParam.setTeamName(restaurantNameEd.getText().toString().trim());
                    commitParam.setSchoolZone(restaurantSchoolEd.getText().toString().trim());
                    commitParam.setOtherName(restaurantNicknameEd.getText().toString().trim());
//                    commitParam.setCompanyName(companyEd.getText().toString().trim());
                    commitParam.setLongitude(longitude);
                    commitParam.setLatitude(latitude);
                    commitParam.setProvince(province);
                    commitParam.setCity(city);
                    commitParam.setArea(area);
                    commitParam.setAddress(address);
                    mDataAdministratorsMemberJson = new ArrayList<>();
                    mDataAdministratorsMemberJson.addAll(mDataAdministrators);
                    mDataAdministratorsMemberJson.remove(mDataAdministratorsMemberJson.size() - 1);
                    mDataAdministratorsMemberJson.addAll(mDataMembership);
                    mDataAdministratorsMemberJson.remove(mDataAdministratorsMemberJson.size() - 1);
                    commitParam.setMemberJson(new Gson().toJson(mDataAdministratorsMemberJson));
                    body = commitParam.getBody(sid, HttpUrl.CREATTEAMINTERFACE, commitParam);
                    presenter = new Presenter(this);
                    presenter.getData("POST", "创建团队", HttpUrl.CREATTEAM_URL);
                }

                break;
            case R.id.administrators_relative:
//                startActivity(new Intent(this, AdministrationActivity.class));
                break;
        }
    }

    private void getPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .request(needPermissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            startActivity(new Intent(CreateTeamActivity.this, MapActivity.class));
                        } else {
                            toastShort("请先获取权限");
                        }
                    }
                });

    }

    class OnTextChange extends TextChange {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (HttpUrl.POSITION[0].equals(postType)) {
                if (enterpriseNameEd.length() > 0 && enterpriseLegalPersonEd.length() > 0 && creditcodeEd.length() > 0) {
                    createTeam.setBackgroundResource(R.drawable.fillet_blue);
                    createTeam.setEnabled(true);
                } else {
                    createTeam.setBackgroundResource(R.drawable.fillet_blueshallow);
                    createTeam.setEnabled(true);
                }
            }
            if (HttpUrl.POSITION[1].equals(postType)) {
                if (restaurantNameEd.length() > 0 && restaurantSchoolEd.length() > 0) {
                    createTeam.setBackgroundResource(R.drawable.fillet_blue);
                    createTeam.setEnabled(true);
                } else {
                    createTeam.setBackgroundResource(R.drawable.fillet_blueshallow);
                    createTeam.setEnabled(true);
                }
            }

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MapData mapData) {
        enterpriseAddressEd.setText(mapData.getTitle());
        restaurantAddressEd.setText(mapData.getTitle());
        address = mapData.getAddress();
        province = mapData.getProvince();
        city = mapData.getCity();
        area = mapData.getArea();
        latitude = mapData.getLatitude();
        longitude = mapData.getLongitude();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(String string) {
        restaurantSchoolEd.setText(string);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event2(MemberJson memberJson) {
        if (getString(R.string.add_member).equals(memberJson.getFlag())) {
            mDataMembership.remove(mDataMembership.size() - 1);
            mDataMembership.add(new MemberJson(HttpUrl.POSITION[2], memberJson.getUserName(), memberJson.getTelnum(), "", ""));
            mDataMembership.add(new MemberJson("add", "", "新增", ""));
            mMembershipAdapter.setDataList(mDataMembership);
            membershipListTv.setText("共" + (mDataMembership.size() - 1) + "人");
        }
        if (getString(R.string.person_in_charge).equals(memberJson.getFlag())) {
            mDataAdministrators.remove(mDataAdministrators.size() - 1);
            mDataAdministrators.add(new MemberJson(HttpUrl.POSITION[3], memberJson.getUserName(), memberJson.getTelnum(), memberJson.getSchoolZone(), memberJson.getJob()));
            mDataAdministrators.add(new MemberJson("add", "", "新增", ""));
            mAdministratorsAdapter.setDataList(mDataAdministrators);
            administratorsListTv.setText("共" + (mDataAdministrators.size() - 1) + "人");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 获取精确到秒的时间戳
     *
     * @param
     * @return
     */
    public static String getSecondTimestampTwo() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        if (null == date) {
            return "";
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return timestamp;
    }
}
