package gzkj.easygroupmeal.activity;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.utli.NetBroadcastReceiver;

public class StartActivity extends BaseActivity {

    public static NetBroadcastReceiver.NetEvevt evevt;
    @BindView(R.id.create_team)
    TextView createTeam;
    private String postType;


    @Override
    public int intiLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void initData() {
        //隐藏状态栏
        hideTopUIMenu();

        if (!inspectNet()) {
            toastShort(getString(R.string.network));
        }
        postType = getIntent().getStringExtra("posttype");
        Log.e("posttype",postType);
        if (HttpUrl.POSITION[0].equals(postType)) {
            createTeam.setText(getString(R.string.join_company));
        }
        if (HttpUrl.POSITION[1].equals(postType)) {
            createTeam.setText(getString(R.string.create_team));
        }
        if (HttpUrl.POSITION[2].equals(postType)) {
            createTeam.setText(getString(R.string.join_team));
        }
    }

    @Override
    public void setListener() {

    }

    @OnClick(R.id.create_team)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_team:
                if (HttpUrl.POSITION[0].equals(postType)) {
                    //总监加入公司
                    Intent intent=new Intent(this, JoinCompanyActivity.class);
                    intent.putExtra("posttype",postType);
                    startActivity(intent);
                }
                if (HttpUrl.POSITION[1].equals(postType)) {
                    //经理创建团队
                    Intent intent=new Intent(this, CreateTeamActivity.class);
                    intent.putExtra("posttype",postType);
                    startActivity(intent);
                }
                if (HttpUrl.POSITION[2].equals(postType)) {
                    //班组长加入团队
                    Intent intent=new Intent(this, JoinCompanyActivity.class);
                    intent.putExtra("posttype",postType);
                    startActivity(intent);
                }
                break;
        }
    }

}
