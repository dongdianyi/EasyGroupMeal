package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.SpinnerAdapter;
import gzkj.easygroupmeal.adapter.TaskAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Cycle;
import gzkj.easygroupmeal.bean.ShowTask;
import gzkj.easygroupmeal.bean.Task;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.utli.SingleClick;
import gzkj.easygroupmeal.view.OnChangeView;
import gzkj.easygroupmeal.view.OnChangeViewB;

public class GeneralTaskActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.cycle_ed)
    EditText cycleEd;
    @BindView(R.id.task_recycler)
    LRecyclerView taskRecycler;
    @BindView(R.id.select_staff_spinner)
    AppCompatSpinner selectStaffSpinner;
    @BindView(R.id.select_task_spinner)
    AppCompatSpinner selectTaskSpinner;
    @BindView(R.id.remind_spinner)
    AppCompatSpinner remindSpinner;
    private List<Cycle> mDataStaff, mDataTask;
    private List<Task.ResultObjBean> mDataTemplateTask, mDataTemplateTaskJson;
    private TaskAdapter mTaskAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private CommitParam commitParam;
    private String sid;
    private int updatePosition;
    private String receptId,taskCycle,taskRemind="0";
    private List<Cycle> mData;

    @Override
    public int intiLayout() {
        return R.layout.activity_general_task;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();

        mData = new ArrayList<>();
        mData.add(new Cycle("开始时提醒",""));
        mData.add(new Cycle("5分钟前","5"));
        mData.add(new Cycle("20分钟前","20"));
        mData.add(new Cycle("1小时前","60"));
        mData.add(new Cycle("1天前","1440"));
        mData.add(new Cycle("2天前","2880"));
        remindSpinner.setAdapter(new SpinnerAdapter(this, mData));


        mDataStaff = new ArrayList<>();
        mDataTask = new ArrayList<>();
        mDataTemplateTask = new ArrayList<>();
        mDataTemplateTaskJson = new ArrayList<>();

        mTaskAdapter = new TaskAdapter(MyApplication.getContextObject(), R.layout.task_item, "general_task");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mTaskAdapter);
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_14)
                .setColorResource(R.color.white)
                .build();
        taskRecycler.addItemDecoration(divider);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        taskRecycler.setLayoutManager(manager);
        taskRecycler.setAdapter(mLRecyclerViewAdapter);
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.SHOWTASKINTERFACE, commitParam);
        presenter=new Presenter(this);
        presenter.getData("POST", "派发任务员工及一级任务展示", HttpUrl.SHOWTASK_URL);
    }

    @Override
    public void setListener() {
        taskRecycler.setPullRefreshEnabled(false);
        taskRecycler.setLoadMoreEnabled(false);
        selectTaskSpinner.setOnItemSelectedListener(this);
        selectStaffSpinner.setOnItemSelectedListener(this);
        remindSpinner.setOnItemSelectedListener(this);
        mTaskAdapter.setOnChangeListener(new OnChangeView() {
            @Override
            public void onChange(View view, int position, String flag) {
                if ("update".equals(flag)) {
                    updatePosition = position;
                    Intent intent = new Intent(GeneralTaskActivity.this, UpdateTaskActivity.class);
                    intent.putExtra("task",mDataTemplateTask.get(position));
                    intent.putExtra("flag","general");
                    startActivity(intent);
                }
            }
        });
        mTaskAdapter.setOnChangeBListener(new OnChangeViewB() {
            @Override
            public void onChange(View view, int position, String flag, boolean isCheck) {
                if ("isCheck".equals(flag)) {
                    if (isCheck) {
                        if (!mDataTemplateTaskJson.contains(mDataTemplateTask.get(position))) {
                            mDataTemplateTaskJson.add(mDataTemplateTask.get(position));
                        }
                    } else {
                        mDataTemplateTaskJson.remove(mDataTemplateTask.get(position));
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().removeStickyEvent(Cycle.class);
            EventBus.getDefault().removeStickyEvent(Task.ResultObjBean.class);
            EventBus.getDefault().unregister(this);
        }
    }
    @SingleClick
    @OnClick({R.id.back, R.id.sure, R.id.cycle_iv, R.id.cycle_ed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                if (receptId==null) {
                    toastShort("请选择员工");
                    return;
                }
                if (taskCycle==null) {
                    toastShort("请选择周期");
                    return;
                }
                if (mDataTemplateTaskJson.size()==0) {
                    toastShort("请选择任务");
                    return;
                }
                commitParam = new CommitParam();
                commitParam.setReceptId(receptId);
                commitParam.setTaskCycle(taskCycle);
                commitParam.setTaskRemind(taskRemind);
                commitParam.setTaskJson(new Gson().toJson(mDataTemplateTaskJson));
                body = commitParam.getBody(sid, HttpUrl.GENERALTASKINTERFACE, commitParam);
                presenter=new Presenter(this);
                presenter.getData("POST", "派发任务", HttpUrl.GENERALTASK_URL);
                break;
            case R.id.cycle_iv:
            case R.id.cycle_ed:
                Intent intent = new Intent(this, AttendanceDateActivity.class);
                intent.putExtra("title", "选择周期");
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void Event(Cycle cycle) {
        cycleEd.setText(cycle.getCycleName());
        taskCycle=cycle.getCycleNum();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void Event(Task.ResultObjBean resultObjBean) {
        Task.ResultObjBean result = (Task.ResultObjBean) mTaskAdapter.getDataList().get(updatePosition);
        result.setStartTime(resultObjBean.getStartTime());
        result.setEndTime(resultObjBean.getEndTime());
        result.setTaskContent(resultObjBean.getTaskContent());
        mTaskAdapter.getDataList().set(updatePosition, result);
        mLRecyclerViewAdapter.notifyItemChanged(mLRecyclerViewAdapter.getAdapterPosition(false, updatePosition));

    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("派发任务员工及一级任务展示".equals(flag)) {
            ShowTask showTask = new Gson().fromJson(object, ShowTask.class);
            for (ShowTask.ResultObjBean.StaffListBean staffListBean : showTask.getResultObj().getStaffList()) {
                mDataStaff.add(new Cycle(staffListBean.getUserName(), staffListBean.getUserId()));
            }
            for (ShowTask.ResultObjBean.TaskListBean taskListBean : showTask.getResultObj().getTaskList()) {
                mDataTask.add(new Cycle(taskListBean.getTaskName(), taskListBean.getTaskId() + ""));
            }
            if (mDataStaff.size()>0) {
                receptId=mDataStaff.get(0).getCycleNum();
            }
            selectTaskSpinner.setAdapter(new SpinnerAdapter(this, mDataTask));
            selectStaffSpinner.setAdapter(new SpinnerAdapter(this, mDataStaff));

        }
        if ("任务模板".equals(flag)) {
            Task templateTask = new Gson().fromJson(object, Task.class);
            mDataTemplateTask.clear();
            mDataTemplateTask.addAll(templateTask.getResultObj());
            mTaskAdapter.setDataList(templateTask.getResultObj());
        }
        if ("派发任务".equals(flag)) {
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.remind_spinner:
                taskRemind=mData.get(position).getCycleNum();
                break;
            case R.id.select_staff_spinner:
                receptId=mDataStaff.get(position).getCycleNum();
                break;
            case R.id.select_task_spinner:
                commitParam = new CommitParam();
                commitParam.setTaskId(mDataTask.get(position).getCycleNum() + "");
                body = commitParam.getBody(sid, HttpUrl.TEMPLATETASKINTERFACE, commitParam);
                presenter=new Presenter(this);
                presenter.getData("POST", "任务模板", HttpUrl.TEMPLATETASK_URL);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
