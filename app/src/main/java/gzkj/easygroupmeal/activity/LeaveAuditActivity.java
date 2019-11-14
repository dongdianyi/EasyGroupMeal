package gzkj.easygroupmeal.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.TeamAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Date;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.view.OnChangeView;

public class LeaveAuditActivity extends BaseActivity {

    @BindView(R.id.leave_recycler)
    LRecyclerView leaveRecycler;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private TeamAdapter mTeamAdapter;
    private String sid;
    private CommitParam commitParam;
    private List<Date.ResultObjBean> mData;
    private String status;

    @Override
    public int intiLayout() {
        return R.layout.activity_leave_audit;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        mData=new ArrayList<>();
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_14)
                .setColorResource(R.color.line_color)
                .build();
        leaveRecycler.addItemDecoration(divider);
        mTeamAdapter = new TeamAdapter(MyApplication.getContextObject(), R.layout.leave_audit_item, "leave");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mTeamAdapter);
        mLRecyclerViewAdapter.addHeaderView(View.inflate(this, R.layout.schedule_top, null));
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        leaveRecycler.setLayoutManager(manager);
        leaveRecycler.setAdapter(mLRecyclerViewAdapter);

        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.LEAVEAUDITINTERFACE, commitParam);
        presenter=new Presenter(this);
        presenter.getData("POST", "请假申请列表", HttpUrl.LEAVEAUDIT_URL);
    }

    @Override
    public void setListener() {
        leaveRecycler.setPullRefreshEnabled(false);
        leaveRecycler.setLoadMoreEnabled(false);
        mTeamAdapter.setOnChangeListener(new OnChangeView() {
            @Override
            public void onChange(View view, int position, String flag) {
                //处理同意拒绝
                commitParam = new CommitParam();
                commitParam.setId(mData.get(position).getId());
                commitParam.setState(flag);
                body = commitParam.getBody(sid, HttpUrl.LEAVEOPERATIONINTERFACE, commitParam);
                presenter = new Presenter(LeaveAuditActivity.this);
                presenter.getData("POST", "请假操作", HttpUrl.LEAVEOPERATION_URL);
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("请假申请列表".equals(flag)) {
            Date date=new Gson().fromJson(object,Date.class);
            mData.clear();
            mData.addAll(date.getResultObj());
            mTeamAdapter.setDataList(mData);
        }
        if ("请假操作".equals(flag)) {
            commitParam = new CommitParam();
            body = commitParam.getBody(sid, HttpUrl.LEAVEAUDITINTERFACE, commitParam);
            presenter=new Presenter(this);
            presenter.getData("POST", "请假申请列表", HttpUrl.LEAVEAUDIT_URL);
        }
    }
}
