package gzkj.easygroupmeal.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.ScheduleAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.datepicker.CustomDatePicker;
import gzkj.easygroupmeal.datepicker.DateFormatUtils;
import gzkj.easygroupmeal.utli.DateUtil;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;

public class AddScheduleActivity extends BaseActivity {

    @BindView(R.id.title_ed)
    EditText titleEd;
    @BindView(R.id.address_ed)
    EditText addressEd;
    @BindView(R.id.togglebutton)
    ToggleButton togglebutton;
    @BindView(R.id.start_date_tv)
    TextView startDateTv;
    @BindView(R.id.end_date_tv)
    TextView endDateTv;
    @BindView(R.id.remind_tv)
    TextView remindTv;
    @BindView(R.id.remarks_ed)
    EditText remarksEd;

    private CustomDatePicker mTimerPickerStart, mTimerPickerEnd;
    private View remindView;
    private DialogCircle dialogCircle;
    private LRecyclerView remindRecycler;
    private ScheduleAdapter mScheduleAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<String> mData;
    private long startTimeNum;
    private long endTimeNum;

    @Override
    public int intiLayout() {
        return R.layout.activity_add_schedule;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        initTimerPicker();
        remindView = View.inflate(this, R.layout.remind_pop, null);
        remindRecycler = remindView.findViewById(R.id.remind_recycler);

        startTimeNum=DateUtil.date2TimeStamp(startDateTv.getText().toString().trim(),"yyyy-MM-dd HH:mm");
        endTimeNum=DateUtil.date2TimeStamp(endDateTv.getText().toString().trim(),"yyyy-MM-dd HH:mm");

        mData = new ArrayList<>();
        mData.add("无");
        mData.add("5分钟前");
        mData.add("20分钟前");
        mData.add("1小时前");
        mData.add("1天前");
        mData.add("2天前");
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_1)
                .setColorResource(R.color.line_color)
                .build();
        remindRecycler.addItemDecoration(divider);
        mScheduleAdapter = new ScheduleAdapter(MyApplication.getContextObject(), R.layout.tv_item, "remind");
        mScheduleAdapter.setDataList(mData);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mScheduleAdapter);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        remindRecycler.setLayoutManager(manager);
        remindRecycler.setAdapter(mLRecyclerViewAdapter);
        remindRecycler.setLoadMoreEnabled(false);
        remindRecycler.setPullRefreshEnabled(false);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                remindTv.setText(mData.get(position));
                if (dialogCircle!=null) {
                    dialogCircle.dismiss();
                }
            }
        });
    }

    private void initTimerPicker() {
        String beginTime = "2019-01-01 18:00";
        String nowTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = "2022-12-31 18:00";

        startDateTv.setText(nowTime);
        endDateTv.setText(nowTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm

        mTimerPickerStart=new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                startTimeNum = DateUtil.date2TimeStamp(time,"yyyy-MM-dd HH:mm");
                startDateTv.setText(time);
            }
        },beginTime,endTime);

        mTimerPickerEnd=new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                endTimeNum = DateUtil.date2TimeStamp(time,"yyyy-MM-dd HH:mm");
                endDateTv.setText(time);
            }
        },beginTime,endTime);

        // 允许点击屏幕或物理返回键关闭
        mTimerPickerStart.setCancelable(true);
        mTimerPickerEnd.setCancelable(true);
        // 显示时和分
        mTimerPickerStart.showSpecificTime(true);
        mTimerPickerEnd.showSpecificTime(true);
        // 允许循环滚动
        mTimerPickerStart.setIsLoop(true);
        mTimerPickerEnd.setIsLoop(true);
        // 允许滚动动画
        mTimerPickerStart.setCanShowAnim(true);
        mTimerPickerEnd.setCanShowAnim(true);
    }

    @Override
    public void setListener() {
        togglebutton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                toastShort(on + "");
            }
        });
    }

    @OnClick({R.id.back, R.id.add, R.id.start_linear, R.id.end_linear, R.id.remind_linear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                if (startTimeNum>=endTimeNum) {
                    toastShort("结束日期选择错误");
                    return;
                }
                break;
            case R.id.start_linear:
                mTimerPickerStart.show(startDateTv.getText().toString());
                break;
            case R.id.end_linear:
                mTimerPickerEnd.show(endDateTv.getText().toString());
                break;
            case R.id.remind_linear:
                if (dialogCircle==null) {
                    dialogCircle = MyApplication.getInstance().getPop(this, remindView, 1, 3, Gravity.BOTTOM, R.style.BottomDialog_Animation, true);
                }else {
                    dialogCircle.show();
                }
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {

    }

}
