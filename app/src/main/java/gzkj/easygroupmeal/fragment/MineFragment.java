package gzkj.easygroupmeal.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.activity.AttendanceManagementAcitivity;
import gzkj.easygroupmeal.activity.AuthenticationActivity;
import gzkj.easygroupmeal.activity.LeaveActivity;
import gzkj.easygroupmeal.activity.LeaveAuditActivity;
import gzkj.easygroupmeal.activity.MainActivity;
import gzkj.easygroupmeal.activity.MyLeaveActivity;
import gzkj.easygroupmeal.activity.NoticeActivity;
import gzkj.easygroupmeal.activity.PersonalDataActivity;
import gzkj.easygroupmeal.activity.SettingActivity;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseFragment;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DensityUtil;
import gzkj.easygroupmeal.utli.GlideLoadUtils;
import gzkj.easygroupmeal.utli.NotifyingScrollView;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.StatusBarUtils;
import gzkj.easygroupmeal.view.OnChangeViewFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.mine_avatar)
    CircleImageView mineAvatar;
    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.mine_avatar_tv)
    TextView mineAvatarTv;
    @BindView(R.id.scroll)
    NotifyingScrollView scroll;
    @BindView(R.id.username_tv)
    TextView usernameTv;
    @BindView(R.id.mine_title)
    TextView mineTitle;
    @BindView(R.id.company_tv)
    TextView companyTv;
    @BindView(R.id.position_tv)
    TextView positionTv;
    @BindView(R.id.mine_work)
    TextView mineWork;
    @BindView(R.id.mine_attendance)
    TextView mineAttendance;
    @BindView(R.id.leave_audit)
    RelativeLayout leaveAudit;
    @BindView(R.id.invitation_line)
    TextView invitation_line;
    @BindView(R.id.leave_line)
    TextView leave_line;
    @BindView(R.id.customerservice_line)
    TextView customerservice_line;
    @BindView(R.id.notice_line)
    TextView notice_line;
    @BindView(R.id.mine_linear)
    LinearLayout mineLinear;
    @BindView(R.id.invitation_linear)
    LinearLayout invitation_linear;
    @BindView(R.id.leave_linear)
    LinearLayout leave_linear;
    @BindView(R.id.attendance_management_linear)
    LinearLayout attendance_management_linear;
    @BindView(R.id.notice_linear)
    LinearLayout notice_linear;
    private CommitParam commitParam;
    private String sid;
    private String postType;
    private Login login;

    public MineFragment() {
        // Required empty public constructor
    }

    //声明接口对象
    private OnChangeViewFragment mOnChangeView;

    //设置监听器,实例化接口
    public void setOnChangeListener(OnChangeViewFragment onChangeView) {
        mOnChangeView = onChangeView;
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
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        StatusBarUtils.with(getActivity())
                .init();
        //设置刷新时动画的颜色，可以设置4个
        if (refreshLayout != null) {
            refreshLayout.setProgressViewOffset(false, 0, DensityUtil.dip2px(getActivity(), 48));
            refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            refreshLayout.setOnRefreshListener(this);
        }
        if (login != null) {
            String name = login.getResultObj().getUserName();
            usernameTv.setText(name);
            positionTv.setText(login.getResultObj().getPostName());
            if (login.getResultObj().getCompanyName().length() > 0) {
                companyTv.setText(login.getResultObj().getCompanyName());
            } else {
                companyTv.setVisibility(View.INVISIBLE);
            }
            if (login.getResultObj().getHead() != null && login.getResultObj().getHead().length() > 0) {
                mineAvatarTv.setVisibility(View.GONE);
                GlideLoadUtils.getInstance().glideLoad(getContext(), login.getResultObj().getHead(), mineAvatar, R.mipmap.photo);
            } else {
                mineAvatarTv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(name) && name.length() >= 2) {
                    mineAvatarTv.setText(name.substring(name.length() - 2, name.length()));
                } else {
                    mineAvatarTv.setText(name);
                }
            }
        }
        scroll.setOnScrollChangedListener(onScrollChangedListener);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private NotifyingScrollView.OnScrollChangedListener onScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        @Override
        public void OnScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            final int headerHeight = 100;
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            mineTitle.setBackgroundColor(Color.rgb(56, 125, 254));
            mineTitle.getBackground().setAlpha(newAlpha);
        }
    };

    @Override
    protected void initData() {
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        postType = mShared.getSharedPreference("posttype", "").toString();
        if (HttpUrl.POSITION[3].equals(postType)) {

            relative.setBackgroundResource(R.color.blue);
            mineLinear.setVisibility(View.GONE);
            companyTv.setVisibility(View.INVISIBLE);
            //显示邀请
            invitation_line.setVisibility(View.VISIBLE);
            invitation_linear.setVisibility(View.VISIBLE);
            //隐藏请假
            leave_linear.setVisibility(View.GONE);
            leave_line.setVisibility(View.GONE);
            //隐藏考勤管理
            attendance_management_linear.setVisibility(View.GONE);
            commitParam = new CommitParam();
            body = commitParam.getBody(sid, HttpUrl.USERINFOINTERFACE, commitParam);
            presenter = new Presenter(this);
            presenter.getData("POST", "个人信息", HttpUrl.USERINFO_URL);
        }
        if (HttpUrl.POSITION[0].equals(postType)) {
            leave_linear.setVisibility(View.GONE);
            leave_line.setVisibility(View.GONE);
            attendance_management_linear.setVisibility(View.VISIBLE);
            notice_linear.setVisibility(View.VISIBLE);
            notice_line.setVisibility(View.VISIBLE);
            mineWork.setVisibility(View.VISIBLE);
            mineAttendance.setVisibility(View.GONE);
            leaveAudit.setVisibility(View.VISIBLE);
        }
        if (HttpUrl.POSITION[1].equals(postType)) {
            leave_linear.setVisibility(View.VISIBLE);
            leave_line.setVisibility(View.VISIBLE);
            notice_linear.setVisibility(View.VISIBLE);
            notice_line.setVisibility(View.VISIBLE);
            mineWork.setVisibility(View.GONE);
            leaveAudit.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.position_tv, R.id.mine_avatar, R.id.mine_attendance, R.id.mine_work, R.id.leave_audit, R.id.customerservice_linear, R.id.invitation_linear, R.id.leave_linear, R.id.attendance_management_linear, R.id.notice_linear, R.id.setting_linear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.position_tv:
                startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                break;
            case R.id.mine_avatar:
                startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                break;
            case R.id.mine_attendance:
                startActivity(new Intent(getActivity(), MyLeaveActivity.class));
                break;
            case R.id.mine_work:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.target = 1;
                mainActivity.checkChange(mainActivity.target);
                mainActivity.getFragment();
                break;
            case R.id.leave_audit:
                startActivity(new Intent(getActivity(), LeaveAuditActivity.class));
                break;
            case R.id.invitation_linear:
                toastShort("敬请期待");
//                startActivity(new Intent(getActivity(), InvitationActivity.class));
                break;
            case R.id.leave_linear:
//                toastShort("敬请期待");
                startActivity(new Intent(getActivity(), LeaveActivity.class));
                break;
            case R.id.notice_linear:
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                intent.putExtra("sid", sid);
                startActivity(intent);
                break;
            case R.id.setting_linear:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.attendance_management_linear:
//                toastShort("敬请期待");
                startActivity(new Intent(getActivity(), AttendanceManagementAcitivity.class));
                break;
            case R.id.customerservice_linear:
                callPhone(HttpUrl.PHONENUM);
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        Login login = new Gson().fromJson(object, Login.class);
        String name = login.getResultObj().getUserName();
        String verify = login.getResultObj().getVerifyState();
        usernameTv.setText(name);
        if (login.getResultObj().getCompanyName().length() > 0) {
            companyTv.setText(login.getResultObj().getCompanyName());
        } else {
            companyTv.setVisibility(View.INVISIBLE);
        }
        if (HttpUrl.POSITION[3].equals(postType)) {
            if ("0".equals(verify)) {
                positionTv.setText(login.getResultObj().getPostName() + "(认证中)");
                positionTv.setEnabled(false);
            }
            if ("1".equals(verify)) {
                positionTv.setText(login.getResultObj().getPostName());
            }
            if ("2".equals(verify)) {
                positionTv.setEnabled(true);
                positionTv.setText(login.getResultObj().getPostName() + "(认证失败,点击重新认证)");
            }
        } else {
            positionTv.setText(login.getResultObj().getPostName());
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        ScheduleFragment scheduleFragment = (ScheduleFragment) mainActivity.mFragments.get(0);
        TeamFragment teamFragment = (TeamFragment) mainActivity.mFragments.get(2);
        if (teamFragment.isAdded() && teamFragment.nameTv != null) {
            teamFragment.nameTv.setText(name);
            if ("0".equals(verify)) {
                teamFragment.verifyTv.setVisibility(View.VISIBLE);
                teamFragment.verifyTv.setEnabled(false);
                teamFragment.verifyTv.setText(getString(R.string.in_audit));
            }
            if ("1".equals(verify)) {
                teamFragment.verifyTv.setVisibility(View.GONE);
            }
            if ("2".equals(verify)) {
                teamFragment.verifyTv.setVisibility(View.VISIBLE);
                teamFragment.verifyTv.setEnabled(true);
                teamFragment.verifyTv.setText(getString(R.string.audit_reject));
            }
        }
        if (scheduleFragment.isAdded() && scheduleFragment.nameTv != null) {
            scheduleFragment.nameTv.setText(name);
            if ("0".equals(verify)) {
                scheduleFragment.verifyTv.setVisibility(View.VISIBLE);
                scheduleFragment.verifyTv.setEnabled(false);
                scheduleFragment.verifyTv.setText(getString(R.string.in_audit));
            }
            if ("1".equals(verify)) {
                scheduleFragment.verifyTv.setVisibility(View.GONE);
            }
            if ("2".equals(verify)) {
                scheduleFragment.verifyTv.setVisibility(View.VISIBLE);
                scheduleFragment.verifyTv.setEnabled(true);
                scheduleFragment.verifyTv.setText(getString(R.string.audit_reject));
            }
        }
        if (login.getResultObj().getHead() != null && login.getResultObj().getHead().length() > 0) {
            mineAvatarTv.setVisibility(View.GONE);
            GlideLoadUtils.getInstance().glideLoad(getContext(), login.getResultObj().getHead(), mineAvatar, R.mipmap.photo);
        } else {
            mineAvatarTv.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(name) && name.length() >= 2) {
                mineAvatarTv.setText(name.substring(name.length() - 2, name.length()));
            } else {
                mineAvatarTv.setText(name);
            }
        }
        mShared.put("head", login.getResultObj().getHead());
        mOnChangeView.onChange(login);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void fail(String flag, Throwable t) {
        super.fail(flag, t);
        refreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void Event(String isUpdate) {
        if ("userInfo".equals(isUpdate)) {
            commitParam = new CommitParam();
            body = commitParam.getBody(sid, HttpUrl.USERINFOINTERFACE, commitParam);
            presenter = new Presenter(this);
            presenter.getData("POST", "个人信息", HttpUrl.USERINFO_URL);
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.USERINFOINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "个人信息", HttpUrl.USERINFO_URL);
    }
}
