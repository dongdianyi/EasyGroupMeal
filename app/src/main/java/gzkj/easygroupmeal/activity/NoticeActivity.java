package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.NoticeAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Notice;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.CompareUtil;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.notice_recycler)
    LRecyclerView noticeRecycler;
    private NoticeAdapter mNoticeAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private String sid;
    private CommitParam commitParam;

    @Override
    public int intiLayout() {
        return R.layout.activity_notice;
    }

    @Override
    public void initData() {
        hideTopUIMenu();

        sid = getIntent().getStringExtra("sid");

        mNoticeAdapter = new NoticeAdapter(MyApplication.getContextObject(), R.layout.notice_item, "notice");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mNoticeAdapter);

        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        noticeRecycler.setLayoutManager(manager);

        noticeRecycler.setAdapter(mLRecyclerViewAdapter);

        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.QUERYNOTICEINTERFACE, commitParam);
        presenter=new Presenter(this);
        presenter.getData("POST", "查询公告", HttpUrl.QUERYNOTICE_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().removeStickyEvent(String.class);
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void setListener() {
        noticeRecycler.setPullRefreshEnabled(false);
        noticeRecycler.setLoadMoreEnabled(false);

    }

    @OnClick({R.id.back, R.id.add_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add_notice:
                Intent intent = new Intent(this, AddNoticeActivity.class);
                intent.putExtra("sid", sid);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        Notice notice = new Gson().fromJson(object, Notice.class);
        Collections.sort(notice.getResultObj().getNotices(), CompareUtil.createComparator(-1, "createtime"));
        mLRecyclerViewAdapter.addFooterView(getLayoutInflater().inflate(R.layout.schedule_top, (ViewGroup)getWindow().getDecorView(),false));
        mNoticeAdapter.setDataList(notice.getResultObj().getNotices());
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void Event(String isUpdate) {
        if ("notice".equals(isUpdate)) {
            commitParam = new CommitParam();
            body = commitParam.getBody(sid, HttpUrl.QUERYNOTICEINTERFACE, commitParam);
            presenter=new Presenter(this);
            presenter.getData("POST", "查询公告", HttpUrl.QUERYNOTICE_URL);
        }
    }
}
