package gzkj.easygroupmeal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.dou361.dialogui.DialogUIUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DataCleanManager;
import gzkj.easygroupmeal.utli.DialogCircle;
import okhttp3.RequestBody;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.clear_cache_tv)
    TextView clearCacheTv;
    private View clearCacheView;
    private DialogCircle dialogCircle;
    private TextView cancelTv;
    private TextView sureTv;
    //网络请求传参
    //控制器发出请求
    private Presenter presenter = new Presenter(this);
    private Dialog dialog;
    //下载更新功能（只使用下载功能）

    private DownloadBuilder builder;

    @Override
    public int intiLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        //获得应用内部缓存(/data/data/gzkj.easygroupmeal/cache)
        File file = new File(this.getCacheDir().getPath());
        try {
            clearCacheTv.setText(DataCleanManager.getCacheSize(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.back, R.id.account_management_linear, R.id.clear_cache_linear, R.id.new_version_linear, R.id.about_linear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.account_management_linear:
                startActivity(new Intent(this, AccountManagementActivity.class));
                break;
            case R.id.clear_cache_linear:
                clearCacheView = View.inflate(this, R.layout.sign_out_pop, null);
                TextView title = clearCacheView.findViewById(R.id.title);
                title.setText("确定清除缓存？");
                sureTv = clearCacheView.findViewById(R.id.sure_tv);
                cancelTv = clearCacheView.findViewById(R.id.cancel_tv);
                dialogCircle = MyApplication.getInstance().getPop(this, clearCacheView, 1.3f, 4, Gravity.CENTER, 0, true);
                sureTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogCircle != null) {
                            dialogCircle.dismiss();
                            dialogCircle = null;
                            dialog = DialogUIUtils.showLoading(SettingActivity.this, "正在清理...", false, true, true, true).show();
                            presenter = new Presenter(SettingActivity.this);
                            presenter.getData("clear", "clear", "clear");
                        }
                    }
                });
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogCircle != null) {
                            dialogCircle.dismiss();
                            dialogCircle = null;
                        }
                    }
                });
                break;
            case R.id.new_version_linear:
                toastShort("已是最新版本");
                //更新下载
//                builder = AllenVersionChecker
//                        .getInstance().downloadOnly(crateUIData());
//                //强制重新下载
//                builder.setForceRedownload(true);
//                //显示下载通知栏
//                builder.setShowNotification(true);
//                //不显示下载框
//                builder.setShowDownloadingDialog(false);
//                //不显示下载失败对话框
//                builder.setShowDownloadFailDialog(false);
//                //自定义更新框
//                builder.setCustomVersionDialogListener(createCustomDialogOne());
//                //自定义下载路径
//                builder.setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/easygroup/");
//                //执行
//                builder.executeMission(this);
                break;
            case R.id.about_linear:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        toastShort(object);
        dialog.dismiss();
        clearCacheTv.setText("0.0KB");
    }

    @Override
    public void fail(String flag, Throwable t) {
        toastShort(t.getMessage());
        dialog.dismiss();
    }

    @Override
    public RequestBody data() {
        return null;
    }

    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    private UIData crateUIData() {
        UIData uiData = UIData.create();
        //设置更新标题
        uiData.setTitle("确定更新吗?");
        //设置更新内容
        uiData.setContent("");
        //设置更新地址
        uiData.setDownloadUrl("");
        return uiData;
    }
    private CustomVersionDialogListener createCustomDialogOne() {
        return (context, versionBundle) -> {
            //自定义更新弹出框
            View updateView = View.inflate(context, R.layout.update_pop, null);
            TextView title=updateView.findViewById(R.id.title);
            TextView content=updateView.findViewById(R.id.content);
            title.setText(versionBundle.getTitle());
            content.setText(versionBundle.getContent());
            dialogCircle = MyApplication.getInstance().getPop(context, updateView, 1.3f, 4, Gravity.CENTER, 0, true);
            return dialogCircle;
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //版本更新注销
//        AllenVersionChecker.getInstance().cancelAllMission(this);
    }
}
