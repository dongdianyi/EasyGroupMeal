package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

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
import gzkj.easygroupmeal.adapter.AttendanceAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Cycle;
import gzkj.easygroupmeal.bean.TeamStaff;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;

public class AttendanceManagementAcitivity extends BaseActivity {

    @BindView(R.id.attendance_recycler)
    LRecyclerView attendanceRecycler;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private AttendanceAdapter attendanceAdapter;
    private CommitParam commitParam;
    private String sid;
    private  List<Cycle> mDataTeam;
    @Override
    public int intiLayout() {
        return R.layout.activity_attendance_management_acitivity;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        mDataTeam=new ArrayList<>();
        attendanceAdapter = new AttendanceAdapter(MyApplication.getContextObject(), R.layout.attendance_management_item, "attendance");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(attendanceAdapter);
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_1)
                .setColorResource(R.color.line_color)
                .build();
        attendanceRecycler.addItemDecoration(divider);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        attendanceRecycler.setLayoutManager(manager);
        attendanceRecycler.setAdapter(mLRecyclerViewAdapter);
        commitParam = new CommitParam();
        commitParam.setType("1");
        body = commitParam.getBody(sid, HttpUrl.TEAMINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "团队员工列表", HttpUrl.TEAM_URL);
    }

    @Override
    public void setListener() {
        attendanceRecycler.setLoadMoreEnabled(false);
        attendanceRecycler.setPullRefreshEnabled(false);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(AttendanceManagementAcitivity.this,SelectDateActivity.class);
                intent.putExtra("teamId",mDataTeam.get(position).getCycleNum());
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("团队员工列表".equals(flag)) {
           TeamStaff teamStaff = new Gson().fromJson(object, TeamStaff.class);
            mDataTeam.clear();
            for (TeamStaff.ResultObjBean resultObjBean : teamStaff.getResultObj()) {
                mDataTeam.add(new Cycle(resultObjBean.getTeamName(), resultObjBean.getTeamId()));
            }
            attendanceAdapter.setDataList(mDataTeam);
        }
    }
}
