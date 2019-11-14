package gzkj.easygroupmeal.activity;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.TakePhotoActivity;
import gzkj.easygroupmeal.bean.Bean;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.GlideLoadUtils;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.SingleClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PersonalDataActivity extends TakePhotoActivity {

    @BindView(R.id.name_ed)
    EditText nameEd;
    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.avatar_tv)
    TextView avatarTv;
    private Uri imageUri;
    private CropOptions.Builder cropBuilder;
    DialogCircle dialogCircle;
    private TextView[] tv;
    private CommitParam commitParam;
    private String sid;
    private String name;
    private TakePhoto takePhoto;
    private String path;

    @Override
    public int intiLayout() {
        return R.layout.activity_personal_data;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(this, "userinfo");
        name = mShared.getSharedPreference("name", "").toString();
        path = mShared.getSharedPreference("head", "").toString();
        sid = mShared.getSharedPreference("sid", "").toString();
        nameEd.setText(name);
        if (path != null&&path.length()>0) {
            avatar.setVisibility(View.VISIBLE);
            avatarTv.setVisibility(View.GONE);
            GlideLoadUtils.getInstance().glideLoad(this, path, avatar,R.mipmap.photo);
        } else {
            avatar.setVisibility(View.GONE);
            avatarTv.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(name) &&name.length()>=2) {
                avatarTv.setText(name.substring(name.length() - 2, name.length()));
            }else {
                avatarTv.setText(name);
            }
        }
    }

    @Override
    public void setListener() {

    }
    @SingleClick
    @OnClick({R.id.back, R.id.sure, R.id.avatar_linear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                commitParam = new CommitParam();
                commitParam.setUserName(nameEd.getText().toString().trim());
                logger("path"+path);
                commitParam.setHead_image(path);
                body = commitParam.getBody(sid, HttpUrl.MODIFYINFORINTERFACE, commitParam);
                presenter = new Presenter(this);
                presenter.getData("POST", "修改用户信息", HttpUrl.MODIFYINFOR_URL);
                break;
            case R.id.avatar_linear:
                avatarPop();
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("上传图片".equals(flag)) {
            Bean bean=new Gson().fromJson(object,Bean.class);
            if (bean.getFilePath().size()>0) {
                path = bean.getFilePath().get(0);
            }
        }
        if ("修改用户信息".equals(flag)) {
            mShared.put("name", nameEd.getText().toString().trim());
            mShared.put("head", path);
            EventBus.getDefault().postSticky("userInfo");
            toastShort("修改成功");
            finish();
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
            avatar.setVisibility(View.VISIBLE);
            avatarTv.setVisibility(View.GONE);
            GlideLoadUtils.getInstance().displayImage(this, file, avatar);
        } else {
            avatar.setVisibility(View.GONE);
            avatarTv.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(name) &&name.length()>=2) {
                avatarTv.setText(name.substring(name.length() - 2, name.length()));
            }else {
                avatarTv.setText(name);
            }
        }
    }
    public void avatarPop() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        takePhoto = getTakePhoto();
        imageUri = Uri.fromFile(file);
        //压缩设置
        CompressConfig config = new CompressConfig.Builder().setMaxSize(102400)
                .setMaxPixel(800)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, false);
        //拍照角度 使用takephoto自带相册
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        builder.setCorrectImage(false);
        takePhoto.setTakePhotoOptions(builder.create());

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
                        takePhoto.onPickFromCaptureWithCrop(imageUri, cropBuilder.create());
                    }
                    if (finalI == 1) {
                       takePhoto.onPickFromGalleryWithCrop(imageUri, cropBuilder.create());
                    }
                    dialogCircle.dismiss();
                }
            });
        }
    }
}
