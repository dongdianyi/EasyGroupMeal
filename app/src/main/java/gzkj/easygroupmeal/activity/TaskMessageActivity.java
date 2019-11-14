package gzkj.easygroupmeal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.MessageAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.CommitParam;
import gzkj.easygroupmeal.bean.Company;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.presenter.Presenter;
import gzkj.easygroupmeal.utli.CompareUtil;
import gzkj.easygroupmeal.utli.DialogCircle;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.view.OnChangeView;
import gzkj.easygroupmeal.view.OnChangeViewB;

public class TaskMessageActivity extends BaseActivity {

    @BindView(R.id.message_recycler)
    LRecyclerView messageRecycler;
    @BindView(R.id.message_linear)
    LinearLayout messageLinear;
    private MessageAdapter messageAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<Company.ResultObjBean> mData;
    private CommitParam commitParam;
    private String sid;
    private StringBuilder messageId;
    private DialogCircle dialogCircle;
    private Dialog dialog;

    @Override
    public int intiLayout() {
        return R.layout.activity_task_message;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
        sid = mShared.getSharedPreference("sid", "").toString();
        mData = new ArrayList<>();
        messageId = new StringBuilder();
        messageAdapter = new MessageAdapter(MyApplication.getContextObject(), R.layout.message_item, "message");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(messageAdapter);
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_14)
                .setColorResource(R.color.line_color)
                .build();
        messageRecycler.addItemDecoration(divider);
        mLRecyclerViewAdapter.addHeaderView(View.inflate(this, R.layout.schedule_top, null));
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        messageRecycler.setLayoutManager(manager);
        messageRecycler.setAdapter(mLRecyclerViewAdapter);
    }

    @Override
    public void setListener() {
        messageRecycler.setLoadMoreEnabled(false);
        messageRecycler.setPullRefreshEnabled(false);
        messageAdapter.setOnChangeListener(new OnChangeView() {
            @Override
            public void onChange(View view, int position, String flag) {
                Intent intent=new Intent(TaskMessageActivity.this,WebActivity.class);
                intent.putExtra("url",mData.get(position).getUrl());
                intent.putExtra("title","详情");
                startActivity(intent);

            }
        });
        messageAdapter.setOnChangeBListener(new OnChangeViewB() {
            @Override
            public void onChange(View view, int position, String flag, boolean isCheck) {
                if (isCheck) {
                   mData.get(position).setCheck(true);
                } else {
                    mData.get(position).setCheck(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        commitParam = new CommitParam();
        body = commitParam.getBody(sid, HttpUrl.MESSAGEINTERFACE, commitParam);
        presenter=new Presenter(this);
        presenter.getData("POST", "消息列表", HttpUrl.MESSAGE_URL);
    }

    @OnClick({R.id.delete_bottom, R.id.all_select, R.id.cancel, R.id.back, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.delete:
                messageLinear.setVisibility(View.VISIBLE);
                messageAdapter.setCurrentItem(true);
                //通知LRecyclerViewAdapter改变状态
                messageAdapter.notifyDataSetChanged();
                break;
            case R.id.delete_bottom:
                deletePop("确定删除吗?");
                break;
            case R.id.all_select:
                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).setCheck(true);
                }
                //通知LRecyclerViewAdapter改变状态
                messageAdapter.notifyDataSetChanged();
                break;
            case R.id.cancel:
                messageLinear.setVisibility(View.GONE);
                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).setCheck(false);
                }
                messageAdapter.setCurrentItem(false);
                //通知LRecyclerViewAdapter改变状态
                messageAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {
        if ("消息删除".equals(flag)) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).isCheck()) {
                    mData.remove(i);
                    i--;
                }
            }
            dialog.dismiss();
            toastShort("删除成功");
            messageAdapter.setDataList(mData);
        }
        if ("消息列表".equals(flag)) {
            Company company = new Gson().fromJson(object, Company.class);
            Collections.sort(company.getResultObj(), CompareUtil.createComparator(-1, "createtime"));
            mData.clear();
            mData.addAll(company.getResultObj());
            messageAdapter.setDataList(mData);

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
                        messageId.delete(0, messageId.length());
                        for (Company.ResultObjBean mDatum : mData) {
                            if (mDatum.isCheck()) {
                                messageId.append(mDatum.getMessage_id() + ",");
                            }
                        }
                        if (messageId.length() == 0) {
                            toastShort("请选择要删除的消息");
                            return;
                        }
                        dialog = DialogUIUtils.showLoading(TaskMessageActivity.this, "正在删除...", false, true, true, true).show();
                        messageId.delete(messageId.lastIndexOf(","), messageId.lastIndexOf(",") + 1);
                        commitParam = new CommitParam();
                        commitParam.setMessageId(messageId.toString());
                        body = commitParam.getBody(sid, HttpUrl.MESSAGEDELINTERFACE, commitParam);
                        presenter=new Presenter(TaskMessageActivity.this);
                        presenter.getData("POST", "消息删除", HttpUrl.MESSAGEDEL_URL);
                    }
                    dialogCircle.dismiss();
                    dialogCircle = null;
                }
            });
        }
    }
}
