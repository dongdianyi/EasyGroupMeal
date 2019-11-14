package gzkj.easygroupmeal.utli;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 有圓角的自定义dialog
 **/
public class DialogCircle extends Dialog {
    private static int default_width = 160; // 默认宽度
    private static int default_height = 120;// 默认高度
    public DialogCircle(Context context) {
        super(context);
    }

    public DialogCircle(Context context, int width, int height, View layout, int style,int gravity) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        window.setAttributes(params);
    }
}
