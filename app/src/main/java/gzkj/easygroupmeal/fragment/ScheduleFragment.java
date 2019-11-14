package gzkj.easygroupmeal.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.activity.AddScheduleActivity;
import gzkj.easygroupmeal.activity.AuthenticationActivity;
import gzkj.easygroupmeal.activity.JoinCompanyActivity;
import gzkj.easygroupmeal.activity.LoginRegisterActivity;
import gzkj.easygroupmeal.activity.MainActivity;
import gzkj.easygroupmeal.activity.TaskMessageActivity;
import gzkj.easygroupmeal.adapter.RecyclerViewAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseFragment;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.bean.Task;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DensityUtil;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.RefreshLayout;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.StatusBarUtils;
import gzkj.easygroupmeal.view.OnChangeViewFragment;
import meal.group.easy.gzkj.calendarlibrary.Calendar;
import meal.group.easy.gzkj.calendarlibrary.CalendarLayout;
import meal.group.easy.gzkj.calendarlibrary.CalendarView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends BaseFragment implements CalendarView.OnDateSelectedListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnDateLongClickListener,
        CalendarView.OnViewChangeListener
        , SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.position_tv)
    TextView positionTv;
    @BindView(R.id.company_tv)
    TextView companyTv;
    @BindView(R.id.verify_tv)
    TextView verifyTv;
    @BindView(R.id.tv_year)
    TextView mTextYear;
    @BindView(R.id.tv_lunar)
    TextView mTextLunar;
    @BindView(R.id.ib_calendar)
    ImageView ibCalendar;
    @BindView(R.id.tv_current_day)
    TextView mTextCurrentDay;
    @BindView(R.id.fl_current)
    FrameLayout flCurrent;
    @BindView(R.id.rl_tool)
    RelativeLayout mRelativeTool;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private int mYear;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<Task.ResultObjBean> mData;
    private CommitParam commitParam;
    private String sid;
    private Calendar calendar;
    private String postType;
    private Login login;

    //声明接口对象
    private OnChangeViewFragment mOnChangeView;
    private MainActivity mainActivity;

    //设置监听器,实例化接口
    public void setOnChangeListener(OnChangeViewFragment onChangeView) {
        mOnChangeView = onChangeView;
    }

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public void setChangeView(Login login) {
        this.login = login;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initView() {
        StatusBarUtils.with(getActivity())
                .init();
//        setStatusBarDarkMode();
        mainActivity = (MainActivity) getActivity();
        //设置刷新时动画的颜色，可以设置4个
        if (refreshLayout != null) {
            refreshLayout.setProgressViewOffset(false, 0, DensityUtil.dip2px(getActivity(), 48));
            refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            refreshLayout.setOnRefreshListener(this);
        }
        if (login != null) {
            changeUi();
        }
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        postType = mShared.getSharedPreference("posttype", "").toString();
        nameTv.setText(mShared.getSharedPreference("name", "").toString());
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        if (mCalendarView.getCurMonth()+2>12) {
            mCalendarView.setRange(mCalendarView.getCurYear(),mCalendarView.getCurMonth(),mCalendarView.getCurYear()+1,mCalendarView.getCurMonth()-10);
        }else {
            mCalendarView.setRange(mCalendarView.getCurYear(),mCalendarView.getCurMonth(),mCalendarView.getCurYear(),mCalendarView.getCurMonth()+2);
        }
        mCalendarView.scrollToCurrent();
        refreshUserInfo();

    }

    private void changeUi() {
        String name = login.getResultObj().getUserName();
        String companyName = login.getResultObj().getCompanyName();
        String postName = login.getResultObj().getPostName();
        nameTv.setText(name);
        companyTv.setText(companyName + "   ");
        positionTv.setText(postName);
        String verify = login.getResultObj().getVerifyState();
        if ("0".equals(verify)) {
            verifyTv.setVisibility(View.VISIBLE);
            verifyTv.setEnabled(false);
            verifyTv.setText(getString(R.string.in_audit));
        }
        if ("1".equals(verify)) {
            verifyTv.setVisibility(View.GONE);
        }
        if ("2".equals(verify)) {
            verifyTv.setVisibility(View.VISIBLE);
            verifyTv.setEnabled(true);
            verifyTv.setText(getString(R.string.audit_reject));
        }
    }

    @Override
    protected void initData() {
        final List<Calendar> schemes = new ArrayList<>();
        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();
//        schemes.add(getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
//        schemes.add(getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        mCalendarView.setSchemeDate(schemes);


        mData = new ArrayList<>();
//        GridItemDecoration divider = new GridItemDecoration.Builder(getContext())
//                .setVertical(R.dimen.dp_10)
//                .setColorResource(R.color.line_color)
//                .build();
//        DividerDecoration divider = new DividerDecoration.Builder(getContext())
//                .setHeight(R.dimen.dp_14)
//                .setColorResource(R.color.line_color)
//                .build();
        mRecyclerViewAdapter = new RecyclerViewAdapter(getContext());
//        mRecyclerViewAdapter.addHeaderView(View.inflate(getContext(), R.layout.schedule_top, null));
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        DividerItemDecoration decor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decor.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divide)); //这里在就是
        mRecyclerView.addItemDecoration(decor);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("日程个人任务".equals(flag)) {
            Task task = new Gson().fromJson(object, Task.class);
            mData.clear();
            mData.addAll(task.getResultObj());
            mRecyclerViewAdapter.setData(mData);
            if (mData.size() == 0) {
                toastShort("暂无数据");
            }
            refreshLayout.setRefreshing(false);
        }
        if ("个人信息".equals(flag)) {
            Login login = new Gson().fromJson(object, Login.class);
            String name = login.getResultObj().getUserName();
            String companyName = login.getResultObj().getCompanyName();
            String postName = login.getResultObj().getPostName();
            nameTv.setText(name);
            companyTv.setText(companyName + "   ");
            positionTv.setText(postName);
            String verify = login.getResultObj().getVerifyState();
            if ("0".equals(verify)) {
                verifyTv.setVisibility(View.VISIBLE);
                verifyTv.setEnabled(false);
                verifyTv.setText(getString(R.string.in_audit));
            }
            if ("1".equals(verify)) {
                verifyTv.setVisibility(View.GONE);
            }
            if ("2".equals(verify)) {
                verifyTv.setVisibility(View.VISIBLE);
                verifyTv.setEnabled(true);
                verifyTv.setText(getString(R.string.audit_reject));
            }
            mOnChangeView.onChange(login);
            mShared.put("name", login.getResultObj().getUserName());
            mShared.put("status", login.getResultObj().getStatus());
            mShared.put("head", login.getResultObj().getHead());
            refreshLayout.setRefreshing(false);
            if (login.getResultObj().getPost_type() == null || "".equals(login.getResultObj().getPost_type()) || "0".equals(login.getResultObj().getStatus())) {
                startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
            }
        }
    }

    @Override
    public void fail(String flag, Throwable t) {
        super.fail(flag, t);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        logger("resume");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().removeStickyEvent(String.class);
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void setListener() {
        flCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnDateLongClickListener(this, true);
        mCalendarView.setOnViewChangeListener(this);
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        this.calendar = calendar;
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
//        if (isClick) {
//            Toast.makeText(getActivity(), getCalendarText(calendar), Toast.LENGTH_SHORT).show();
//        }
        refreshSchedul();
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
    public void onMonthChange(int year, int month) {
//Log.e("onMonthChange", "  -- " + year + "  --  " + month);
    }

    @Override
    public void onDateLongClick(Calendar calendar) {
//        Toast.makeText(getActivity(), "长按不选择日期\n" + getCalendarText(calendar), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onViewChange(boolean isMonthView) {
        if (isMonthView) {
            refreshLayout.setEnabled(true);
        } else {
            refreshLayout.setEnabled(false);
        }
        Log.e("onViewChange", "  ---  " + (isMonthView ? "月视图" : "周视图"));
    }

    private static String getCalendarText(Calendar calendar) {
        return String.format("新历%s \n 农历%s \n 公历节日：%s \n 农历节日：%s \n 节气：%s \n 是否闰月：%s",
                calendar.getMonth() + "月" + calendar.getDay() + "日",
                calendar.getLunarCakendar().getMonth() + "月" + calendar.getLunarCakendar().getDay() + "日",
                TextUtils.isEmpty(calendar.getGregorianFestival()) ? "无" : calendar.getGregorianFestival(),
                TextUtils.isEmpty(calendar.getTraditionFestival()) ? "无" : calendar.getTraditionFestival(),
                TextUtils.isEmpty(calendar.getSolarTerm()) ? "无" : calendar.getSolarTerm(),
                calendar.getLeapMonth() == 0 ? "否" : String.format("闰%s月", calendar.getLeapMonth()));
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @OnClick({R.id.add_schedule, R.id.verify_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_schedule:
                startActivity(new Intent(getContext(), AddScheduleActivity.class));
                break;
            case R.id.verify_tv:
                if (HttpUrl.POSITION[0].equals(postType) || HttpUrl.POSITION[1].equals(postType) || HttpUrl.POSITION[2].equals(postType)) {
                    Intent intent = new Intent(getActivity(), JoinCompanyActivity.class);
                    intent.putExtra("posttype", postType);
                    startActivity(intent);
                }
                if (HttpUrl.POSITION[3].equals(postType)) {
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    public void refreshUserInfo() {
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.USERINFOINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "个人信息", HttpUrl.USERINFO_URL);
    }

    public void refreshSchedul() {
        commitParam = new CommitParam();
        commitParam.setTaskCycle(calendar.getWeek() + "");
        body = commitParam.getBody(sid, HttpUrl.SCHEDULETASKINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "日程个人任务", HttpUrl.SCHEDULETASK_URL);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        refreshUserInfo();
        refreshSchedul();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void Event(String state) {
        //推送点击跳转state 1:任务 2：消息 3：申请
        logger("state" + state);
        if ("1".equals(state)) {
            mainActivity.target = 1;
            mainActivity.checkChange(mainActivity.target);
            mainActivity.getFragment();
        }
        if ("2".equals(state)) {
            startActivity(new Intent(getActivity(), TaskMessageActivity.class));
        }
        if ("3".equals(state)) {
            mainActivity.target = 2;
            mainActivity.checkChange(mainActivity.target);
            mainActivity.getFragment();
        }

    }
}
