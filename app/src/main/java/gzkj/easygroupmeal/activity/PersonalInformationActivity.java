package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
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
import gzkj.easygroupmeal.utli.TextChange;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PersonalInformationActivity extends TakePhotoActivity {

    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.name_ed)
    EditText nameEd;
    @BindView(R.id.position_ed)
    EditText positionEd;
    @BindView(R.id.start)
    TextView start;
    @BindView(R.id.position_spinner)
    ImageView positionSpinner;
    DialogCircle dialogCircle;
    private TextView[] tv;
    private Uri imageUri;
    private CropOptions.Builder cropBuilder;
    //职位
    private String position;
    private CommitParam commitParam;
    private String sid;
    private String path;

    @Override
    public int intiLayout() {
        return R.layout.activity_personal_information;
    }

    @Override
    public void initData() {
        hideTopUIMenu();

        start.setEnabled(false);
        sid = getIntent().getStringExtra("sid");
        mShared = new SharedPreferencesHelper(this, "userinfo");
    }

    @Override
    public void setListener() {
//监听所有的edittext
        TextChange textChange = new OnTextChange();
        positionEd.addTextChangedListener(textChange);
        nameEd.addTextChangedListener(textChange);
    }

    @OnClick({R.id.back, R.id.position_ed, R.id.position_spinner, R.id.avatar, R.id.start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.avatar:
                avatarPop();
                break;
            case R.id.start:
                commitParam = new CommitParam();
                commitParam.setUserName(nameEd.getText().toString().trim());
                commitParam.setPostType(position);
                commitParam.setHead_image(path);
                body = commitParam.getBody(sid, HttpUrl.MODIFYINFORINTERFACE, commitParam);
                presenter=new Presenter(this);
                presenter.getData("POST", "修改用户信息", HttpUrl.MODIFYINFOR_URL);
                break;
            case R.id.position_spinner:
            case R.id.position_ed:
                positionPop();
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
            mShared.put("posttype", position);
            mShared.put("name", nameEd.getText().toString().trim());
            mShared.put("head", path);
            if (positionEd.getText().toString().trim().equals(getString(R.string.person_in_charge))) {
                Intent intent = new Intent(this, AuthenticationActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, StartActivity.class);
                intent.putExtra("posttype", position);
                startActivity(intent);

            }
        }
    }

    public void positionPop() {
        View positionView = View.inflate(this, R.layout.position_pop, null);
        TextView foreman = positionView.findViewById(R.id.foreman);
        TextView chiefInspector = positionView.findViewById(R.id.chief_inspector);
        TextView manager = positionView.findViewById(R.id.manager);
        TextView personInCharge = positionView.findViewById(R.id.person_in_charge);
        tv = new TextView[]{chiefInspector, manager, foreman, personInCharge};
        dialogCircle = MyApplication.getInstance().getPop(this, positionView, 1.3f, 3, Gravity.CENTER, 0, true);
        for (int i = 0; i < tv.length; i++) {
            final int finalI = i;
            tv[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positionEd.setText(tv[finalI].getText());
                    position = HttpUrl.POSITION[finalI];
                    dialogCircle.dismiss();
                }
            });
        }
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

    class OnTextChange extends TextChange {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (nameEd.length() > 0 && positionEd.length() > 0) {
                start.setBackgroundResource(R.drawable.fillet_blue_more);
                start.setEnabled(true);
            } else {
                start.setBackgroundResource(R.drawable.fillet_blueshallow_more);
                start.setEnabled(false);
            }
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
            GlideLoadUtils.getInstance().displayImage(this, file, avatar);
        }
    }

}
