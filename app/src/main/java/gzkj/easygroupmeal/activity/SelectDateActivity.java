package gzkj.easygroupmeal.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Date;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DateUtil;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import meal.group.easy.gzkj.calendarlibrary.Calendar;
import meal.group.easy.gzkj.calendarlibrary.CalendarView;

public class SelectDateActivity extends BaseActivity implements CalendarView.OnDateSelectedListener {

    @BindView(R.id.select_date_ed)
    EditText selectDateEd;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sure)
    TextView sure;
    @BindView(R.id.calendar)
    CalendarView calendarView;
    private Calendar calendar;
    private int year;
    private int month;
    private boolean sureFlag;
    private CommitParam commitParam;
    private String sid;
    private String teamId;
    private String date;
    private List<Calendar> schemes;
    private View dateView;
    private DialogCircle dialogCircle;
    private Map<Long, Date.ResultObjBean> map;
    private String clockId;

    @Override
    public int intiLayout() {
        return R.layout.activity_select_date;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        teamId = getIntent().getStringExtra("teamId");
        schemes = new ArrayList<>();
        year = calendarView.getCurYear();
        month = calendarView.getCurMonth();
        calendarView.setRange(year, 1, year + 3, 12);
        calendarView.scrollToCurrent();
        map = new HashMap<>();
        title.setText(year + "年" + month + "月");
        commitParam = new CommitParam();
        commitParam.setTeamId(teamId);
        body = commitParam.getBody(sid, HttpUrl.QUREYNOPICINTERFACE, commitParam);
        presenter = new Presenter(SelectDateActivity.this);
        presenter.getData("POST", "查询不打卡日期", HttpUrl.QUREYNOPTC_URL);

    }
    @SuppressWarnings("all")
    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        this.calendar = calendar;
        date=calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
        if (map.containsKey(DateUtil.date2TimeStamp(date, "yyyy-MM-dd"))) {
            Date.ResultObjBean resultObjBean=(Date.ResultObjBean)map.get(DateUtil.date2TimeStamp(date, "yyyy-MM-dd"));
            sureFlag = false;
            sure.setText(R.string.delete);
            selectDateEd.setEnabled(false);
            assert resultObjBean != null;
            selectDateEd.setText(resultObjBean.getCause());
            clockId = resultObjBean.getClockId();
        }else {
            sureFlag = true;
            sure.setText(R.string.add);
            selectDateEd.setEnabled(true);
            selectDateEd.setText("");
        }
        title.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
//        if (isClick) {
//            Toast.makeText(getActivity(), getCalendarText(calendar), Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void setListener() {
        calendarView.setOnDateSelectedListener(this);
    }

    @OnClick({R.id.back, R.id.sure, R.id.last_month, R.id.next_month})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                dateView = View.inflate(this, R.layout.sign_out_pop, null);
                TextView title = dateView.findViewById(R.id.title);
                TextView sureTv = dateView.findViewById(R.id.sure_tv);
                TextView cancelTv = dateView.findViewById(R.id.cancel_tv);
                if (sureFlag) {
                    //添加
                    title.setText("确定添加不打卡日期？");
                    dialogCircle = MyApplication.getInstance().getPop(this, dateView, 1.3f, 4, Gravity.CENTER, 0, true);
                    sureTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialogCircle != null) {
                                dialogCircle.dismiss();
                                dialogCircle = null;
                                commitParam = new CommitParam();
                                commitParam.setDate(DateUtil.timeStamp2Date(DateUtil.date2TimeStamp(date, "yyyy-MM-dd"), "yyyy-MM-dd"));
                                commitParam.setTeamId(teamId);
                                commitParam.setCause(selectDateEd.getText().toString());
                                body = commitParam.getBody(sid, HttpUrl.ADDNOPTCINTERFACE, commitParam);
                                presenter = new Presenter(SelectDateActivity.this);
                                presenter.getData("POST", "添加不打卡日期", HttpUrl.ADDNOPTC_URL);
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
                } else {
                    //删除
                    title.setText("确定删除不打卡日期？");
                    dialogCircle = MyApplication.getInstance().getPop(this, dateView, 1.3f, 4, Gravity.CENTER, 0, true);
                    sureTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialogCircle != null) {
                                dialogCircle.dismiss();
                                dialogCircle = null;
                                commitParam = new CommitParam();
                                commitParam.setClockId(clockId);
                                body = commitParam.getBody(sid, HttpUrl.DELETENOPTCINTERFACE, commitParam);
                                presenter = new Presenter(SelectDateActivity.this);
                                presenter.getData("POST", "删除不打卡日期", HttpUrl.DELETENOPTC_URL);
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
                }
                break;
            case R.id.last_month:
                calendarView.scrollToPre();
                break;
            case R.id.next_month:
                calendarView.scrollToNext();
                break;

        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("添加不打卡日期".equals(flag)) {
            Date date=new Gson().fromJson(object,Date.class);
            sureFlag = false;
            sure.setText(R.string.delete);
            for (Date.ResultObjBean resultObjBean : date.getResultObj()) {
                schemes.add(getSchemeCalendar(DateUtil.getYear(resultObjBean.getDate(),"yyyy-MM-dd"), DateUtil.getMonth(resultObjBean.getDate(),"yyyy-MM-dd"), DateUtil.getDay(resultObjBean.getDate(),"yyyy-MM-dd"), 0xFF40db25,"假"));
                map.put(DateUtil.date2TimeStamp(resultObjBean.getDate(), "yyyy-MM-dd"),resultObjBean);
                clockId=resultObjBean.getClockId();
            }
            calendarView.setSchemeDate(schemes);
            selectDateEd.setEnabled(false);
            toastShort("添加成功");
        }
        if ("删除不打卡日期".equals(flag)) {
            sureFlag = true;
            sure.setText(R.string.add);
            schemes.remove(getSchemeCalendar(calendar.getYear(), calendar.getMonth(), calendar.getDay(), 0xFF40db25, "假"));
            map.remove(DateUtil.date2TimeStamp(date, "yyyy-MM-dd"));
            calendarView.setSchemeDate(schemes);
            selectDateEd.setEnabled(true);
            selectDateEd.setText("");
            toastShort("删除成功");
        }
        if ("查询不打卡日期".equals(flag)) {
            Date date=new Gson().fromJson(object,Date.class);
            schemes.clear();
            map.clear();
            for (Date.ResultObjBean resultObjBean : date.getResultObj()) {
                schemes.add(getSchemeCalendar(DateUtil.getYear(resultObjBean.getDate(),"yyyy-MM-dd"), DateUtil.getMonth(resultObjBean.getDate(),"yyyy-MM-dd"), DateUtil.getDay(resultObjBean.getDate(),"yyyy-MM-dd"), 0xFF40db25,"假"));
                map.put(DateUtil.date2TimeStamp(resultObjBean.getDate(), "yyyy-MM-dd"),resultObjBean);
            }
            calendarView.setSchemeDate(schemes);
            calendarView.setOnDateSelectedListener(this);
        }
    }
}

