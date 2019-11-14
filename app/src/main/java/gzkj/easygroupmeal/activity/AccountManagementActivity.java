package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.SingleClick;

public class AccountManagementActivity extends BaseActivity {

    @BindView(R.id.account_information_tv)
    TextView accountInformationTv;
    private View signOutView;
    private DialogCircle dialogCircle;
    private TextView sureTv;
    private TextView cancelTv;
    private String mobile;
    private String sid;


    @Override
    public int intiLayout() {
        return R.layout.activity_account_management;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(this, "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        mobile = mShared.getSharedPreference("mobile", "").toString();
        accountInformationTv.setText(mobile);

    }

    @Override
    public void setListener() {

    }

    @SingleClick
    @OnClick({R.id.back, R.id.sign_out, R.id.account_information_linear, R.id.change_password_linear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sign_out:
                signOutView = View.inflate(this, R.layout.sign_out_pop, null);
                sureTv = signOutView.findViewById(R.id.sure_tv);
                cancelTv = signOutView.findViewById(R.id.cancel_tv);
                dialogCircle = MyApplication.getInstance().getPop(this, signOutView, 1.3f, 4, Gravity.CENTER, 0, true);
                sureTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signOut();
                    }
                });
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogCircle != null) {
                            dialogCircle.dismiss();
                            dialogCircle = null;
                        }
                    }
                });
                break;
            case R.id.account_information_linear:
                startActivity(new Intent(this, PersonalDataActivity.class));
                break;
            case R.id.change_password_linear:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
        }
    }

    private void signOut() {
        CommitParam commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.SIGNOUTINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "退出登录", HttpUrl.SIGNOUT_URL);
    }


    @Override
    public void toActivityData(String flag, String object) {
        if ("退出登录".equals(flag)) {
            if (dialogCircle != null) {
                mShared.clear();
                dialogCircle.dismiss();
                dialogCircle = null;
            }
            JPushInterface.clearAllNotifications(MyApplication.getContextObject());
            JPushInterface.setAlias(MyApplication.getContextObject(), 0, null);
            startActivity(new Intent(AccountManagementActivity.this, LoginRegisterActivity.class));
            finish();
        }
    }

}
