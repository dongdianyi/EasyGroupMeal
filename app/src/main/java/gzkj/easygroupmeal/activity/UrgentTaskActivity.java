package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.SpinnerAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Cycle;
import gzkj.easygroupmeal.bean.ShowTask;
import gzkj.easygroupmeal.datepicker.CustomDatePicker;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DateUtil;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;

public class UrgentTaskActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.select_staff_spinner)
    AppCompatSpinner selectStaffSpinner;
    @BindView(R.id.remind_spinner)
    AppCompatSpinner remindSpinner;
    @BindView(R.id.select_time_ed)
    EditText selectTimeEd;
    @BindView(R.id.select_endtime_ed)
    EditText selectEndtimeEd;
    @BindView(R.id.task_content_ed)
    EditText taskContentEd;
    @BindView(R.id.task_name_ed)
    EditText taskNameEd;
    @BindView(R.id.cycle_ed)
    EditText cycleEd;
    private CommitParam commitParam;
    private String sid;
    private List<Cycle> mDataStaff;
    private CustomDatePicker mTimerPickerStart, mTimerPickerEnd;
    private String receptId,taskCycle,taskRemind="";
    private List<Cycle> mData;
    private long endTimeNum;
    private long startTimeNum;

    @Override
    public int intiLayout() {
        return R.layout.activity_urgent_task;
    }

    @Override
    public void initData() {
        hideTopUIMenu();

        initTimerPicker();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();

        startTimeNum=DateUtil.date2TimeStamp("2019-01-01 "+selectTimeEd.getText().toString().trim(),"yyyy-MM-dd HH:mm");
        endTimeNum=DateUtil.date2TimeStamp("2019-01-01 "+selectEndtimeEd.getText().toString().trim(),"yyyy-MM-dd HH:mm");

        mData = new ArrayList<>();
        mData.add(new Cycle("开始时提醒",""));
        mData.add(new Cycle("5分钟前","5"));
        mData.add(new Cycle("20分钟前","20"));
        mData.add(new Cycle("1小时前","60"));
        mData.add(new Cycle("1天前","1440"));
        mData.add(new Cycle("2天前","2880"));
        remindSpinner.setAdapter(new SpinnerAdapter(this, mData));


        mDataStaff=new ArrayList<>();
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.SHOWTASKINTERFACE, commitParam);
        presenter=new Presenter(this);
        presenter.getData("POST", "派发任务员工及一级任务展示", HttpUrl.SHOWTASK_URL);
    }

    @Override
    public void setListener() {
        selectStaffSpinner.setOnItemSelectedListener(this);
        remindSpinner.setOnItemSelectedListener(this);
    }


    @Override
    public void toActivityData(String flag, String object) {
        ShowTask showTask = new Gson().fromJson(object, ShowTask.class);
        if ("派发任务员工及一级任务展示".equals(flag)) {
            for (ShowTask.ResultObjBean.StaffListBean staffListBean : showTask.getResultObj().getStaffList()) {
                mDataStaff.add(new Cycle(staffListBean.getUserName(), staffListBean.getUserId()));
            }
            if (mDataStaff.size()>0) {
                receptId = mDataStaff.get(0).getCycleNum();
            }
            selectStaffSpinner.setAdapter(new SpinnerAdapter(this, mDataStaff));

        }
        if ("紧急任务".equals(flag)) {
            toastShort(showTask.getResultDesc());
            finish();
        }
    }

    @OnClick({R.id.back, R.id.sure, R.id.select_time_iv, R.id.select_time_ed, R.id.end_time_iv, R.id.select_endtime_ed, R.id.cycle_iv, R.id.cycle_ed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                if (receptId==null) {
                    toastShort("请选择员工");
                    return;
                }
                if (startTimeNum>=endTimeNum) {
                    toastShort("结束时间选择错误");
                    return;
                }
                if (taskContentEd.length()==0) {
                    toastShort("请填写任务");
                    return;
                }
                if (taskCycle==null) {
                    toastShort("请选择周期");
                    return;
                }
                commitParam = new CommitParam();
                commitParam.setReceptId(receptId);
                commitParam.setIsWarn("1");
                commitParam.setTaskCycle(taskCycle);
                commitParam.setTaskRemind(taskRemind);
                commitParam.setStartTime(selectTimeEd.getText().toString().trim());
                commitParam.setEndTime(selectEndtimeEd.getText().toString().trim());
                commitParam.setTaskName(taskNameEd.getText().toString().trim());
                commitParam.setTaskContent(taskContentEd.getText().toString().trim());
                body = commitParam.getBody(sid, HttpUrl.URGENTTASKINTERFACE, commitParam);
                presenter=new Presenter(this);
                presenter.getData("POST", "紧急任务", HttpUrl.URGENTTASK_URL);
                break;
            case R.id.select_time_iv:
            case R.id.select_time_ed:
                mTimerPickerStart.show("2019-01-01 "+selectTimeEd.getText().toString().trim());
                break;
            case R.id.end_time_iv:
            case R.id.select_endtime_ed:
                mTimerPickerEnd.show("2019-01-01 "+selectEndtimeEd.getText().toString().trim());
                break;
            case R.id.cycle_iv:
            case R.id.cycle_ed:
                Intent intent = new Intent(this, AttendanceDateActivity.class);
                intent.putExtra("title", "选择周期");
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().removeStickyEvent(Cycle.class);
            EventBus.getDefault().unregister(this);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void Event(Cycle cycle) {
        cycleEd.setText(cycle.getCycleName());
        taskCycle=cycle.getCycleNum();
    }

    private void initTimerPicker() {
        String beginTime = "2019-01-01 00:00";
        String endTime = "2022-12-31 23:59";

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm

        mTimerPickerStart=new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                startTimeNum = DateUtil.date2TimeStamp(time,"yyyy-MM-dd HH:mm");
                selectTimeEd.setText(time.substring(11));
            }
        },beginTime,endTime);

        mTimerPickerEnd=new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                endTimeNum = DateUtil.date2TimeStamp(time,"yyyy-MM-dd HH:mm");
                selectEndtimeEd.setText(time.substring(11));
            }
        },beginTime,endTime);

        // 允许点击屏幕或物理返回键关闭
        mTimerPickerStart.setCancelable(true);
        mTimerPickerEnd.setCancelable(true);
        // 只显示时和分
        mTimerPickerStart.showSpecificTime2(true);
        mTimerPickerEnd.showSpecificTime2(true);
        // 允许循环滚动
        mTimerPickerStart.setIsLoop(true);
        mTimerPickerEnd.setIsLoop(true);
        // 允许滚动动画
        mTimerPickerStart.setCanShowAnim(true);
        mTimerPickerEnd.setCanShowAnim(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.remind_spinner:
                taskRemind = mData.get(position).getCycleNum();
                break;
            case R.id.select_staff_spinner:
                receptId = mDataStaff.get(position).getCycleNum();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
