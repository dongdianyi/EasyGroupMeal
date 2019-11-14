package gzkj.easygroupmeal.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.meetsl.scardview.SCardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.activity.UpdateTaskActivity;
import gzkj.easygroupmeal.adapter.ScheduleAdapter;
import gzkj.easygroupmeal.adapter.SpinnerAdapter;
import gzkj.easygroupmeal.adapter.TaskAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.TakePhotoFragment;
import gzkj.easygroupmeal.bean.Bean;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Cycle;
import gzkj.easygroupmeal.bean.Login;
import gzkj.easygroupmeal.bean.Task;
import gzkj.easygroupmeal.bean.TeamStaff;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.GlideLoadUtils;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.StatusBarUtils;
import gzkj.easygroupmeal.view.OnChangeView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskTwoFragment extends TakePhotoFragment implements AdapterView.OnItemSelectedListener {


    @BindView(R.id.task_recycler)
    LRecyclerView taskRecycler;
    private String mTitle;
    private TaskAdapter mTaskAdapter;
    private ScheduleAdapter mScheduleAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<Task.ResultObjBean> mData;
    private TextView[] tv, tvUpdate;
    private View[] views;
    private DialogCircle dialogCircle, dialogCircle2;
    private TextView submissionTv;
    private ImageView camear;
    private ImageView photoIv;
    private Uri imageUri;
    private CropOptions.Builder cropBuilder;
    private CommitParam commitParam;
    private String sid;
    private ArrayAdapter arrayAdapter;
    private SpinnerAdapter teamArrayAdapter, staffArrayAdapter;
    private List<Cycle> mDataTeam, mDataStaff;
    private int positionId;
    private EditText errorReasonEd;
    private TeamStaff teamStaff;
    private int staffPosition;
    private int index;
    private String submitId;
    private String recordId;
    private HeaderView mHeaderView;
    private View headView;
    private String path;
    private TextView photographTv;
    private TextView titlePop;

    public TaskTwoFragment() {
        // Required empty public constructor
    }

    public static TaskTwoFragment newInstance(String title, String flag) {
        TaskTwoFragment fragment = new TaskTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("flag", flag);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_task_two;
    }

    @Override
    protected void initView() {
        StatusBarUtils.with(getActivity())
                .init();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        mTitle = getArguments().getString("title");
        ArrayList mList = new ArrayList();
        mList.add("全部任务");
        mList.add("未完成");
        mList.add("已完成");
        arrayAdapter = new ArrayAdapter(getContext(), R.layout.task_spinner_tv, mList);
        arrayAdapter.setDropDownViewResource(R.layout.task_spinner_tv_pop);

        mData = new ArrayList<>();
        mDataTeam = new ArrayList<>();
        mDataStaff = new ArrayList<>();

        mTaskAdapter = new TaskAdapter(MyApplication.getContextObject(), R.layout.task_item_chief_inspector, "task");
        mScheduleAdapter = new ScheduleAdapter(MyApplication.getContextObject(), R.layout.schedule_item, "task");
        headView = LayoutInflater.from(MyApplication.getContextObject()).inflate(R.layout.task_top, null, false);
        mHeaderView = new HeaderView(headView);
        mHeaderView.taskSpinner.setPopupBackgroundResource(R.drawable.fillet_blue);
        mHeaderView.taskSpinner.setAdapter(arrayAdapter);
        DividerDecoration divider = new DividerDecoration.Builder(getContext())
                .setHeight(R.dimen.dp_14)
                .setColorResource(R.color.line_color)
                .build();
        taskRecycler.addItemDecoration(divider);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        taskRecycler.setLayoutManager(manager);
        if (mTitle.equals("个人任务")) {
            personTask("0");
        } else {
            teamTask();
            commitParam = new CommitParam();
            commitParam.setType("1");
            body = commitParam.getBody(sid, HttpUrl.TEAMINTERFACE, commitParam);
            presenter = new Presenter(TaskTwoFragment.this);
            presenter.getData("POST", "团队员工列表", HttpUrl.TEAM_URL);
        }
    }

    class HeaderView {
        @BindView(R.id.task_spinner)
        AppCompatSpinner taskSpinner;
        @BindView(R.id.notes_task)
        LinearLayout notesTask;
        @BindView(R.id.staff_task)
        SCardView staffTask;
        @BindView(R.id.team_spinner)
        AppCompatSpinner teamSpinner;
        @BindView(R.id.staff_spinner)
        AppCompatSpinner staffSpinner;

        public HeaderView(View headerRootView) {
            ButterKnife.bind(this, headerRootView);
        }
    }

    public void personTask(String taskStatus) {
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mTaskAdapter);
        mLRecyclerViewAdapter.addHeaderView(headView);
        mLRecyclerViewAdapter.addFooterView(getLayoutInflater().inflate(R.layout.schedule_bottom, (ViewGroup) getActivity().getWindow().getDecorView(),false));
        taskRecycler.setAdapter(mLRecyclerViewAdapter);
        mHeaderView.notesTask.setVisibility(View.VISIBLE);
        mHeaderView.staffTask.setVisibility(View.GONE);
        commitParam = new CommitParam();
        commitParam.setTaskStatus(taskStatus);
        body = commitParam.getBody(sid, HttpUrl.PERSONALTASKINTERFACE, commitParam);
        presenter=new Presenter(this);
        presenter.getData("POST", "个人任务", HttpUrl.PERSONALTASK_URL);
    }

    public void teamTask() {
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mScheduleAdapter);
        mLRecyclerViewAdapter.addHeaderView(headView);
        mLRecyclerViewAdapter.addFooterView(getLayoutInflater().inflate(R.layout.schedule_bottom, (ViewGroup) getActivity().getWindow().getDecorView(),false));
        taskRecycler.setAdapter(mLRecyclerViewAdapter);
        mHeaderView.notesTask.setVisibility(View.GONE);
        mHeaderView.staffTask.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        mHeaderView.taskSpinner.setOnItemSelectedListener(this);
        mHeaderView.teamSpinner.setOnItemSelectedListener(this);
        taskRecycler.setLoadMoreEnabled(false);
        taskRecycler.setPullRefreshEnabled(false);
        mScheduleOnChange();
        mTaskOnchange();
    }

    public void mTaskOnchange() {
        mTaskAdapter.setOnChangeListener(new OnChangeView() {
            @Override
            public void onChange(View view, int position, String flag) {
                positionId = position;
                recordId = mData.get(position).getRecordId();
                if (mData.get(position).getSubmitId() != null) {
                    submitId = mData.get(position).getSubmitId();
                }
                if (view.getId() == R.id.exception_submit) {//异常提交
                    exceptionPop("2");
                }
                if (view.getId() == R.id.submit_normally) {
                    updatePop("确定提交吗?", "normally");
                }
                if (view.getId() == R.id.update_state) {
                    updatePop("确定要更新此条工作记录吗?", "update");
                }
                if (view.getId() == R.id.no_complete) {
                    noCompletePop();
                }
            }
        });
    }

    public void mScheduleOnChange() {
        mScheduleAdapter.setOnChangeListener(new OnChangeView() {
            @Override
            public void onChange(View view, int position, String flag) {
                if (view.getId() == R.id.modify_iv) {
                    Intent intent = new Intent(getActivity(), UpdateTaskActivity.class);
                    intent.putExtra("task", mData.get(position));
                    intent.putExtra("flag", "tasktwo");
                    startActivity(intent);
                }
            }
        });
    }

    public void updatePop(String mTitle, final String status) {
        View updateStateView = View.inflate(getContext(), R.layout.update_state_pop, null);
        TextView sureTv = updateStateView.findViewById(R.id.sure);
        TextView title = updateStateView.findViewById(R.id.title);
        TextView cancelTv = updateStateView.findViewById(R.id.cancel_tv);
        title.setText(mTitle);
        tvUpdate = new TextView[]{sureTv, cancelTv};
        dialogCircle = MyApplication.getInstance().getPop(getContext(), updateStateView, 1.1f, 3, Gravity.BOTTOM, R.style.BottomDialog_Animation, true);
        for (int i = 0; i < tvUpdate.length; i++) {
            final int finalI = i;
            tvUpdate[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogCircle.dismiss();
                    dialogCircle = null;
                    //正常提交任务
                    if (finalI == 0) {
                        if ("normally".equals(status)) {
                            commitParam = new CommitParam();
                            commitParam.setRecordId(mData.get(positionId).getRecordId());
                            commitParam.setTaskStatus("1");
                            commitParam.setErrorReason("");
                            body = commitParam.getBody(sid, HttpUrl.SUBMITTASKINTERFACE, commitParam);
                            presenter=new Presenter(TaskTwoFragment.this);
                            presenter.getData("POST", "正常任务提交", HttpUrl.SUBMITTASK_URL);
                        }
                        if ("update".equals(status)) {
                            if ("1".equals(mData.get(positionId).getTaskStatus())) {
                                exceptionPop("1");
                            }
                            if ("2".equals(mData.get(positionId).getTaskStatus())) {
                                commitParam = new CommitParam();
                                commitParam.setRecordId(recordId);
                                commitParam.setSubmitId(submitId);
                                commitParam.setTaskStatus("1");
                                commitParam.setErrorReason("");
                                body = commitParam.getBody(sid, HttpUrl.UPDATESTATETASKINTERFACE, commitParam);
                                presenter=new Presenter(TaskTwoFragment.this);
                                presenter.getData("POST", "修改状态", HttpUrl.UPDATESTATETASK_URL);
                            }
                        }
                    }
                }
            });
        }
    }

    public void exceptionPop(final String state) {
        View exceptionView = View.inflate(getContext(), R.layout.exception_pop, null);
        submissionTv = exceptionView.findViewById(R.id.submission);
        titlePop = exceptionView.findViewById(R.id.title_pop);
        titlePop.setText(getString(R.string.exception_submit));
        errorReasonEd = exceptionView.findViewById(R.id.error_reason_ed);
        errorReasonEd.setHint("请输入没有正常提交的原因");
        camear = exceptionView.findViewById(R.id.camear);
        photographTv = exceptionView.findViewById(R.id.photograph_tv);
        photoIv = exceptionView.findViewById(R.id.photo_iv);
        dialogCircle = MyApplication.getInstance().getPop(getContext(), exceptionView, 1.1f, 2, Gravity.CENTER, 0, true);
        views = new View[]{camear, photographTv, submissionTv};
        for (int i = 0; i < views.length; i++) {
            final int finalI = i;
            views[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI == 0 || finalI == 1) {
                        avatarPop();
                    } else {
                        if (errorReasonEd.getText().length()==0) {
                            toastShort("请输入没有正常提交的原因");
                            return;
                        }
                        if ("1".equals(state)) {
                            commitParam = new CommitParam();
                            commitParam.setRecordId(recordId);
                            commitParam.setSubmitId(submitId);
                            commitParam.setTaskStatus("2");
                            commitParam.setImage(path);
                            commitParam.setErrorReason(errorReasonEd.getText().toString().trim());
                            body = commitParam.getBody(sid, HttpUrl.UPDATESTATETASKINTERFACE, commitParam);
                            presenter=new Presenter(TaskTwoFragment.this);
                            presenter.getData("POST", "修改状态", HttpUrl.UPDATESTATETASK_URL);
                        }
                        if ("2".equals(state)) {
                            commitParam = new CommitParam();
                            commitParam.setRecordId(mData.get(positionId).getRecordId());
                            commitParam.setTaskStatus("2");
                            commitParam.setImage(path);
                            commitParam.setErrorReason(errorReasonEd.getText().toString().trim());
                            body = commitParam.getBody(sid, HttpUrl.SUBMITTASKINTERFACE, commitParam);
                            presenter=new Presenter(TaskTwoFragment.this);
                            presenter.getData("POST", "异常任务提交", HttpUrl.SUBMITTASK_URL);
                        }
                        dialogCircle.dismiss();
                        dialogCircle=null;
                    }
                }
            });
        }
    }

    private void noCompletePop() {
        View exceptionView = View.inflate(getContext(), R.layout.exception_pop, null);
        submissionTv = exceptionView.findViewById(R.id.submission);
        titlePop = exceptionView.findViewById(R.id.title_pop);
        titlePop.setText("未完成原因");
        errorReasonEd = exceptionView.findViewById(R.id.error_reason_ed);
        errorReasonEd.setHint("请输入未完成的原因");
        camear = exceptionView.findViewById(R.id.camear);
        photographTv = exceptionView.findViewById(R.id.photograph_tv);
        photoIv = exceptionView.findViewById(R.id.photo_iv);
        dialogCircle = MyApplication.getInstance().getPop(getContext(), exceptionView, 1.1f, 2, Gravity.CENTER, 0, true);
        views = new View[]{camear, photographTv, submissionTv};
        for (int i = 0; i < views.length; i++) {
            final int finalI = i;
            views[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI == 0 || finalI == 1) {
                        avatarPop();
                    } else {
                        if (errorReasonEd.getText().length()==0) {
                            toastShort("请输入未完成的原因");
                            return;
                        }
                        commitParam = new CommitParam();
                        commitParam.setRecordId(mData.get(positionId).getRecordId());
                        commitParam.setImage(path);
                        commitParam.setCause(errorReasonEd.getText().toString().trim());
                        body = commitParam.getBody(sid, HttpUrl.NOCOMPLETE, commitParam);
                        presenter=new Presenter(TaskTwoFragment.this);
                        presenter.getData("POST", "未完成备注", HttpUrl.NOCOMPLETE_URL);
                        dialogCircle.dismiss();
                        dialogCircle=null;
                    }
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
            GlideLoadUtils.getInstance().displayImage(getContext(), file, photoIv);
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


        View avatarView = View.inflate(getContext(), R.layout.avatar_pop, null);
        TextView photographTv = avatarView.findViewById(R.id.photograph_tv);
        TextView photoTv = avatarView.findViewById(R.id.photo_tv);
        photoTv.setVisibility(View.GONE);
        TextView cancelTv = avatarView.findViewById(R.id.cancel_tv);
        tv = new TextView[]{photographTv, photoTv, cancelTv};
        dialogCircle2 = MyApplication.getInstance().getPop(getContext(), avatarView, 1.1f, 3, Gravity.BOTTOM, R.style.BottomDialog_Animation, true);
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
                    dialogCircle2.dismiss();
                    dialogCircle2=null;
                }
            });
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
        if ("个人任务".equals(flag)) {
            Task task = new Gson().fromJson(object, Task.class);
            mData.clear();
            mData.addAll(task.getResultObj());
            mTaskAdapter.setDataList(mData);
        }
        if ("修改状态".equals(flag)) {
            Login login = new Gson().fromJson(object, Login.class);
            toastShort(login.getResultDesc());
            recordId = login.getResultObj().getRecordId();
            submitId = login.getResultObj().getSubmitId();
            mData.get(positionId).setTaskStatus(login.getResultObj().getTaskStatus());
        }
        if ("正常任务提交".equals(flag)) {
            Login login = new Gson().fromJson(object, Login.class);
            submitId = login.getResultObj().getSubmitId();
            recordId = login.getResultObj().getRecordId();
            Task.ResultObjBean mData = (Task.ResultObjBean) mTaskAdapter.getDataList().get(positionId);
            mData.setTaskType("2");
            mData.setTaskStatus("1");
            mTaskAdapter.getDataList().set(positionId, mData);
            mLRecyclerViewAdapter.notifyItemChanged(mLRecyclerViewAdapter.getAdapterPosition(false, positionId));
            toastShort(login.getResultDesc());
        }
        if ("异常任务提交".equals(flag)) {
            Login login = new Gson().fromJson(object, Login.class);
            submitId = login.getResultObj().getSubmitId();
            recordId = login.getResultObj().getRecordId();
            Task.ResultObjBean mData = (Task.ResultObjBean) mTaskAdapter.getDataList().get(positionId);
            mData.setTaskType("2");
            mData.setTaskStatus("2");
            mTaskAdapter.getDataList().set(positionId, mData);
            mLRecyclerViewAdapter.notifyItemChanged(mLRecyclerViewAdapter.getAdapterPosition(false, positionId));
            toastShort(login.getResultDesc());
        }
        if ("未完成备注".equals(flag)) {
            Login login = new Gson().fromJson(object, Login.class);
            toastShort(login.getResultDesc());
        }
        if ("团队员工列表".equals(flag)) {
            teamStaff = new Gson().fromJson(object, TeamStaff.class);
            mDataTeam.clear();
            for (TeamStaff.ResultObjBean resultObjBean : teamStaff.getResultObj()) {
                mDataTeam.add(new Cycle(resultObjBean.getTeamName(), resultObjBean.getTeamId()));
            }
            teamArrayAdapter = new SpinnerAdapter(getActivity(), mDataTeam);
            mHeaderView.teamSpinner.setAdapter(teamArrayAdapter);
        }
        if ("员工任务".equals(flag)) {
            Task task = new Gson().fromJson(object, Task.class);
            mData.clear();
            mData.addAll(task.getResultObj());
            mScheduleAdapter.setDataList(mData);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.task_spinner:
                index = position;
                if (position == 0) {
                    if (mTitle.equals("个人任务")) {
                        mTaskAdapter = new TaskAdapter(MyApplication.getContextObject(), R.layout.task_item_chief_inspector, "task");
                        personTask("0");
                        mTaskOnchange();
                    } else {
                        mScheduleAdapter = new ScheduleAdapter(MyApplication.getContextObject(), R.layout.schedule_item, "task");
                        teamTask();
                        mScheduleOnChange();
                        if (mDataStaff.size() > 0) {
                            commitParam = new CommitParam();
                            commitParam.setReceptId(mDataStaff.get(staffPosition).getCycleNum());
                            commitParam.setTaskType("0");
                            body = commitParam.getBody(sid, HttpUrl.TEAMTASKINTERFACE, commitParam);
                            presenter=new Presenter(this);
                            presenter.getData("POST", "员工任务", HttpUrl.TEAMTASK_URL);
                        }

                    }
                }
                if (position == 1) {
                    if (mTitle.equals("个人任务")) {
                        mTaskAdapter = new TaskAdapter(MyApplication.getContextObject(), R.layout.task_item_completed, mTitle + "task_no_completed");
                        personTask("1");
                        mTaskOnchange();
                    } else {
                        mScheduleAdapter = new ScheduleAdapter(MyApplication.getContextObject(), R.layout.task_item_completed, mTitle + "task_no_completed");
                        teamTask();
                        mScheduleOnChange();
                        if (mDataStaff.size() > 0) {
                            commitParam = new CommitParam();
                            commitParam.setReceptId(mDataStaff.get(staffPosition).getCycleNum());
                            commitParam.setTaskType("1");
                            body = commitParam.getBody(sid, HttpUrl.TEAMTASKINTERFACE, commitParam);
                            presenter=new Presenter(this);
                            presenter.getData("POST", "员工任务", HttpUrl.TEAMTASK_URL);
                        }
                    }
                }
                if (position == 2) {
                    if (mTitle.equals("个人任务")) {
                        mTaskAdapter = new TaskAdapter(MyApplication.getContextObject(), R.layout.task_item_completed, mTitle + "task_completed");
                        personTask("2");
                        mTaskOnchange();
                    } else {
                        mScheduleAdapter = new ScheduleAdapter(MyApplication.getContextObject(), R.layout.task_item_completed, mTitle + "task_completed");
                        teamTask();
                        mScheduleOnChange();
                        if (mDataStaff.size() > 0) {
                            commitParam = new CommitParam();
                            commitParam.setReceptId(mDataStaff.get(staffPosition).getCycleNum());
                            commitParam.setTaskType("2");
                            body = commitParam.getBody(sid, HttpUrl.TEAMTASKINTERFACE, commitParam);
                            presenter=new Presenter(this);
                            presenter.getData("POST", "员工任务", HttpUrl.TEAMTASK_URL);
                        }

                    }
                }
                break;
            case R.id.team_spinner:
                mDataStaff.clear();
                for (TeamStaff.ResultObjBean.TeamStaffBean teamStaffBean : teamStaff.getResultObj().get(position).getTeamStaff()) {
                    mDataStaff.add(new Cycle(teamStaffBean.getUserName(), teamStaffBean.getUserId()));
                }
                staffArrayAdapter = new SpinnerAdapter(getActivity(), mDataStaff);
                mHeaderView.staffSpinner.setAdapter(staffArrayAdapter);
                mHeaderView.staffSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.staff_spinner:
                staffPosition = position;
                commitParam = new CommitParam();
                commitParam.setReceptId(mDataStaff.get(position).getCycleNum());
                commitParam.setTaskType(index + "");
                body = commitParam.getBody(sid, HttpUrl.TEAMTASKINTERFACE, commitParam);
                presenter = new Presenter(TaskTwoFragment.this);
                presenter.getData("POST", "员工任务", HttpUrl.TEAMTASK_URL);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mHeaderView.taskSpinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        mHeaderView.taskSpinner.setSelection(index, true);
    }
}
