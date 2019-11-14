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
import gzkj.easygroupmeal.adapter.NoticeAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Date;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;

public class MyLeaveActivity extends BaseActivity {

    @BindView(R.id.leave_recycler)
    LRecyclerView leaveRecycler;
    private NoticeAdapter mNoticeAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private String sid;
    private CommitParam commitParam;
    private List<Date.ResultObjBean> mData;

    @Override
    public int intiLayout() {
        return R.layout.activity_my_leave;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        mData = new ArrayList<>();
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_14)
                .setColorResource(R.color.line_color)
                .build();
        leaveRecycler.addItemDecoration(divider);
        mNoticeAdapter = new NoticeAdapter(MyApplication.getContextObject(), R.layout.leave_item, "leave");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mNoticeAdapter);

        mLRecyclerViewAdapter.addHeaderView(View.inflate(this, R.layout.schedule_top, null));

        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        leaveRecycler.setLayoutManager(manager);

        leaveRecycler.setAdapter(mLRecyclerViewAdapter);

        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.MYLEAVEINTERFACE, commitParam);
        presenter=new Presenter(this);
        presenter.getData("POST", "我的请假记录", HttpUrl.MYLEAVE_URL);

    }

    @Override
    public void setListener() {
        leaveRecycler.setPullRefreshEnabled(false);
        leaveRecycler.setLoadMoreEnabled(false);

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("我的请假记录".equals(flag)) {
            Date date=new Gson().fromJson(object,Date.class);
            mData.clear();
            mData.addAll(date.getResultObj());
            mNoticeAdapter.setDataList(mData);
        }
    }
}
