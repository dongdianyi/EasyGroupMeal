package gzkj.easygroupmeal.activity;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;

public class ForgetPwdActivity extends BaseActivity {

    @BindView(R.id.input_phone_ed)
    EditText inputPhoneEd;
    @BindView(R.id.input_pwd_ed)
    EditText inputPwdEd;
    @BindView(R.id.input_code_ed)
    EditText inputCodeEd;
    @BindView(R.id.get_code_tv)
    TextView getCodeTv;

    //设置验证码倒数秒数
    private int time = 60;
    private Handler handler = new Handler();

    //验证码倒数
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            handler.postDelayed(this, 1000);
            getCodeTv.setText(time + getString(R.string.get_codes));
            if (time == 0) {
                getCodeTv.setText(getString(R.string.again_get_code));
                handler.removeCallbacks(runnable);
                time = 60;
                getCodeTv.setEnabled(true);
                getCodeTv.setBackgroundResource(R.drawable.fillet_blue_more);
            } else {
                getCodeTv.setEnabled(false);
                getCodeTv.setBackgroundResource(R.drawable.fillet_blueshallow_more);
            }
        }
    };
    private CommitParam commitParam;

    @Override
    public int intiLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.back, R.id.sure, R.id.get_code_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                if (inputPhoneEd.length() != 11) {
                    toastShort("手机号输入错误");
                    return;
                }
                if (inputPwdEd.length()<6) {
                    toastShort("请输入大于等于6位数密码");
                    return;
                }
                if (inputCodeEd.length() == 0) {
                    toastShort("请输入验证码");
                    return;
                }
                commitParam = new CommitParam();
                commitParam.setMobile(inputPhoneEd.getText().toString().trim());
                commitParam.setPwd(inputPwdEd.getText().toString().trim());
                commitParam.setRandCode(inputCodeEd.getText().toString().trim());
                body = commitParam.getBody("", HttpUrl.FORGETPWDINTERFACE, commitParam);
                presenter = new Presenter(this);
                presenter.getData("POST", "更改密码", HttpUrl.FORGETPWD_URL);
                break;
            case R.id.get_code_tv:
                if (!inputPhoneEd.getText().toString().trim().equals("") && inputPhoneEd.length() == 11) {
                    handler.post(runnable);
                    commitParam = new CommitParam();
                    commitParam.setMobile(inputPhoneEd.getText().toString().trim());
                    body = commitParam.getBody("", HttpUrl.CODEINTERFACE, commitParam);
                    presenter = new Presenter(this);
                    presenter.getData("POST", "验证码", HttpUrl.CODE_URL);
                } else {
                    toastShort(getString(R.string.input_right_phone));
                }
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        Login login = new Gson().fromJson(object, Login.class);
        if ("验证码".equals(flag)) {
            toastShort(login.getResultDesc());
        }
        if ("更改密码".equals(flag)) {
            toastShort(login.getResultDesc());
            finish();
        }

    }
}
