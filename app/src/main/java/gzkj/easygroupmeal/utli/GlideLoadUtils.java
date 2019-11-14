package gzkj.easygroupmeal.utli;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.stay4it.banner.loader.ImageLoader;

import gzkj.easygroupmeal.R;

/**
 * Created by ddy on 2018/4/27.
 * Glide加载图片
 */

public class GlideLoadUtils extends ImageLoader {
    private String TAG = "ImageLoader";

    /**
     * 借助内部类 实现线程安全的单例模式
     * 属于懒汉式单例，因为Java机制规定，内部类SingletonHolder只有在getInstance()
     * 方法第一次调用的时候才会被加载（实现了lazy），而且其加载过程是线程安全的。
     * 内部类加载的时候实例化一次instance。
     */

    public GlideLoadUtils() {
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }

    private static class GlideLoadUtilsHolder {
        private final static GlideLoadUtils INSTANCE = new GlideLoadUtils();
    }

    public static GlideLoadUtils getInstance() {
        return GlideLoadUtilsHolder.INSTANCE;
    }


    public static void loadRoundImg(Context ctx,String path, ImageView imageView) {
        Glide.with(ctx).load(path).transform(new GlideRoundTransform(ctx,DensityUtil.dip2px(ctx,10)))
                .priority(Priority.HIGH).into(imageView);
    }

    /**
     * 加载缩略图
     *
     * @param ctx
     * @param path
     * @param imageView
     */
    public static void loadImg(Context ctx, String path, ImageView imageView) {

        Glide.with(ctx).load(path).centerCrop()
                .placeholder(R.mipmap.photo)
                .error(R.mipmap.photo)
                .priority(Priority.HIGH).into(imageView);
    }

    public void glideLoad2(Activity context, String url, ImageView imageView, int default_image) {
        if (context != null) {
            Glide.with(context).load(url) .centerCrop()
                    .placeholder(default_image)
                    .error(default_image)
                    .fitCenter()
                    .priority(Priority.HIGH).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }
    /**
     * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
     *
     * @param context
     * @param url           加载图片的url地址  String
     * @param imageView     加载图片的ImageView 控件
     * @param default_image 图片展示错误的本地图片 id
     */
    public void glideLoad(Context context, String url, ImageView imageView, int default_image) {
        if (context != null) {
            Glide.with(context).load(url).centerCrop()
                    .fitCenter()
                    .priority(Priority.HIGH) .error(default_image).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoad(Activity activity, String url, ImageView imageView, int default_image) {
        if (!activity.isDestroyed()) {
            Glide.with(activity).load(url) .centerCrop()
                    .fitCenter()
                    .priority(Priority.HIGH) .error(default_image).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,activity is Destroyed");
        }
    }

    public void glideLoad(Fragment fragment, String url, ImageView imageView, int default_image) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(url) .centerCrop()
                    .placeholder(default_image)
                    .error(default_image)
                    .fitCenter()
                    .priority(Priority.HIGH).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,fragment is null");
        }
    }

    public void glideLoad(android.app.Fragment fragment, String url, ImageView imageView, int default_image) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(url) .centerCrop()
                    .placeholder(default_image)
                    .error(default_image)
                    .fitCenter()
                    .priority(Priority.HIGH).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,android.ChatRoomApp.Fragment is null");
        }
    }
}
