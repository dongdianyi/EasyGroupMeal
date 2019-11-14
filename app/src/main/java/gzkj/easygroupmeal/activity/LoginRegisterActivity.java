package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.AndroidBug5497Workaround;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.SingleClick;
import gzkj.easygroupmeal.utli.TextChange;
import gzkj.easygroupmeal.utli.VerificationCodeView;
import io.reactivex.functions.Consumer;

public class LoginRegisterActivity extends BaseActivity {


    @BindView(R.id.pwd_login_tv)
    TextView pwdLoginTv;
    @BindView(R.id.forget_pwd_tv)
    TextView forgetPwdTv;
    @BindView(R.id.input_phone_ed)
    EditText inputPhoneEd;
    @BindView(R.id.input_pwd_ed)
    EditText inputPwdEd;
    @BindView(R.id.input_code_ed)
    EditText inputCodeEd;
    @BindView(R.id.input_verification_ed)
    EditText inputVerification;
    @BindView(R.id.verifycodeview)
    VerificationCodeView verifycodeview;
    @BindView(R.id.get_code_tv)
    TextView getCodeTv;
    @BindView(R.id.code_linear)
    LinearLayout codeLinear;
    @BindView(R.id.verification_linear)
    LinearLayout verificationLinear;
    @BindView(R.id.register_tv)
    TextView registerTv;
    @BindView(R.id.agreement_tv)
    TextView agreementTv;
    @BindView(R.id.customer_linear)
    LinearLayout customerLinear;
    //设置验证码倒数秒数
    private int time = 60;
    private Handler handler = new Handler();
    //记录是登录还是注册0是登录1是注册2是忘记密码
    private int state = 0;

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
    private String sid;
    private String postType;
    private String status;
    private String invalid;


    @Override
    public int intiLayout() {
        return R.layout.activity_login_register;
    }

    @Override
    public void initData() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(needPermissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.e(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.e(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.e(TAG, permission.name + " is denied.");
                        }
                    }
                });
        //隐藏状态栏
        AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content));
        hideTopUIMenu();
        MyApplication.getInstance().exit(this);
        mShared = new SharedPreferencesHelper(this, "userinfo");
        if (getIntent().getStringExtra("invalid")!=null) {
            invalid = getIntent().getStringExtra("invalid");
            mShared.clear();
            toastShort(invalid);
        }
        mShared.put("isFirst", false);
        inputPhoneEd.setText(mShared.getSharedPreference("mobile", "").toString());
        inputPwdEd.setText(mShared.getSharedPreference("pwd", "").toString());
        if (inputPhoneEd.length() > 0 && inputPwdEd.length() > 0) {
            registerTv.setBackgroundResource(R.drawable.fillet_blue);
            registerTv.setEnabled(true);
        } else {
            registerTv.setBackgroundResource(R.drawable.fillet_blueshallow);
            registerTv.setEnabled(false);
        }

    }

    @Override
    public void setListener() {
        //监听所有的edittext
        TextChange textChange = new OnTextChange();
        inputPwdEd.addTextChangedListener(textChange);
        inputPhoneEd.addTextChangedListener(textChange);
        inputCodeEd.addTextChangedListener(textChange);
        inputVerification.addTextChangedListener(textChange);
    }

    @SingleClick
    @OnClick({R.id.verifycodeview, R.id.forget_pwd_tv, R.id.pwd_login_tv, R.id.get_code_tv, R.id.register_tv, R.id.agreement_tv, R.id.customer_linear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.verifycodeview:
                inputVerification.setText("");
                verifycodeview.refreshCode();
                break;
            case R.id.forget_pwd_tv:
                state = 2;
                pwdLoginTv.setText(getString(R.string.pwd_login));
                codeLinear.setVisibility(View.VISIBLE);
                verificationLinear.setVisibility(View.GONE);
                forgetPwdTv.setVisibility(View.GONE);
                registerTv.setText(getString(R.string.sure));
                inputPwdEd.setHint(R.string.input_new_pwd);
                if (inputPhoneEd.length() > 0 && inputPwdEd.length() > 0 && inputCodeEd.length() > 0) {
                    registerTv.setBackgroundResource(R.drawable.fillet_blue);
                    registerTv.setEnabled(true);
                } else {
                    registerTv.setBackgroundResource(R.drawable.fillet_blueshallow);
                    registerTv.setEnabled(false);
                }
                break;
            case R.id.pwd_login_tv:
                inputPwdEd.setHint(R.string.input_pwd);
                if (state == 0) {
                    //登录点击注册
                    state = 1;
                    verifycodeview.refreshCode();
                    pwdLoginTv.setText(getString(R.string.pwd_login));
                    codeLinear.setVisibility(View.VISIBLE);
                    verificationLinear.setVisibility(View.VISIBLE);
                    forgetPwdTv.setVisibility(View.GONE);
                    inputPwdEd.setHint(R.string.input_pwd);
                    registerTv.setText(getString(R.string.register));
                    if (inputPhoneEd.length() > 0 && inputPwdEd.length() > 0 && inputCodeEd.length() > 0 && inputVerification.length() > 0) {
                        registerTv.setBackgroundResource(R.drawable.fillet_blue);
                        registerTv.setEnabled(true);
                    } else {
                        registerTv.setBackgroundResource(R.drawable.fillet_blueshallow);
                        registerTv.setEnabled(false);
                    }
                } else {
                    //注册点击登录
                    state = 0;
                    pwdLoginTv.setText(getString(R.string.phone_register));
                    codeLinear.setVisibility(View.GONE);
                    verificationLinear.setVisibility(View.GONE);
                    forgetPwdTv.setVisibility(View.VISIBLE);
                    registerTv.setText(getString(R.string.login));

                    if (inputPhoneEd.length() > 0 && inputPwdEd.length() > 0) {
                        registerTv.setBackgroundResource(R.drawable.fillet_blue);
                        registerTv.setEnabled(true);
                    } else {
                        registerTv.setBackgroundResource(R.drawable.fillet_blueshallow);
                        registerTv.setEnabled(false);
                    }
                }
                handler.removeCallbacks(runnable);
                time = 60;
                getCodeTv.setEnabled(true);
                getCodeTv.setText(getString(R.string.get_code));
                getCodeTv.setBackgroundResource(R.drawable.fillet_blue_more);
                break;
            case R.id.get_code_tv:
                if (!inputPhoneEd.getText().toString().trim().equals("") && inputPhoneEd.length() == 11) {
                    handler.post(runnable);
                    commitParam = new CommitParam();
                    commitParam.setMobile(inputPhoneEd.getText().toString().trim());
                    body = commitParam.getBody("", HttpUrl.CODEINTERFACE, commitParam);
                    presenter = new Presenter(LoginRegisterActivity.this);
                    presenter.getData("POST", "验证码", HttpUrl.CODE_URL);
                } else {
                    toastShort(getString(R.string.input_right_phone));
                }
                break;
            case R.id.register_tv:
                if (inputPwdEd.length() < 6) {
                    toastShort("请输入大于等于6位数密码");
                    return;
                }
                commitParam = new CommitParam();
                commitParam.setMobile(inputPhoneEd.getText().toString().trim());
                commitParam.setPwd(inputPwdEd.getText().toString().trim());
                commitParam.setRandCode(inputCodeEd.getText().toString().trim());
                if (state == 0) {
                    body = commitParam.getBody("", HttpUrl.LOGININTERFACE, commitParam);
                    presenter = new Presenter(LoginRegisterActivity.this);
                    presenter.getData("POST", "登录", HttpUrl.LOGIN_URL);
                } else if (state == 1) {
                    String input = inputVerification.getText().toString().trim().toLowerCase();
                    String code = verifycodeview.getvCode().toLowerCase();
                    if (!TextUtils.isEmpty(input) && input.equals(code)) {
                        body = commitParam.getBody("", HttpUrl.REGISTERINTERFACE, commitParam);
                        presenter = new Presenter(LoginRegisterActivity.this);
                        presenter.getData("POST", "注册", HttpUrl.REGISTER_URL);
                    } else {
                        toastShort("图形验证码不正确");
                        return;
                    }
                } else if (state == 2) {
                    body = commitParam.getBody("", HttpUrl.FORGETPWDINTERFACE, commitParam);
                    presenter = new Presenter(LoginRegisterActivity.this);
                    presenter.getData("POST", "忘记密码", HttpUrl.FORGETPWD_URL);
                }
                break;
            case R.id.agreement_tv:
                Intent intent = new Intent(LoginRegisterActivity.this, WebActivity.class);
                intent.putExtra("url", HttpUrl.AGREEMENT_URL);
                intent.putExtra("title", "用户协议");
                startActivity(intent);
                break;
            case R.id.customer_linear:
                callPhone(HttpUrl.PHONENUM);
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        Login login = new Gson().fromJson(object, Login.class);
        if (flag.equals("验证码")) {
            toastShort(login.getResultDesc());
        } else if ("忘记密码".equals(flag)) {
            toastShort(login.getResultDesc());
            state = 0;
            pwdLoginTv.setText(getString(R.string.phone_register));
            codeLinear.setVisibility(View.GONE);
            verificationLinear.setVisibility(View.GONE);
            forgetPwdTv.setVisibility(View.VISIBLE);
            registerTv.setText(getString(R.string.login));

            if (inputPhoneEd.length() > 0 && inputPwdEd.length() > 0) {
                registerTv.setBackgroundResource(R.drawable.fillet_blue);
                registerTv.setEnabled(true);
            } else {
                registerTv.setBackgroundResource(R.drawable.fillet_blueshallow);
                registerTv.setEnabled(false);
            }
            handler.removeCallbacks(runnable);
            time = 60;
            getCodeTv.setEnabled(true);
            getCodeTv.setText(getString(R.string.get_code));
            getCodeTv.setBackgroundResource(R.drawable.fillet_blue_more);

        } else {
            MyApplication.getInstance().setFlag(false);
            sid = login.getResultObj().getSid();
            postType = login.getResultObj().getPost_type();
            status = login.getResultObj().getStatus();
            toastShort(login.getResultDesc());
            if (sid != null) {
                JPushInterface.setAlias(MyApplication.getContextObject(), 0, login.getResultObj().getUserId());
                mShared.put("sid", sid);
                mShared.put("mobile", inputPhoneEd.getText().toString().trim());
                mShared.put("pwd", inputPwdEd.getText().toString().trim());
                mShared.put("name", login.getResultObj().getUserName());
            }
            if (postType != null&&!"".equals(postType)) {
                mShared.put("posttype", postType);
                mShared.put("status", status);
                if ("0".equals(status)) {
                    if (HttpUrl.POSITION[3].equals(postType)) {
                        Intent intent = new Intent(this, AuthenticationActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, StartActivity.class);
                        if (login.getResultObj().getSid() != null) {
                            intent.putExtra("posttype", postType);
                            startActivity(intent);
                        }
                    }
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                }
            } else {
                Intent intent = new Intent(this, PersonalInformationActivity.class);
                if (login.getResultObj().getSid() != null) {
                    intent.putExtra("sid", sid);
                    startActivity(intent);
                }
            }
        }
    }

    class OnTextChange extends TextChange {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (state == 0) {
                if (inputPhoneEd.length() > 0 && inputPwdEd.length() > 0) {
                    registerTv.setBackgroundResource(R.drawable.fillet_blue);
                    registerTv.setEnabled(true);
                } else {
                    registerTv.setBackgroundResource(R.drawable.fillet_blueshallow);
                    registerTv.setEnabled(false);
                }
            } else if (state == 1) {
                if (inputPhoneEd.length() > 0 && inputPwdEd.length() > 0 && inputCodeEd.length() > 0 && inputVerification.length() > 0) {
                    registerTv.setBackgroundResource(R.drawable.fillet_blue);
                    registerTv.setEnabled(true);
                } else {
                    registerTv.setBackgroundResource(R.drawable.fillet_blueshallow);
                    registerTv.setEnabled(false);
                }
            } else {
                if (inputPhoneEd.length() > 0 && inputPwdEd.length() > 0 && inputCodeEd.length() > 0) {
                    registerTv.setBackgroundResource(R.drawable.fillet_blue);
                    registerTv.setEnabled(true);
                } else {
                    registerTv.setBackgroundResource(R.drawable.fillet_blueshallow);
                    registerTv.setEnabled(false);
                }
            }
        }
    }
}
