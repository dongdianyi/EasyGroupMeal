package gzkj.easygroupmeal.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.fragment.MineFragment;
import gzkj.easygroupmeal.fragment.ScheduleFragment;
import gzkj.easygroupmeal.fragment.TaskFragment;
import gzkj.easygroupmeal.fragment.TeamFragment;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.view.OnChangeViewFragment;


public class MainActivity extends BaseActivity {
    @BindView(R.id.schedule_linear)
    RelativeLayout scheduleLinear;
    @BindView(R.id.team_linear)
    RelativeLayout teamLinear;
    @BindView(R.id.task_message_red)
    TextView taskMessageRed;
    @BindView(R.id.team_apply_red)
    TextView teamApplyRed;
    @BindView(R.id.team_tv)
    TextView teamTv;
    @BindView(R.id.task_tv)
    TextView taskTv;
    @BindView(R.id.schedule_tv)
    TextView scheduleTv;
    @BindView(R.id.mine_tv)
    TextView mineTv;
    @BindView(R.id.schedule_iv)
    ImageView scheduleIv;
    @BindView(R.id.task_iv)
    ImageView taskIv;
    @BindView(R.id.team_iv)
    ImageView teamIv;
    @BindView(R.id.mine_iv)
    ImageView mineIv;
    private int current = 0;
    public int target = 0;
    public List<Fragment> mFragments;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    TextView[] tvArr;
    ImageView[] ivArr;
    int[] photoArrChecked, photoArrUnChecked;
    private String sid;
    private String postType;
    private TeamFragment teamFragment;
    private CommitParam commitParam;

    @Override
    public int intiLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        MyApplication.getInstance().exit(this);
        mShared = new SharedPreferencesHelper(this, "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        postType = mShared.getSharedPreference("posttype", "").toString();
        mFragments = new ArrayList<>();
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        teamFragment = new TeamFragment();
        MineFragment mineFragment = new MineFragment();
        mFragments.add(scheduleFragment);
        mFragments.add(new TaskFragment());
        mFragments.add(teamFragment);
        mFragments.add(mineFragment);
        scheduleFragment.setOnChangeListener(new OnChangeViewFragment() {
            @Override
            public void onChange(Login login) {
                teamFragment.setChangeView(login);
                mineFragment.setChangeView(login);
            }
        });
        mineFragment.setOnChangeListener(new OnChangeViewFragment() {
            @Override
            public void onChange(Login login) {
                scheduleFragment.setChangeView(login);
                teamFragment.setChangeView(login);
            }
        });
        tvArr = new TextView[]{scheduleTv, taskTv, teamTv, mineTv};
        ivArr = new ImageView[]{scheduleIv, taskIv, teamIv, mineIv};
        photoArrChecked = new int[]{R.mipmap.schedule_iv_checked, R.mipmap.task_iv_checked, R.mipmap.team_iv_checked, R.mipmap.mine_iv_checked};
        photoArrUnChecked = new int[]{R.mipmap.schedule_iv_unchecked, R.mipmap.task_iv_unchecked, R.mipmap.team_iv_unchecked, R.mipmap.mine_iv_unchecked};
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        if (HttpUrl.POSITION[3].equals(postType)) {
            scheduleLinear.setVisibility(View.GONE);
            teamLinear.setVisibility(View.GONE);
            tran.add(R.id.linear_main, mFragments.get(1));
            current = 1;
            checkChange(1);
        } else {
            refreshApply();
            refreshMessage();
            tran.add(R.id.linear_main, mFragments.get(0));
        }
        tran.commit();
    }

    @Override
    public void setListener() {
    }

    public void getFragment() {
        Fragment fragmentCu = mFragments.get(current);
        Fragment fragmentTar = mFragments.get(target);
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.hide(fragmentCu);
        if (fragmentTar.isAdded()) {
            tran.show(fragmentTar);
        } else {
            tran.add(R.id.linear_main, fragmentTar);
        }
        current = target;
        tran.commit();

    }

    public void checkChange(int target) {
        for (int i = 0; i < tvArr.length; i++) {
            if (i == target) {
                tvArr[i].setTextColor(Color.rgb(109, 188, 252));
                ivArr[i].setImageResource(photoArrChecked[i]);
            } else {
                tvArr[i].setTextColor(Color.rgb(59, 59, 59));
                ivArr[i].setImageResource(photoArrUnChecked[i]);
            }
        }
    }

    @OnClick({R.id.team_linear, R.id.schedule_linear, R.id.task_linear, R.id.mine_linear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.schedule_linear:
                target = 0;
                checkChange(target);
                getFragment();
                break;
            case R.id.task_linear:
                target = 1;
                checkChange(target);
                getFragment();
                break;
            case R.id.team_linear:
                target = 2;
                checkChange(target);
                getFragment();
                break;
            case R.id.mine_linear:
                target = 3;
                checkChange(target);
                getFragment();
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        Login login = new Gson().fromJson(object, Login.class);
        if ("申请列表红点".equals(flag)) {
            if (login.getResultObj().getVerfityCount()<100&&login.getResultObj().getVerfityCount()>0) {
                teamApplyRed.setVisibility(View.VISIBLE);
                teamApplyRed.setText(String.valueOf(login.getResultObj().getVerfityCount()));
            }else if (login.getResultObj().getVerfityCount()>=100){
                teamApplyRed.setVisibility(View.VISIBLE);
                teamApplyRed.setText("99+");
            }else {
                teamApplyRed.setVisibility(View.GONE);
            }
        }
        if ("消息列表红点".equals(flag)) {
            if (login.getResultObj().getMessageCount()<100&&login.getResultObj().getMessageCount()>0) {
                taskMessageRed.setVisibility(View.VISIBLE);
                taskMessageRed.setText(String.valueOf(login.getResultObj().getMessageCount()));
            }else if (login.getResultObj().getMessageCount()>=100){
                taskMessageRed.setVisibility(View.VISIBLE);
                taskMessageRed.setText("99+");
            }else {
                taskMessageRed.setVisibility(View.GONE);
            }
        }
    }

    //双击退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 6200) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
            } else {
//                IntentWrapper.onBackPressed(this);
//                Process.killProcess(Process.myPid());
//                System.exit(0);
                finish();
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.MAIN");
//                intent.addCategory("android.intent.category.HOME");
//                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
    public void refreshMessage() {
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.APPLYREDDOTINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "消息列表红点", HttpUrl.APPLYREDDOT_URL);
    }
    public void refreshApply() {
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.APPLYREDDOTINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "申请列表红点", HttpUrl.APPLYREDDOT_URL);
    }
}
