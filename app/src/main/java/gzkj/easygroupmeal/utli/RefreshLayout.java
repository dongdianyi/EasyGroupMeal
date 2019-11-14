package gzkj.easygroupmeal.utli;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 重新拦截事件   解决与banner横向滑动冲突
 */
public class RefreshLayout extends SwipeRefreshLayout {

    float lastx = 0;
    float lasty = 0;
    boolean ismovepic = false;

    public RefreshLayout(@NonNull Context context) {
        super(context);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        if (ev.getAction() ==MotionEvent.ACTION_DOWN){
            lastx = ev.getX();
            lasty = ev.getY();
            ismovepic = false;
            return super.onInterceptTouchEvent(ev);
        }

        final int action = MotionEventCompat.getActionMasked(ev);
        Log.e("refresh",ev.getX() + "---" + ev.getY());

        int x2 = (int) Math.abs(ev.getX() - lastx);
        int y2 = (int) Math.abs(ev.getY() - lasty);

        //滑动图片最小距离检查
        Log.e("refresh","滑动差距 - >" + x2 + "--" + y2);
        if (x2>y2){
            if (x2>=100)ismovepic = true;
            return false;
        }

        //是否移动图片(下拉刷新不处理)
        if (ismovepic){
            Log.e("refresh","滑动差距 - >" + x2 + "--" + y2);
            return false;
        }

        boolean isok = super.onInterceptTouchEvent(ev);

        Log.e("refresh","isok ->" + isok);

        return isok;
    }
}
