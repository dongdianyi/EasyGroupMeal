package gzkj.easygroupmeal.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.TakePhotoActivity;
import gzkj.easygroupmeal.bean.Bean;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.bean.MapData;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.GlideLoadUtils;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

//认证
public class AuthenticationActivity extends TakePhotoActivity {

    DialogCircle dialogCircle;
    @BindView(R.id.enterprise_name_ed)
    EditText enterpriseNameEd;
    @BindView(R.id.enterprise_address_ed)
    EditText enterpriseAddressEd;
    @BindView(R.id.proof_material)
    ImageView proofMaterial;
    @BindView(R.id.restaurant_name_ed)
    EditText restaurantNameEd;
    @BindView(R.id.restaurant_nickname_ed)
    EditText restaurantNicknameEd;
    private TextView[] tv;
    private Uri imageUri;
    private CropOptions.Builder cropBuilder;
    private String sid;
    private CommitParam commitParam;

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,

    };
    private String address;
    private String postType;
    private String path="http://img0.imgtn.bdimg.com/it/u=538260288,976318836&fm=26&gp=0.jpg";

    @Override
    public int intiLayout() {
        return R.layout.activity_authentication;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        postType = mShared.getSharedPreference("posttype","").toString();
        EventBus.getDefault().register(this);

    }

    @Override
    public void setListener() {

    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("上传图片".equals(flag)) {
            Bean bean=new Gson().fromJson(object,Bean.class);
            if (bean.getFilePath().size()>0) {
                path = bean.getFilePath().get(0);
            }
        }
        if ("甲方认证".equals(flag)) {
            Login login=new Gson().fromJson(object,Login.class);
            toastShort(login.getResultDesc());
            mShared.put("status", "1");
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @OnClick({R.id.restaurant_name_ed,R.id.enterprise_address_iv,R.id.enterprise_address_ed,R.id.proof_material_tv, R.id.back, R.id.submission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.enterprise_address_ed:
            case R.id.enterprise_address_iv:
                getPermissions();
                break;
            case R.id.restaurant_name_ed:
                startActivity(new Intent(this, SchoolActivity.class));
                break;
            case R.id.submission:
                if (enterpriseNameEd.length() == 0) {
                    toastShort("请填写企业名称");
                    return;
                }
                if (restaurantNameEd.length() == 0) {
                    toastShort("请填写餐厅名称");
                    return;
                }
                if (enterpriseAddressEd.length() == 0) {
                    toastShort("请填写地址");
                    return;
                }
                if (path==null) {
                    toastShort("请上传证明资料");
                    return;
                }
                commitParam=new CommitParam();
                commitParam.setWorkUnit(enterpriseNameEd.getText().toString().trim());
                commitParam.setPostType(postType);
                commitParam.setSchoolZone(restaurantNameEd.getText().toString().trim());
                commitParam.setOtherName(restaurantNicknameEd.getText().toString().trim());
                commitParam.setAddress(address);
                commitParam.setImageUrl(path);
                body = commitParam.getBody(sid, HttpUrl.AUTHENTICATIONINTERFACE, commitParam);
                presenter=new Presenter(this);
                presenter.getData("POST", "甲方认证", HttpUrl.AUTHENTICATION_URL);
                break;
            case R.id.proof_material_tv:
                avatarPop();
                break;
        }
    }

    private void getPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .request(needPermissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            startActivity(new Intent(AuthenticationActivity.this, MapActivity.class));
                        } else {
                            toastShort("请先获取权限");
                        }
                    }
                });

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MapData mapData) {
        enterpriseAddressEd.setText(mapData.getAddress());
        address = mapData.getAddress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(String string) {
        restaurantNameEd.setText(string);
    }
    public void avatarPop() {

        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        //压缩设置
        CompressConfig config = new CompressConfig.Builder().setMaxSize(102400)
                .setMaxPixel(800)
                .enableReserveRaw(true)
                .create();
        getTakePhoto().onEnableCompress(config, false);
        //拍照角度 使用takephoto自带相册
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        builder.setCorrectImage(true);
        getTakePhoto().setTakePhotoOptions(builder.create());

        //裁剪设置

        cropBuilder = new CropOptions.Builder();
        cropBuilder.setOutputX(800).setOutputY(800);
        cropBuilder.setWithOwnCrop(true);


        View avatarView = View.inflate(this, R.layout.avatar_pop, null);
        TextView photographTv = avatarView.findViewById(R.id.photograph_tv);
        TextView photoTv = avatarView.findViewById(R.id.photo_tv);
        TextView cancelTv = avatarView.findViewById(R.id.cancel_tv);
        tv = new TextView[]{photographTv, photoTv, cancelTv};
        dialogCircle = MyApplication.getInstance().getPop(this, avatarView, 1.1f, 3, Gravity.BOTTOM, R.style.BottomDialog_Animation, true);
        for (int i = 0; i < tv.length; i++) {
            final int finalI = i;
            tv[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI == 0) {
                        getTakePhoto().onPickFromCaptureWithCrop(imageUri, cropBuilder.create());
                    }
                    if (finalI == 1) {
                        getTakePhoto().onPickFromGalleryWithCrop(imageUri, cropBuilder.create());
                    }
                    dialogCircle.dismiss();
                }
            });
        }
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        TImage tImage = result.getImage();
        File file = new File(tImage.getCompressPath());
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("file", file.getName(), imageBody);
        fileBody=new ArrayList<>();
        fileBody.add(imageBodyPart);
        presenter = new Presenter(this);
        presenter.getDataFile("POST", "上传图片", HttpUrl.AVATAE_URL);
        if (file != null) {
            GlideLoadUtils.getInstance().displayImage(this, file, proofMaterial);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
