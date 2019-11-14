package gzkj.easygroupmeal.activity;

import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.BaseActivity;

public class InvitationActivity extends BaseActivity {

    @BindView(R.id.name_ed)
    EditText nameEd;
    @BindView(R.id.identity_ed)
    EditText identityEd;
    @BindView(R.id.phone_ed)
    EditText phoneEd;

    @Override
    public int intiLayout() {
        return R.layout.activity_invitation;
    }

    @Override
    public void initData() {
        hideTopUIMenu();

    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.back, R.id.sure, R.id.identity_iv, R.id.identity_ed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                finish();
                break;
            case R.id.identity_iv:
                break;
            case R.id.identity_ed:
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {

    }
}
