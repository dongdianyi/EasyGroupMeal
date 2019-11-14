package gzkj.easygroupmeal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Company;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.GlideLoadUtils;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;

public class TeamStaffActivity extends BaseActivity {

    @BindView(R.id.mine_avatar)
    CircleImageView mineAvatar;
    @BindView(R.id.mine_avatar_tv)
    TextView mineAvatarTv;
    @BindView(R.id.username_tv)
    TextView usernameTv;
    @BindView(R.id.team_staff_name_tv)
    TextView teamStaffNameTv;
    @BindView(R.id.team_staff_position_tv)
    TextView teamStaffPositionTv;
    @BindView(R.id.team_staff_phone_tv)
    TextView teamStaffPhoneTv;
    @BindView(R.id.delete)
    TextView deleteTv;
    private Company.ResultObjBean resultObjBean;
    private String name;
    private String companyName;
    private String sid;
    private String postType;
    private DialogCircle dialogCircle;
    private Dialog dialog;


    @Override
    public int intiLayout() {
        return R.layout.activity_team_staff;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(this, "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        postType = mShared.getSharedPreference("posttype", "").toString();
        resultObjBean = (Company.ResultObjBean) getIntent().getSerializableExtra("staff");
        name = resultObjBean.getUserName();
        if (resultObjBean.getCompanyName() != null && resultObjBean.getCompanyName().length() > 0) {
            companyName = resultObjBean.getCompanyName();
        } else if (resultObjBean.getTeamName() != null && resultObjBean.getTeamName().length() > 0) {
            companyName = resultObjBean.getTeamName();
        }
        if (resultObjBean.getHead() != null && !resultObjBean.getHead().equals("")) {
            mineAvatarTv.setVisibility(View.GONE);
            mineAvatar.setVisibility(View.VISIBLE);
            GlideLoadUtils.getInstance().glideLoad(this, resultObjBean.getHead(), mineAvatar, R.mipmap.photo);
        } else {
            mineAvatar.setVisibility(View.INVISIBLE);
            mineAvatarTv.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(name) && name.length() >= 2) {
                mineAvatarTv.setText(name.substring(name.length() - 2, name.length()));
            } else {
                mineAvatarTv.setText(name);
            }
        }
        usernameTv.setText(name);
        teamStaffNameTv.setText(companyName);
        teamStaffPositionTv.setText(resultObjBean.getPostName());
        teamStaffPhoneTv.setText(resultObjBean.getTelnum());

        if (HttpUrl.POSITION[0].equals(postType)) {
            if (HttpUrl.POSITION[1].equals(resultObjBean.getPostType())) {
                deleteTv.setVisibility(View.VISIBLE);
            } else {
                deleteTv.setVisibility(View.GONE);
            }
        } else if (HttpUrl.POSITION[1].equals(postType)) {
            if (HttpUrl.POSITION[2].equals(resultObjBean.getPostType())) {
                deleteTv.setVisibility(View.VISIBLE);
            } else {
                deleteTv.setVisibility(View.GONE);
            }
        } else {
            deleteTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void setListener() {

    }

    public void deleteStaff() {
        CommitParam commitParam = new CommitParam();
        commitParam.setUserId(resultObjBean.getUserId());
        body = commitParam.getBody(sid, HttpUrl.DELETESTAFFINTERFACE, commitParam);
        presenter = new Presenter(this);
        presenter.getData("POST", "删除人员", HttpUrl.DELETESTAFF_URL);
    }

    @OnClick({R.id.delete, R.id.back, R.id.team_staff_phone_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.delete:
                //删除操作
                deletePop("确定移除该人员吗?");
                break;
            case R.id.team_staff_phone_iv:
                callPhone(resultObjBean.getTelnum());
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("删除人员".equals(flag)) {
            dialog.dismiss();
            Login login = new Gson().fromJson(object, Login.class);
            toastShort(login.getResultDesc());
            Intent intent = new Intent();
            setResult(1001, intent);
            finish();
        }
    }

    @Override
    public void fail(String flag, Throwable t) {
        super.fail(flag, t);
        dialog.dismiss();
    }

    public void deletePop(String mTitle) {
        View updateStateView = View.inflate(this, R.layout.update_state_pop, null);
        TextView sureTv = updateStateView.findViewById(R.id.sure);
        TextView title = updateStateView.findViewById(R.id.title);
        TextView cancelTv = updateStateView.findViewById(R.id.cancel_tv);
        title.setText(mTitle);
        TextView textView[] = new TextView[]{sureTv, cancelTv};
        dialogCircle = MyApplication.getInstance().getPop(this, updateStateView, 1.1f, 3, Gravity.BOTTOM, R.style.BottomDialog_Animation, true);
        for (int i = 0; i < textView.length; i++) {
            final int finalI = i;
            textView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI == 0) {
                        dialog = DialogUIUtils.showLoading(TeamStaffActivity.this, "正在移除...", false, true, true, true).show();
                        deleteStaff();
                    }
                    dialogCircle.dismiss();
                    dialogCircle = null;
                }
            });
        }
    }

}
