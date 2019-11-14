package gzkj.easygroupmeal.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.datepicker.CustomDatePicker;
import gzkj.easygroupmeal.datepicker.DateFormatUtils;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DateUtil;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.SingleClick;

public class LeaveActivity extends BaseActivity {

    @BindView(R.id.leave_name_ed)
    EditText leaveNameEd;
    @BindView(R.id.leave_replacename_ed)
    EditText leaveReplacenameEd;
    @BindView(R.id.leave_replacename_spinner)
    ImageView leaveReplacenameSpinner;
    @BindView(R.id.leave_starttime_ed)
    TextView leaveStarttimeEd;
    @BindView(R.id.leave_starttime_spinner)
    ImageView leaveStarttimeSpinner;
    @BindView(R.id.leave_endtime_ed)
    TextView leaveEndtimeEd;
    @BindView(R.id.leave_endtime_spinner)
    ImageView leaveEndtimeSpinner;
    @BindView(R.id.leave_reason)
    EditText leaveReason;

    private long startTimeNum;
    private long endTimeNum;
    private CustomDatePicker mTimerPickerStart, mTimerPickerEnd;
    private String sid;
    private CommitParam commitParam;
    private View leaveView;
    private DialogCircle dialogCircle;

    @Override
    public int intiLayout() {
        return R.layout.activity_leave;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        initTimerPicker();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        startTimeNum=DateUtil.date2TimeStamp(leaveStarttimeEd.getText().toString().trim(),"yyyy-MM-dd HH:mm");
        endTimeNum=DateUtil.date2TimeStamp(leaveEndtimeEd.getText().toString().trim(),"yyyy-MM-dd HH:mm");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("提交请假申请".equals(flag)) {
            Login login=new Gson().fromJson(object,Login.class);
            toastShort(login.getResultDesc());
        }
    }
    @SingleClick
    @OnClick({R.id.back, R.id.sure, R.id.leave_replacename_ed, R.id.leave_replacename_spinner, R.id.leave_starttime_ed, R.id.leave_starttime_spinner, R.id.leave_endtime_ed, R.id.leave_endtime_spinner})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                if (startTimeNum>=endTimeNum) {
                    toastShort("结束日期选择错误");
                    return;
                }
                if (leaveReason.length()==0) {
                    toastShort("请填写请假原因");
                    return;
                }

                //请假
                leaveView = View.inflate(this, R.layout.sign_out_pop, null);
                TextView title = leaveView.findViewById(R.id.title);
                TextView sureTv = leaveView.findViewById(R.id.sure_tv);
                TextView cancelTv = leaveView.findViewById(R.id.cancel_tv);
                title.setText("确定请假？");
                dialogCircle = MyApplication.getInstance().getPop(this, leaveView, 1.3f, 4, Gravity.CENTER, 0, true);
                sureTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogCircle != null) {
                            dialogCircle.dismiss();
                            dialogCircle = null;
                            commitParam = new CommitParam();
                            commitParam.setStartTime(leaveStarttimeEd.getText().toString());
                            commitParam.setEndTime(leaveEndtimeEd.getText().toString());
                            commitParam.setCause(leaveReason.getText().toString());
                            body = commitParam.getBody(sid, HttpUrl.LEAVEINTERFACE, commitParam);
                            presenter=new Presenter(LeaveActivity.this);
                            presenter.getData("POST", "提交请假申请", HttpUrl.LEAVE_URL);
                        }
                    }
                });
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogCircle != null) {
                            dialogCircle.dismiss();
                            dialogCircle = null;
                        }
                    }
                });

                break;
            case R.id.leave_replacename_ed:
                break;
            case R.id.leave_replacename_spinner:
                break;
            case R.id.leave_starttime_ed:
            case R.id.leave_starttime_spinner:
                mTimerPickerStart.show(leaveStarttimeEd.getText().toString());
                break;
            case R.id.leave_endtime_ed:
            case R.id.leave_endtime_spinner:
                mTimerPickerEnd.show(leaveEndtimeEd.getText().toString());
                break;
        }
    }
    private void initTimerPicker() {
        String beginTime = "2019-01-01 18:00";
        String nowTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = "2022-12-31 18:00";

        leaveStarttimeEd.setText(nowTime);
        leaveEndtimeEd.setText(nowTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm

        mTimerPickerStart=new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                startTimeNum = DateUtil.date2TimeStamp(time,"yyyy-MM-dd HH:mm");
                leaveStarttimeEd.setText(time);
            }
        },beginTime,endTime);

        mTimerPickerEnd=new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                endTimeNum = DateUtil.date2TimeStamp(time,"yyyy-MM-dd HH:mm");
                leaveEndtimeEd.setText(time);
            }
        },beginTime,endTime);

        // 允许点击屏幕或物理返回键关闭
        mTimerPickerStart.setCancelable(true);
        mTimerPickerEnd.setCancelable(true);
        // 允许循环滚动
        mTimerPickerStart.setIsLoop(true);
        mTimerPickerEnd.setIsLoop(true);
        // 允许滚动动画
        mTimerPickerStart.setCanShowAnim(true);
        mTimerPickerEnd.setCanShowAnim(true);
    }
}
