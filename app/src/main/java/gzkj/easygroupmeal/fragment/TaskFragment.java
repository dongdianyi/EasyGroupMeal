package gzkj.easygroupmeal.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.google.gson.Gson;
import com.stay4it.banner.Banner;
import com.stay4it.banner.Transformer;
import com.stay4it.banner.listener.OnBannerListener;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.util.RFABShape;
import com.wangjie.rapidfloatingactionbutton.util.RFABTextUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.activity.GeneralTaskActivity;
import gzkj.easygroupmeal.activity.MainActivity;
import gzkj.easygroupmeal.activity.TaskMessageActivity;
import gzkj.easygroupmeal.activity.UrgentTaskActivity;
import gzkj.easygroupmeal.activity.WebActivity;
import gzkj.easygroupmeal.adapter.LabVpAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseFragment;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.bean.Notice;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.CompareUtil;
import gzkj.easygroupmeal.utli.DensityUtil;
import gzkj.easygroupmeal.utli.GlideLoadUtils;
import gzkj.easygroupmeal.utli.MarqueeText;
import gzkj.easygroupmeal.utli.RefreshLayout;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.StatusBarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends BaseFragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.task_message_red)
    TextView taskMessageRed;
    @BindView(R.id.tv_text)
    MarqueeText tvText;
    @BindView(R.id.message_iv)
    ImageView messageIv;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.notice_linear)
    LinearLayout noticeLinear;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsing;
    @BindView(R.id.task_tab)
    XTabLayout taskTab;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.task_vp)
    ViewPager taskVp;
    @BindView(R.id.activity_main)
    CoordinatorLayout activityMain;
    @BindView(R.id.label_list_sample_rfal)
    RapidFloatingActionLayout rfaLayout;
    @BindView(R.id.label_list_sample_rfab)
    RapidFloatingActionButton rfaButton;
    private LabVpAdapter mLabVpAdapter;
    private List<String> mTitles;
    private RapidFloatingActionHelper rfabHelper;
    private String sid, postType;
    private CommitParam commitParam;
    private MainActivity mainActivity;
    private TextView mainTaskMessageRed;

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_task;
    }

    @Override
    protected void initView() {
        StatusBarUtils.with(getActivity())
                .init();
        mainActivity = (MainActivity) getActivity();
        mainTaskMessageRed = mainActivity.findViewById(R.id.task_message_red);

        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        postType = mShared.getSharedPreference("posttype", "").toString();

        //设置刷新时动画的颜色，可以设置4个
        if (refreshLayout != null) {
            refreshLayout.setProgressViewOffset(false, 0, DensityUtil.dip2px(getActivity(), 48));
            refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            refreshLayout.setOnRefreshListener(this);
        }

        mTitles = new ArrayList<>();
        if (HttpUrl.POSITION[0].equals(postType) || HttpUrl.POSITION[1].equals(postType)) {
            mTitles.add("个人任务");
            mTitles.add("员工任务");
            mLabVpAdapter = new LabVpAdapter(getChildFragmentManager(), mTitles, "task");
            taskVp.setAdapter(mLabVpAdapter);
            taskTab.setupWithViewPager(taskVp);
            taskTab.getTabAt(0).setCustomView(getTabView(0));
            taskTab.getTabAt(1).setCustomView(getTabView(1));
            addFloatingActionButton();
        }
        if (HttpUrl.POSITION[2].equals(postType)) {
            mTitles.add("个人任务");
            mLabVpAdapter = new LabVpAdapter(getChildFragmentManager(), mTitles, "task");
            taskVp.setAdapter(mLabVpAdapter);
            taskTab.setupWithViewPager(taskVp);
            taskTab.getTabAt(0).setCustomView(getTabView(0));
            rfaButton.setVisibility(View.GONE);
        }
        if (HttpUrl.POSITION[3].equals(postType)) {
            mTitles.add("巡检任务");
            mLabVpAdapter = new LabVpAdapter(getChildFragmentManager(), mTitles, "task");
            taskVp.setAdapter(mLabVpAdapter);
            taskTab.setupWithViewPager(taskVp);
            taskTab.getTabAt(0).setCustomView(getTabView(0));
            rfaButton.setVisibility(View.GONE);
            noticeLinear.setVisibility(View.GONE);
            messageIv.setVisibility(View.GONE);

        }
    }

    private void addFloatingActionButton() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("派发任务")
//                .setIconNormalColor(Color.rgb(56, 125, 254))
//                .setIconPressedColor(Color.rgb(56, 125, 254))
                        .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(Color.rgb(56, 125, 254), RFABTextUtil.dip2px(getContext(), 4)))
                        .setLabelColor(Color.WHITE)
                        .setLabelSizeSp(14)
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("紧急任务")
//                .setIconNormalColor(Color.rgb(247, 5, 5))
//                .setIconPressedColor(Color.rgb(247, 5, 5))
                        .setLabelColor(Color.WHITE)
                        .setLabelSizeSp(14)
                        .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(Color.rgb(247, 5, 5), RFABTextUtil.dip2px(getContext(), 4)))
                        .setWrapper(1)
        );
        rfaContent
                .setItems(items)
//                .setIconShadowRadius(RFABTextUtil.dip2px(getContext(), 5))
//                .setIconShadowColor(Color.rgb(247, 5, 5))
//                .setIconShadowDy(RFABTextUtil.dip2px(getContext(), 5))
        ;

        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
    }

    @Override
    protected void initData() {
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.QUERYNOTICEINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "查询公告", HttpUrl.QUERYNOTICE_URL);

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMessage();
    }

    @Override
    protected void setListener() {
        taskTab.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                changeTabSelect(tab);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {
                changeTabNormal(tab);
            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    refreshLayout.setEnabled(true);
                } else {
                    refreshLayout.setEnabled(false);
                }
            }
        });
    }

    private void changeTabSelect(XTabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView txt_xian = view.findViewById(R.id.txt_xian);
        TextView txt_title = view.findViewById(R.id.txt_title);
        if (txt_title.getText().toString().equals("个人任务") || txt_title.getText().toString().equals("巡检任务")) {
            taskVp.setCurrentItem(0);
        }
        if (txt_title.getText().toString().equals("员工任务")) {
            taskVp.setCurrentItem(1);
        }
        txt_xian.setVisibility(View.VISIBLE);
        txt_title.setAlpha(1f);
    }

    private void changeTabNormal(XTabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView txt_xian = view.findViewById(R.id.txt_xian);
        TextView txt_title = view.findViewById(R.id.txt_title);
        txt_xian.setVisibility(View.GONE);
        txt_title.setAlpha(0.8f);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tab_top, null);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_xian = view.findViewById(R.id.txt_xian);
        txt_title.setText(mTitles.get(position));
        if (position == 0) {
            txt_title.setAlpha(1f);
        }
        if (position == 1) {
            txt_xian.setVisibility(View.GONE);
            txt_title.setAlpha(0.8f);
        }
        return view;
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        if (position == 0) {
            startActivity(new Intent(getActivity(), GeneralTaskActivity.class));
        }
        if (position == 1) {
            startActivity(new Intent(getActivity(), UrgentTaskActivity.class));
        }
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
//        Toast.makeText(getContext(), "clicked icon: " + position, Toast.LENGTH_SHORT).show();
        rfabHelper.toggleContent();
    }

    @OnClick(R.id.message_iv)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), TaskMessageActivity.class));
    }

    /**
     * 初始化
     */
    private void initBanner(final List<String> listUrl, final List<String> listDetails) {
        banner.setDelayTime(2000)
                .setBannerAnimation(Transformer.Default)
                .setImages(listUrl)
                .setImageLoader(new com.stay4it.banner.loader.ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        GlideLoadUtils.getInstance().displayImage(context, path, imageView);
                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (listDetails.get(position) != null && listDetails.get(position).length() > 0) {
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("url", listDetails.get(position));
                    intent.putExtra("title", "详情");
                    startActivity(intent);
                }
            }
        }).start();
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("查询公告".equals(flag)) {
            Notice notice = new Gson().fromJson(object, Notice.class);
            Collections.sort(notice.getResultObj().getNotices(), CompareUtil.createComparator(-1, "createtime"));
            if (notice.getResultObj().getNotices().size() > 0) {
                tvText.setText(notice.getResultObj().getNotices().get(0).getContent());
            }
            List<String> listUrl = new ArrayList<>();
            List<String> listDetails = new ArrayList<>();
            for (Notice.ResultObjBean.BannersBean bannersBean : notice.getResultObj().getBanners()) {
                listUrl.add(bannersBean.getUrl());
                listDetails.add(bannersBean.getDetails());
            }
            initBanner(listUrl, listDetails);
            refreshLayout.setRefreshing(false);
        }
        if ("消息列表红点".equals(flag)) {
                Login login = new Gson().fromJson(object, Login.class);
                if (login.getResultObj().getMessageCount() < 100 && login.getResultObj().getMessageCount() > 0) {
                    taskMessageRed.setVisibility(View.VISIBLE);
                    mainTaskMessageRed.setVisibility(View.VISIBLE);
                    taskMessageRed.setText(String.valueOf(login.getResultObj().getMessageCount()));
                    mainTaskMessageRed.setText(String.valueOf(login.getResultObj().getMessageCount()));
                } else if (login.getResultObj().getMessageCount() >= 100) {
                    taskMessageRed.setVisibility(View.VISIBLE);
                    mainTaskMessageRed.setVisibility(View.VISIBLE);
                    taskMessageRed.setText("99+");
                    mainTaskMessageRed.setText("99+");
                } else {
                    taskMessageRed.setVisibility(View.GONE);
                    mainTaskMessageRed.setVisibility(View.GONE);
                }
        }
    }

    @Override
    public void fail(String flag, Throwable t) {
        super.fail(flag, t);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        refreshMessage();
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.QUERYNOTICEINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "查询公告", HttpUrl.QUERYNOTICE_URL);
    }
    public void refreshMessage() {
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.APPLYREDDOTINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "消息列表红点", HttpUrl.APPLYREDDOT_URL);
    }
}
