package gzkj.easygroupmeal.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.tencent.bugly.Bugly;

import java.util.ArrayList;
import java.util.List;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.receiver.InitIntentService;
import gzkj.easygroupmeal.utli.DensityUtil;
import gzkj.easygroupmeal.utli.DialogCircle;


/**
 * Created by Administrator on 2017/6/1.
 */

public class MyApplication extends Application {
    public static MyApplication instance;
    public static Context applicationContext;
    private List<Activity> mList = new ArrayList<>();
    private  boolean flag=false;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
//        //注意要设置在bugly init之前自定义ui
//        Beta.upgradeDialogLayoutId = R.layout.update_pop;
//        //bugly更新升级
        Bugly.init(getApplicationContext(), "9e1c65c462", false);
        InitIntentService.start(this);
    }


    public static MyApplication getInstance() {
        return instance;
    }

    //返回
    public static Context getContextObject() {
        return applicationContext;
    }

    //将启动的进程添加进入list中
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mList.remove(activity);
    }

    //将list中的activity全部销毁
    public void exit() {
        for (Activity activity : mList) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    public void exit(Activity act) {
        for (Activity activity : mList) {
            if (activity != act) {
                activity.finish();
            }
        }
    }

    //获取最后一个Activity
    public Activity getLastAct() {
        if (mList != null) {
            if (mList.size() > 0) {
                return mList.get(mList.size() - 1);
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //杀进程
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 弹窗可使用第三方 gradle直接集成 'com.dou361.dialogui:jjdxm-dialogui:1.0.3'
     *
     * @param context     上下文
     * @param view        视图布局
     * @param widthValue  宽度比例
     * @param heightValue 高度比例
     * @param gravity     弹窗显示位置
     * @param style       动画样式
     * @param isCancel    是否可以点击取消弹窗
     * @return
     */
    public DialogCircle getPop(Context context, View view, float widthValue, float heightValue, int gravity, int style, boolean isCancel) {
        WindowManager wm = (WindowManager) MyApplication.getContextObject().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int wscreenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        DialogCircle dialogCircle = new DialogCircle(context, DensityUtil.dip2px(context, wscreenWidth / widthValue), DensityUtil.dip2px(context, screenHeight / heightValue), view,
                R.style.dialog, gravity);
        Log.e("h_bl", "屏幕宽度（像素）：" + width);
        Log.e("h_bl", "屏幕高度（像素）：" + height);
        Log.e("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        Log.e("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.e("h_bl", "屏幕宽度（dp）：" + wscreenWidth);
        Log.e("h_bl", "屏幕高度（dp）：" + screenHeight);
        dialogCircle.setCancelable(isCancel);
        dialogCircle.getWindow().setWindowAnimations(style);
        dialogCircle.show();
        return dialogCircle;
    }

    /**
     * 返回app运行状态
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return int 1:前台 2:后台 0:不存在
     */
    public static int isAppAlive(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> listInfos = activityManager.getRunningTasks(20);
        // 判断程序是否在栈顶
        if (listInfos.get(0).topActivity.getPackageName().equals(packageName)) {
            return 1;
        } else {
            // 判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : listInfos) {
                if (info.topActivity.getPackageName().equals(packageName)) {
                    return 2;
                }
            }
            return 0;// 栈里找不到
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
