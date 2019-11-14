package gzkj.easygroupmeal.fragment;


import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.activity.AddAdministratorsActivity;
import gzkj.easygroupmeal.activity.ApplyActivity;
import gzkj.easygroupmeal.activity.AuthenticationActivity;
import gzkj.easygroupmeal.activity.JoinCompanyActivity;
import gzkj.easygroupmeal.activity.MainActivity;
import gzkj.easygroupmeal.activity.TeamStaffActivity;
import gzkj.easygroupmeal.adapter.TeamAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseFragment;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Company;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DensityUtil;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.StatusBarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.position_tv)
    TextView positionTv;
    @BindView(R.id.verify_tv)
    TextView verifyTv;
    @BindView(R.id.team_apply_red)
    TextView teamApplyRed;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.company_tv)
    TextView companyTv;
    @BindView(R.id.team_recycler)
    LRecyclerView teamRecycler;
    @BindView(R.id.team_join_company_tv)
    TextView teamJoinCompanyTv;
    @BindView(R.id.team_linear)
    LinearLayout teamLinear;
    private CommitParam commitParam;
    private String sid;
    private String postType;
    private DialogCircle dialogCircle;
    private List<Company.ResultObjBean> mData;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private TeamAdapter mTeamAdapter;
    private Login login;
    private String verify;
    private MainActivity mainActivity;
    private TextView mainTeamApplyRed;

    public TeamFragment() {
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
        return R.layout.fragment_team;
    }

    @Override
    protected void initView() {
        StatusBarUtils.with(getActivity())
                .init();
        mainActivity = (MainActivity) getActivity();
        mainTeamApplyRed = mainActivity.findViewById(R.id.team_apply_red);
        //设置刷新时动画的颜色，可以设置4个
        if (refreshLayout != null) {
            refreshLayout.setProgressViewOffset(false, 0, DensityUtil.dip2px(getActivity(), 48));
            refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            refreshLayout.setOnRefreshListener(this);
        }
        if (login != null) {
            nameTv.setText(login.getResultObj().getUserName());
            companyTv.setText(login.getResultObj().getCompanyName()+"   ");
            positionTv.setText(login.getResultObj().getPostName());
            verify = login.getResultObj().getVerifyState();
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

        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        postType = mShared.getSharedPreference("posttype", "").toString();
        if (HttpUrl.POSITION[0].equals(postType)) {
            teamJoinCompanyTv.setVisibility(View.GONE);
        }
        if (HttpUrl.POSITION[2].equals(postType)) {
            teamLinear.setVisibility(View.GONE);
        }else {
            teamLinear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        DividerDecoration divider = new DividerDecoration.Builder(getContext())
                .setHeight(R.dimen.dp_1)
                .setColorResource(R.color.line_color)
                .build();
        teamRecycler.addItemDecoration(divider);
        mTeamAdapter = new TeamAdapter(MyApplication.getContextObject(), R.layout.team_item, "team");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mTeamAdapter);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        teamRecycler.setLayoutManager(manager);
        teamRecycler.setAdapter(mLRecyclerViewAdapter);
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.TEAMUSERINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "团队员工列表", HttpUrl.TEAMUSER_URL);
    }

    @Override
    protected void setListener() {
        teamRecycler.setPullRefreshEnabled(false);
        teamRecycler.setLoadMoreEnabled(false);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    refreshLayout.setEnabled(true);
                } else {
                    refreshLayout.setEnabled(false);
                }
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity(),TeamStaffActivity.class);
                intent.putExtra("staff",mData.get(position));
                startActivityForResult(intent, 1001);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();
        refreshApply();
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("团队员工列表".equals(flag)) {
            Company company = new Gson().fromJson(object, Company.class);
            mData.clear();
            mData.addAll(company.getResultObj());
            mTeamAdapter.setDataList(mData);
            refreshLayout.setRefreshing(false);
        }
        if ("申请列表红点".equals(flag)) {
            Login login = new Gson().fromJson(object, Login.class);
            if (login.getResultObj().getVerfityCount()<100&&login.getResultObj().getVerfityCount()>0) {
                teamApplyRed.setVisibility(View.VISIBLE);
                mainTeamApplyRed.setVisibility(View.VISIBLE);
                teamApplyRed.setText(String.valueOf(login.getResultObj().getVerfityCount()));
                mainTeamApplyRed.setText(String.valueOf(login.getResultObj().getVerfityCount()));
            }else if (login.getResultObj().getVerfityCount()>=100){
                teamApplyRed.setVisibility(View.VISIBLE);
                mainTeamApplyRed.setVisibility(View.VISIBLE);
                teamApplyRed.setText("99+");
                mainTeamApplyRed.setText("99+");
            }else {
                teamApplyRed.setVisibility(View.GONE);
                mainTeamApplyRed.setVisibility(View.GONE);
            }
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
            mShared.put("name", login.getResultObj().getUserName());
            mShared.put("status", login.getResultObj().getStatus());
            refreshLayout.setRefreshing(false);

        }
    }

    @Override
    public void fail(String flag, Throwable t) {
        super.fail(flag, t);
        refreshLayout.setRefreshing(false);
    }

    @OnClick({ R.id.verify_tv,R.id.team_apply_tv, R.id.team_join_company_tv, R.id.team_creat_team_tv, R.id.team_add_staff_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            case R.id.team_apply_tv:
                startActivityForResult(new Intent(getActivity(), ApplyActivity.class), 1001);
                break;
            case R.id.team_join_company_tv:
                if ("0".equals(verify)) {
                    toastShort("正在审核");
                } else if ("1".equals(verify)) {
                    toastShort("已有公司");
                } else {
                    Intent intent = new Intent(getContext(), JoinCompanyActivity.class);
                    intent.putExtra("posttype", postType);
                    startActivity(intent);
                }
                break;
            case R.id.team_creat_team_tv:
                break;
            case R.id.team_add_staff_tv:
                if (HttpUrl.POSITION[0].equals(postType)) {
                    Intent intentStaff = new Intent(getContext(), AddAdministratorsActivity.class);
                    intentStaff.putExtra("sid", sid);
                    intentStaff.putExtra("posttype", postType);
                    intentStaff.putExtra("flag", getString(R.string.add_member));
                    intentStaff.putExtra("type", "company");
                    startActivityForResult(intentStaff, 1001);
                } else {
                    View addStaffView = View.inflate(getContext(), R.layout.add_staff, null);
                    TextView perSonTv = addStaffView.findViewById(R.id.person_in_charge);
                    TextView addStaffTv = addStaffView.findViewById(R.id.add_staff);
                    TextView cancelTv = addStaffView.findViewById(R.id.cancel_tv);
                    TextView[] tv = new TextView[]{perSonTv, addStaffTv, cancelTv};
                    dialogCircle = MyApplication.getInstance().getPop(getContext(), addStaffView, 1.1f, 3, Gravity.BOTTOM, R.style.BottomDialog_Animation, true);
                    for (int i = 0; i < tv.length; i++) {
                        final int finalI = i;
                        tv[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (finalI == 0) {
                                    Intent intent = new Intent(getContext(), AddAdministratorsActivity.class);
                                    intent.putExtra("sid", sid);
                                    intent.putExtra("posttype", postType);
                                    intent.putExtra("flag", getString(R.string.person_in_charge));
                                    intent.putExtra("type", "team");
                                    startActivity(intent);
                                }
                                if (finalI == 1) {
                                    Intent intent = new Intent(getContext(), AddAdministratorsActivity.class);
                                    intent.putExtra("sid", sid);
                                    intent.putExtra("posttype", postType);
                                    intent.putExtra("flag", getString(R.string.add_member));
                                    intent.putExtra("type", "team");
                                    startActivityForResult(intent, 1001);
                                }
                                dialogCircle.dismiss();
                            }
                        });
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001 && requestCode == 1001) {
            commitParam = new CommitParam();
            body = commitParam.getBody(sid, HttpUrl.TEAMUSERINTERFACE, commitParam);
            presenter = new Presenter(this);
            presenter.getData("POST", "团队员工列表", HttpUrl.TEAMUSER_URL);
        }
    }
    public void refreshUserInfo() {
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.USERINFOINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "个人信息", HttpUrl.USERINFO_URL);
    }
    public void refreshApply() {
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.APPLYREDDOTINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "申请列表红点", HttpUrl.APPLYREDDOT_URL);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        refreshUserInfo();
        refreshApply();
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.TEAMUSERINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "团队员工列表", HttpUrl.TEAMUSER_URL);
    }
}
