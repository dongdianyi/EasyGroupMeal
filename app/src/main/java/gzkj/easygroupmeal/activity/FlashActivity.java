package gzkj.easygroupmeal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.utli.DensityUtil;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;

public class FlashActivity extends BaseActivity {
    @BindView(R.id.vp)
    ViewPager viewPager;
    int currentItem;
    List<Integer> imageIDList;
    List<ImageView> imageViews;
    @Override
    public int intiLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        return R.layout.activity_flash;
    }

    @Override
    public void initData() {
        mShared = new SharedPreferencesHelper(this, "userinfo");
        boolean isFirst = (boolean) mShared.getSharedPreference("isFirst", true);
        if (isFirst) {
            imageIDList = new ArrayList();
            imageIDList.add(R.mipmap.vp1);
            imageIDList.add(R.mipmap.vp2);
            imageIDList.add(R.mipmap.vp3);
            imageViews = new ArrayList<>();
            for (int i = 0; i < imageIDList.size(); i++) {
                imageViews.add(new ImageView(this));
            }
            viewPager.setAdapter(new GuideAdapter());
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentItem = position;
                    Log.i("Guide","监听改变"+position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                float startX;
                float startY;
                float endX;
                float endY;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            startX=event.getX();
                            startY=event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            endX=event.getX();
                            endY=event.getY();
                            WindowManager windowManager= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                            //获取屏幕的宽度
                            Point size = new Point();
                            windowManager.getDefaultDisplay().getSize(size);
                            int width=size.x;
                            //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                            if(currentItem==(imageViews.size()-1)&&startX-endX>0&&startX-endX>=(width/4)){
                                Intent intent = new Intent(FlashActivity.this, LoginRegisterActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                            }
                            break;
                    }
                    return false;
                }
            });
        } else {
            viewPager.setVisibility(View.GONE);
            new CountDownTimer(3 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //倒计时的过程中回调该函数
                }

                @Override
                public void onFinish() {
                    //倒计时结束时回调该函数
                    if (!"".equals(mShared.getSharedPreference("sid", "")) && !"".equals(mShared.getSharedPreference("posttype", "")) && "1".equals(mShared.getSharedPreference("status", ""))) {
                        startActivity(new Intent(FlashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Intent intent = new Intent(FlashActivity.this, LoginRegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }.start();
        }
    }

    @Override
    public void setListener() {

    }
    /**
     * Viewpager适配器
     */
    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * 判断当前分页是不是view
         * 由于ViewPager里面的分页可以填入Fragment
         *
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 清理内存
         * 从第一页滑动到第二页，此时第一页的内存应该释放
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));//释放滑动过后的前一页
        }

        /**
         * 得到---->暂时是没有用的
         *
         * @param object
         * @return
         */
        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        /**
         * 初始化分页
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            imageView.setImageResource(imageIDList.get(position));
            ViewGroup.LayoutParams viewLayoutParams = new ViewGroup.LayoutParams
                    (
                            DensityUtil.dip2px(FlashActivity.this, 170),
                            DensityUtil.dip2px(FlashActivity.this, 200)
                    );
            container.addView(imageView,viewLayoutParams);//设置图片的宽高

            return imageView;
        }
    }
}
