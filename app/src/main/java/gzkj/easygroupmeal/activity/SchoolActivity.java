package gzkj.easygroupmeal.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.CompanyAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Company;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.TextChange;

public class SchoolActivity extends BaseActivity {

    @BindView(R.id.search_school_ed)
    EditText searchSchoolEd;
    @BindView(R.id.school_recycler)
    LRecyclerView schoolRecycler;

    private CompanyAdapter mCompanyAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<Company.ResultObjBean> mData;
    private CommitParam commitParam;
    private String sid;
    private String schoolName="";

    @Override
    public int intiLayout() {
        return R.layout.activity_school;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(this, "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        mData = new ArrayList<>();
        mCompanyAdapter = new CompanyAdapter(MyApplication.getContextObject(), R.layout.join_company_item, "search_school");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mCompanyAdapter);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        schoolRecycler.setLayoutManager(manager);
        schoolRecycler.setAdapter(mLRecyclerViewAdapter);
    }

    @Override
    public void toActivityData(String flag, String object) {
        if (flag.equals("搜索校区")) {
            Company login = new Gson().fromJson(object, Company.class);
            mData.clear();
            mData.addAll(login.getResultObj());
            mCompanyAdapter.setCurrentItem(-1);
            mCompanyAdapter.setDataList(mData);
        }
    }

    @Override
    public void fail(String flag, Throwable t) {
        if (flag.equals("搜索校区")) {
            mData.clear();
            mCompanyAdapter.setCurrentItem(-1);
            mCompanyAdapter.clear();
        }
    }

    @Override
    public void setListener() {
        TextChange textChange = new OnTextChange();
        searchSchoolEd.addTextChangedListener(textChange);
        schoolRecycler.setLoadMoreEnabled(false);
        schoolRecycler.setPullRefreshEnabled(false);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                schoolName = mData.get(position).getSchoolZone();
                //改变选中状态
                mCompanyAdapter.setCurrentItem(position);
                //通知LRecyclerViewAdapter改变状态
                mCompanyAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.back, R.id.sure, R.id.clear_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                EventBus.getDefault().post(schoolName);
                finish();
                break;
            case R.id.clear_iv:
                searchSchoolEd.setText("");
                break;
        }
    }

    class OnTextChange extends TextChange {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (!searchSchoolEd.getText().toString().trim().equals("")) {
                commitParam = new CommitParam();
                commitParam.setSchoolZone(searchSchoolEd.getText().toString().trim());
                body = commitParam.getBody(sid, HttpUrl.SCHOOLINTERFACE, commitParam);
                presenter = new Presenter(SchoolActivity.this);
                presenter.getData("POST", "搜索校区", HttpUrl.SCHOOL_URL);
            } else {
                mData.clear();
                mCompanyAdapter.setCurrentItem(-1);
                mCompanyAdapter.clear();
            }
        }
    }
}
