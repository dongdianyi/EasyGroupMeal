package gzkj.easygroupmeal.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.bean.Task;
import gzkj.easygroupmeal.datepicker.CustomDatePicker;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DateUtil;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;

public class UpdateTaskActivity extends BaseActivity {

    @BindView(R.id.select_time_ed)
    EditText selectTimeEd;
    @BindView(R.id.select_endtime_ed)
    EditText selectEndtimeEd;
    @BindView(R.id.task_content_ed)
    EditText taskContentEd;
    @BindView(R.id.delete)
    TextView deleteTv;
    private Task.ResultObjBean resultObjBean;
    private String flag;
    private CommitParam commitParam;
    private String sid;
    private long startTimeNum;
    private long endTimeNum;
    private DialogCircle dialogCircle;

    @Override
    public int intiLayout() {
        return R.layout.activity_update_task;
    }

    private CustomDatePicker mTimerPickerStart, mTimerPickerEnd;

    @Override
    public void initData() {
        hideTopUIMenu();
        initTimerPicker();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        resultObjBean = (Task.ResultObjBean) getIntent().getSerializableExtra("task");
        flag = getIntent().getStringExtra("flag");
        if ("general".equals(flag)) {
            deleteTv.setVisibility(View.GONE);
        }
        selectTimeEd.setText(resultObjBean.getStartTime());
        selectEndtimeEd.setText(resultObjBean.getEndTime());
        startTimeNum = DateUtil.date2TimeStamp("2019-01-01 "+resultObjBean.getStartTime(),"yyyy-MM-dd HH:mm");
        endTimeNum = DateUtil.date2TimeStamp("2019-01-01 "+resultObjBean.getEndTime(),"yyyy-MM-dd HH:mm");
        taskContentEd.setText(resultObjBean.getTaskContent());
        taskContentEd.setSelection(taskContentEd.length());//将光标移至文字末尾
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.delete,R.id.back, R.id.sure, R.id.select_time_iv, R.id.select_time_ed, R.id.end_time_iv, R.id.select_endtime_ed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.delete:
                updatePop("确定删除该任务吗?");
                break;
            case R.id.sure:
                if (startTimeNum>=endTimeNum) {
                    toastShort("结束时间选择错误");
                    return;
                }
                if (taskContentEd.length()==0) {
                    toastShort("内容不能为空");
                    return;
                }

                if ("tasktwo".equals(flag)) {
                    commitParam = new CommitParam();
                    commitParam.setRecordId(resultObjBean.getRecordId());
                    commitParam.setTaskName(resultObjBean.getTaskName());
                    commitParam.setTaskContent(taskContentEd.getText().toString());
                    commitParam.setStartTime(selectTimeEd.getText().toString().trim());
                    commitParam.setEndTime(selectEndtimeEd.getText().toString().trim());
                    commitParam.setIsWarn(resultObjBean.getIsWarn());
                    body = commitParam.getBody(sid, HttpUrl.UPDATETASKINTERFACE, commitParam);
                    presenter=new Presenter(this);
                    presenter.getData("POST", "任务修改", HttpUrl.UPDATETASK_URL);
                }
                if ("general".equals(flag)) {
                    EventBus.getDefault().postSticky(new Task.ResultObjBean(selectTimeEd.getText().toString().trim(),selectEndtimeEd.getText().toString().trim(),taskContentEd.getText().toString()));
                    finish();
                }
                break;
            case R.id.select_time_iv:
            case R.id.select_time_ed:
                mTimerPickerStart.show("2019-01-01 "+selectTimeEd.getText().toString().trim());
                break;
            case R.id.end_time_iv:
            case R.id.select_endtime_ed:
                mTimerPickerEnd.show("2019-01-01 "+selectEndtimeEd.getText().toString().trim());
                break;
        }
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
    public void toActivityData(String flag, String object) {
        Login login=new Gson().fromJson(object,Login.class);
        toastShort(login.getResultDesc());
        finish();
    }
    public void updatePop(String mTitle) {
        View updateStateView = View.inflate(this, R.layout.update_state_pop, null);
        TextView sureTv = updateStateView.findViewById(R.id.sure);
        TextView title = updateStateView.findViewById(R.id.title);
        TextView cancelTv = updateStateView.findViewById(R.id.cancel_tv);
        title.setText(mTitle);
       TextView[] tvUpdate = new TextView[]{sureTv, cancelTv};
        dialogCircle = MyApplication.getInstance().getPop(this, updateStateView, 1.1f, 3, Gravity.BOTTOM, R.style.BottomDialog_Animation, true);
        for (int i = 0; i < tvUpdate.length; i++) {
            final int finalI = i;
            tvUpdate[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogCircle.dismiss();
                    dialogCircle = null;
                    //删除任务
                    if (finalI == 0) {
                            commitParam = new CommitParam();
                            commitParam.setRecordId(resultObjBean.getRecordId());
                            commitParam.setReceptId(resultObjBean.getReceptId());
                            body = commitParam.getBody(sid, HttpUrl.DELETETASKINTERFACE, commitParam);
                            presenter=new Presenter(UpdateTaskActivity.this);
                            presenter.getData("POST", "任务删除", HttpUrl.DELETETASK_URL);
                    }
                }
            });
        }
    }
}
