package gzkj.easygroupmeal.activity;

import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.SingleClick;

public class AddNoticeActivity extends BaseActivity {

    @BindView(R.id.notice_ed)
    EditText noticeEd;
    private String sid;
    private CommitParam commitParam;

    @Override
    public int intiLayout() {
        return R.layout.activity_add_notice;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        sid = getIntent().getStringExtra("sid");
    }

    @Override
    public void setListener() {

    }
    @SingleClick
    @OnClick({R.id.back, R.id.release_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.release_tv:
                commitParam = new CommitParam();
                commitParam.setContent(noticeEd.getText().toString().trim());
                body = commitParam.getBody(sid, HttpUrl.ADDNOTICEINTERFACE, commitParam);
                presenter=new Presenter(this);
                presenter.getData("POST", "添加公告", HttpUrl.ADDNOTICE_URL);
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        toastShort("发布成功");
        EventBus.getDefault().postSticky("notice");
        finish();
    }
}
