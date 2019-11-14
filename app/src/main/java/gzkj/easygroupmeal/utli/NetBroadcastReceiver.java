package gzkj.easygroupmeal.utli;

/**
 * Created by ddy on 2018/5/11.
 * 监测网络的广播
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import gzkj.easygroupmeal.activity.StartActivity;

public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetEvevt evevt = StartActivity.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            evevt.onNetChange(netWorkState);
        }
    }

    // 自定义接口
    public interface NetEvevt {
         void onNetChange(int netMobile);
    }
}