package gzkj.easygroupmeal.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import gzkj.easygroupmeal.activity.LoginRegisterActivity;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.NetUtil;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.StatusBarUtils;
import gzkj.easygroupmeal.view.IView;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class BaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, IView, ObservableOnSubscribe<View> {
    public static RequestBody body;
    public static ObservableEmitter emitter;
    //控制器发出请求
    public static Presenter presenter;
    public static SharedPreferencesHelper mShared;
    /**
     * 网络类型
     */
    public static int netMobile;
    /***封装toast对象**/
    private Toast toast;
    /***获取TAG的activity名称**/
    protected final String TAG = this.getClass().getSimpleName();
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        //设置布局
        if (intiLayout() != 0) {
            setContentView(intiLayout());
            ButterKnife.bind(this);
            MyApplication.getInstance().addActivity(this);
        }
        //设置数据
        initData();
        //监听器
        setListener();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    public void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 沉浸式导航栏
     */
    public void hideTopUIMenu() {
        StatusBarUtils.with(this)
                .init();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int intiLayout();

    /**
     * 设置数据
     */
    public abstract void initData();

    /**
     * 设置数据
     */
    public abstract void setListener();

    /**
     * 显示长toast
     *
     * @param msg
     */
    @SuppressLint("ShowToast")
    public void toastLong(String msg) {
        if (null == toast) {
            toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    /**
     * 显示短toast
     *
     * @param msg
     */
    @SuppressLint("ShowToast")
    public void toastShort(String msg) {
        if (null == toast) {
            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    /**
     * 显示Log
     *
     * @param strMsg
     */
    public void logger(String strMsg) {
        Log.e(TAG, strMsg);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        netMobile = NetUtil.getNetWorkState(BaseActivity.this);
        return isNetConnect();
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public static boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;

        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    @Override
    public void subscribe(ObservableEmitter<View> e) {
        emitter = e;
    }

    @Override
    public List<MultipartBody.Part> dataFile() {
        return null;
    }

    @Override
    public RequestBody data() {
        return body;
    }

    @Override
    public void fail(String flag, Throwable t) {
        if (t.getMessage()!=null&&!"".equals(t.getMessage())) {
            toastShort(t.getMessage());
        }
    }

    @Override
    public void toActivityData(String flag, String object) {

    }

    @Override
    public void invalid(String t) {
        if (!MyApplication.getInstance().isFlag()) {
            Intent intent = new Intent(this, LoginRegisterActivity.class);
            intent.putExtra("invalid", t);
            startActivity(intent);
            MyApplication.getInstance().setFlag(true);
        }
    }

    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (checkDoubleClick(intent)) {
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    /**
     * 检查是否重复跳转，不需要则重写方法并返回true
     */
    private String mActivityJumpTag;        //activity跳转tag
    private long mClickTime;                //activity跳转时间

    protected boolean checkDoubleClick(Intent intent) {

        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return true;
        }
//        if (tag.equals(mActivityJumpTag)) {
//            // 检查不通过
//            result = false;
//        }
        if (tag.equals(mActivityJumpTag) && mClickTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mClickTime = SystemClock.uptimeMillis();
        return result;
    }
}