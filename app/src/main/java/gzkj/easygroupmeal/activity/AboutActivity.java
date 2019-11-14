package gzkj.easygroupmeal.activity;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.utli.SingleClick;
import gzkj.easygroupmeal.utli.VersionUtils;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_about)
    TextView tvAbout;

    @Override
    public int intiLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        tvAbout.setText(VersionUtils.getVersionName(this));
    }

    @Override
    public void setListener() {
    }
    @SingleClick
    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
