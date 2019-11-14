package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
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
import gzkj.easygroupmeal.bean.Company;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.view.OnChangeView;

public class ApplyActivity extends BaseActivity {

    @BindView(R.id.apply_recycler)
    LRecyclerView applyRecycler;
    private List<Company.ResultObjBean> mData;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private TeamAdapter mTeamAdapter;
    private String sid;
    private CommitParam commitParam;
    private View headView;
    private int index;

    @Override
    public int intiLayout() {
        return R.layout.activity_apply;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_14)
                .setColorResource(R.color.line_color)
                .build();
        applyRecycler.addItemDecoration(divider);
        mData=new ArrayList<>();
        mTeamAdapter = new TeamAdapter(MyApplication.getContextObject(), R.layout.apply_item, "apply");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mTeamAdapter);
        headView = LayoutInflater.from(MyApplication.getContextObject()).inflate(R.layout.schedule_top, null, false);
        mLRecyclerViewAdapter.addHeaderView(headView);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        applyRecycler.setLayoutManager(manager);
        applyRecycler.setAdapter(mLRecyclerViewAdapter);
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.APPLYLISTINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "申请列表", HttpUrl.APPLYLIST_URL);
    }

    @Override
    public void setListener() {
        applyRecycler.setPullRefreshEnabled(false);
        applyRecycler.setLoadMoreEnabled(false);
        mTeamAdapter.setOnChangeListener(new OnChangeView() {
            @Override
            public void onChange(View view, int position, String flag) {
                //处理同意拒绝
                index = position;
                commitParam = new CommitParam();
                commitParam.setVerfityId(mData.get(position).getVerfityId());
                commitParam.setResultType(flag);
                body = commitParam.getBody(sid, HttpUrl.APPLYINTERFACE, commitParam);
                presenter = new Presenter(ApplyActivity.this);
                presenter.getData("POST", "申请操作", HttpUrl.APPLY_URL);
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("申请列表".equals(flag)) {
            Company company=new Gson().fromJson(object,Company.class);
            mData.clear();
            mData.addAll(company.getResultObj());
            mTeamAdapter.setDataList(mData);
        }
        if ("申请操作".equals(flag)) {
            mData.remove(index);
            mTeamAdapter.setDataList(mData);
            Intent intent = new Intent();
            setResult(1001, intent);
        }
    }
}
