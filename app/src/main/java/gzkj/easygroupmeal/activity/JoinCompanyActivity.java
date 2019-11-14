package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;

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
import gzkj.easygroupmeal.utli.AndroidBug5497Workaround;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.TextChange;

public class JoinCompanyActivity extends BaseActivity {


    @BindView(R.id.search_company_ed)
    EditText searchCompanyEd;
    @BindView(R.id.company_recycler)
    LRecyclerView companyRecycler;
    @BindView(R.id.join_team)
    TextView joinTeam;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.create_company)
    TextView createCompany;
    private CompanyAdapter mCompanyAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<Company.ResultObjBean> mData;
    private CommitParam commitParam;
    private String sid;
    private String postType;
    private String companyId,teamId;
    private Company login;
    private String companyName,teamName;
    private DialogCircle dialogCircle;

    @Override
    public int intiLayout() {
        return R.layout.activity_join_company;
    }

    @Override
    public void initData() {
        AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content));
        hideTopUIMenu();
        joinTeam.setEnabled(false);
        mShared = new SharedPreferencesHelper(this, "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        postType = getIntent().getStringExtra("posttype");
        if (HttpUrl.POSITION[2].equals(postType)||HttpUrl.POSITION[1].equals(postType)) {
            createCompany.setVisibility(View.GONE);
        }
        if (HttpUrl.POSITION[0].equals(postType)||HttpUrl.POSITION[1].equals(postType)) {
            title.setText(getString(R.string.join_company));
            joinTeam.setText(getString(R.string.join_company));
        }
        commitParam = new CommitParam();
        mData = new ArrayList<>();
        if (HttpUrl.POSITION[0].equals(postType)||HttpUrl.POSITION[1].equals(postType)) {
            mCompanyAdapter = new CompanyAdapter(MyApplication.getContextObject(), R.layout.join_company_item, "search_company");
        }else {
            mCompanyAdapter = new CompanyAdapter(MyApplication.getContextObject(), R.layout.join_company_item, "search_team");
        }
        mCompanyAdapter.setDataList(mData);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mCompanyAdapter);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        companyRecycler.setLayoutManager(manager);
        companyRecycler.setAdapter(mLRecyclerViewAdapter);
    }

    @Override
    public void setListener() {
        //监听所有的edittext
        TextChange textChange = new OnTextChange();
        searchCompanyEd.addTextChangedListener(textChange);
        companyRecycler.setLoadMoreEnabled(false);
        companyRecycler.setPullRefreshEnabled(false);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (HttpUrl.POSITION[0].equals(postType)||HttpUrl.POSITION[1].equals(postType)) {
                    companyId = mData.get(position).getCompanyId() + "";
                    companyName = mData.get(position).getCompanyName();
                }else {
                    teamId=mData.get(position).getTeamId()+"";
                    teamName = mData.get(position).getTeamName();
                }
                joinTeam.setEnabled(true);
                joinTeam.setBackgroundResource(R.drawable.fillet_blue);
                //改变选中状态
                mCompanyAdapter.setCurrentItem(position);
                //通知LRecyclerViewAdapter改变状态
                mCompanyAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void toActivityData(String flag, String object) {
        if (flag.equals("搜索公司")||flag.equals("搜索团队")) {
            login = new Gson().fromJson(object, Company.class);
            mData.clear();
            mData.addAll(login.getResultObj());
            joinTeam.setBackgroundResource(R.drawable.fillet_blueshallow);
            mCompanyAdapter.setCurrentItem(-1);
            mCompanyAdapter.setDataList(mData);
        }
        if (flag.equals("加入公司")||flag.equals("加入团队")) {
            login = new Gson().fromJson(object, Company.class);
            toastShort(login.getResultDesc());
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void fail(String flag, Throwable t) {
        if (flag.equals("搜索公司")||flag.equals("搜索团队")) {
            mData.clear();
            joinTeam.setBackgroundResource(R.drawable.fillet_blueshallow);
            mCompanyAdapter.setCurrentItem(-1);
            mCompanyAdapter.clear();
        }
        if (flag.equals("加入公司")||flag.equals("加入团队")) {
            toastShort(t.getMessage());
        }
    }

    @OnClick({R.id.create_company, R.id.back, R.id.clear_iv, R.id.join_team})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.create_company:
                Intent intent = new Intent(this, CreateTeamActivity.class);
                intent.putExtra("posttype", postType);
                startActivity(intent);
                break;
            case R.id.clear_iv:
                searchCompanyEd.setText("");
                break;
            case R.id.join_team:

                if (HttpUrl.POSITION[0].equals(postType)||HttpUrl.POSITION[1].equals(postType)) {
                    joinTeam("确定要加入此公司吗?",0);

                }
                if (HttpUrl.POSITION[2].equals(postType)) {
                    joinTeam("确定要加入此团队吗?",1);
                }
                break;
        }
    }
    public void joinTeam(String mTitle, final int status) {
        View updateStateView = View.inflate(this, R.layout.update_state_pop, null);
        TextView sureTv = updateStateView.findViewById(R.id.sure);
        TextView title = updateStateView.findViewById(R.id.title);
        TextView cancelTv = updateStateView.findViewById(R.id.cancel_tv);
        title.setText(mTitle);
        TextView textView[] = new TextView[]{sureTv, cancelTv};
        dialogCircle = MyApplication.getInstance().getPop(this, updateStateView, 1.1f, 3, Gravity.BOTTOM, R.style.BottomDialog_Animation, true);
        for (int i = 0; i < textView.length; i++) {
            final int finalI = i;
            textView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI ==0) {
                    if (status==0) {
                        commitParam.setCompanyId(companyId);
                        body = commitParam.getBody(sid, HttpUrl.JOINCOMPANYINTERFACE, commitParam);
                        presenter=new Presenter(JoinCompanyActivity.this);
                        presenter.getData("POST", "加入公司", HttpUrl.JOINCOMPANY_URL);
                    }
                    if (status==1) {
                        commitParam.setTeamId(teamId);
                        commitParam.setUserType(postType);
                        body = commitParam.getBody(sid, HttpUrl.JOINTEAMINTERFACE, commitParam);
                        presenter=new Presenter(JoinCompanyActivity.this);
                        presenter.getData("POST", "加入团队", HttpUrl.JOINTEAM_URL);
                    }
                    }
                    dialogCircle.dismiss();
                    dialogCircle=null;
                }
            });
        }
    }
        class OnTextChange extends TextChange {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (HttpUrl.POSITION[0].equals(postType)||HttpUrl.POSITION[1].equals(postType)) {
                if (!searchCompanyEd.getText().toString().trim().equals("")) {
                    commitParam.setCompanyName(searchCompanyEd.getText().toString().trim());
                    body = commitParam.getBody(sid, HttpUrl.QUERYCOMPANYINTERFACE, commitParam);
                    presenter=new Presenter(JoinCompanyActivity.this);
                    presenter.getData("POST", "搜索公司", HttpUrl.QUERYCOMPANY_URL);
                } else {
                    mData.clear();
                    joinTeam.setBackgroundResource(R.drawable.fillet_blueshallow);
                    mCompanyAdapter.setCurrentItem(-1);
                    mCompanyAdapter.clear();
                }
            }
            if (HttpUrl.POSITION[2].equals(postType)) {
                if (!searchCompanyEd.getText().toString().trim().equals("")) {
                    commitParam.setTeamName(searchCompanyEd.getText().toString().trim());
                    body = commitParam.getBody(sid, HttpUrl.QUERYTEAMINTERFACE, commitParam);
                    presenter=new Presenter(JoinCompanyActivity.this);
                    presenter.getData("POST", "搜索团队", HttpUrl.QUERYTEAM_URL);
                } else {
                    mData.clear();
                    joinTeam.setBackgroundResource(R.drawable.fillet_blueshallow);
                    mCompanyAdapter.setCurrentItem(-1);
                    mCompanyAdapter.clear();
                }
            }
        }
    }
}
