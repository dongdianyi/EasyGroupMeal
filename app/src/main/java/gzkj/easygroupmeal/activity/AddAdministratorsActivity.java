package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.bean.MemberJson;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.SingleClick;

public class AddAdministratorsActivity extends BaseActivity {

    @BindView(R.id.name_ed)
    EditText nameEd;
    @BindView(R.id.position_ed)
    EditText positionEd;
    @BindView(R.id.phone_ed)
    EditText phoneEd;
    @BindView(R.id.company_tv)
    TextView companyTv;
    @BindView(R.id.company_ed)
    EditText companyEd;
    @BindView(R.id.company_line)
    TextView companyLine;
    @BindView(R.id.title)
    TextView title;
    private String postType;
    private String flag;
    private CommitParam commitParam;
    private String sid;
    private String type;

    @Override
    public int intiLayout() {
        return R.layout.activity_add_administrators;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        flag = getIntent().getStringExtra("flag");
        postType = getIntent().getStringExtra("posttype");
        EventBus.getDefault().register(this);
        if (getIntent().getStringExtra("type") == null) {
        } else {
            type = getIntent().getStringExtra("type");
        }
        title.setText(flag);
        if (getString(R.string.person_in_charge).equals(flag)) {

        } else {
            positionEd.setFocusable(false);
            positionEd.setFocusableInTouchMode(false);
            companyTv.setVisibility(View.GONE);
            companyEd.setVisibility(View.GONE);
            companyLine.setVisibility(View.GONE);
            if (HttpUrl.POSITION[0].equals(postType)) {
                positionEd.setText("餐厅经理");
            } else {
                positionEd.setText("班组长");
            }
        }

    }

    @Override
    public void setListener() {

    }
    @SingleClick
    @OnClick({R.id.back, R.id.company_ed,R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.company_ed:
                startActivity(new Intent(this, SchoolActivity.class));
                break;
            case R.id.sure:
                if (nameEd.length() == 0) {
                    toastShort("请输入姓名");
                    return;
                }
                if (positionEd.length() == 0) {
                    toastShort("请输入职务");
                    return;
                }
                if (phoneEd.length() != 11) {
                    toastShort("请输入正确手机号码");
                    return;
                }
                if (getString(R.string.person_in_charge).equals(flag)) {
                    if (companyEd.length() == 0) {
                        toastShort("请输入单位");
                        return;
                    }
                }
                if ("team".equals(type)) {
                    if (getString(R.string.person_in_charge).equals(flag)) {
                        commitParam = new CommitParam();
                        commitParam.setJob(positionEd.getText().toString().trim());
                        commitParam.setSchoolZone(companyEd.getText().toString().trim());
                        commitParam.setUserName(nameEd.getText().toString().trim());
                        commitParam.setMobile(phoneEd.getText().toString().trim());
                        body = commitParam.getBody(sid, HttpUrl.ADDCHARGEINTERFACE, commitParam);
                        presenter=new Presenter(this);
                        presenter.getData("POST", "团队添加甲方", HttpUrl.ADDCHARGE_URL);
                    }
                    if (getString(R.string.add_member).equals(flag)) {
                        commitParam = new CommitParam();
                        commitParam.setUserName(nameEd.getText().toString().trim());
                        commitParam.setMobile(phoneEd.getText().toString().trim());
                        commitParam.setPostType(HttpUrl.POSITION[2]);
                        body = commitParam.getBody(sid, HttpUrl.ADDSTAFFINTERFACE, commitParam);
                        presenter=new Presenter(this);
                        presenter.getData("POST", "团队添加员工", HttpUrl.ADDSTAFF_URL);
                    }
                }

                if ("company".equals(type)) {
                    commitParam = new CommitParam();
                    commitParam.setUserName(nameEd.getText().toString().trim());
                    commitParam.setMobile(phoneEd.getText().toString().trim());
                    commitParam.setPostType(HttpUrl.POSITION[1]);
                    body = commitParam.getBody(sid, HttpUrl.ADDSTAFF2INTERFACE, commitParam);
                    presenter=new Presenter(this);
                    presenter.getData("POST", "公司添加员工", HttpUrl.ADDSTAFF2_URL);
                }
                if (type == null) {
                    EventBus.getDefault().post(new MemberJson(flag, positionEd.getText().toString().trim(), nameEd.getText().toString().trim(), phoneEd.getText().toString().trim(), companyEd.getText().toString().trim(), positionEd.getText().toString().trim()));
                    finish();
                }
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        Login login = new Gson().fromJson(object, Login.class);
        toastShort(login.getResultDesc());
        if (type != null && !"团队添加甲方".equals(flag)) {
            Intent intent = new Intent();
            setResult(1001, intent);
            finish();
        }
        if ("团队添加甲方".equals(flag)) {
            finish();
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(String string) {
        companyEd.setText(string);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
