package gzkj.easygroupmeal.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.ListBaseAdapter;
import gzkj.easygroupmeal.base.SuperViewHolder;
import gzkj.easygroupmeal.bean.Task;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.utli.SharedPreferencesHelper;
import gzkj.easygroupmeal.view.OnChangeView;

/**
 * 工作列表适配器
 */
public class ScheduleAdapter extends ListBaseAdapter {

    private Context context;
    private int layout;
    private String flag;

    //声明接口对象
    private OnChangeView mOnChangeView;
    private  String postType;
    private  SharedPreferencesHelper mShared;


    //设置监听器,实例化接口
    public void setOnChangeListener(OnChangeView onChangeView) {
        mOnChangeView = onChangeView;
    }

    public ScheduleAdapter(Context context, int layoutId, String flag) {
        super(context);
        this.context = context;
        this.layout = layoutId;
        this.flag = flag;
        if ("task".equals(flag)) {
            mShared = new SharedPreferencesHelper(MyApplication.getContextObject(), "userinfo");
            postType = mShared.getSharedPreference("posttype", "").toString();
        }
    }

    @Override
    public int getLayoutId() {
        return layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        if (flag.equals("remind")) {
            String mData = (String) mDataList.get(position);
            TextView tv = holder.getView(R.id.tv);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
            tv.setLayoutParams(lp);
            tv.setText(mData);
        }
        if ("task".equals(flag)) {
            Task.ResultObjBean mData = (Task.ResultObjBean) mDataList.get(position);
            TextView time = holder.getView(R.id.time);
            TextView content = holder.getView(R.id.content);
            TextView iswarnTv = holder.getView(R.id.iswarn_tv);
            ImageView modifyIv = holder.getView(R.id.modify_iv);
            if (!HttpUrl.POSITION[3].equals(postType)) {
                modifyIv.setVisibility(View.VISIBLE);
            }
            if ("1".equals(mData.getIsWarn())) {
                iswarnTv.setBackgroundResource(R.drawable.fillet_red_left);
            } else {
                iswarnTv.setBackgroundResource(R.drawable.fillet_blue_left);
            }
            time.setText(mData.getStartTime() + "-" + mData.getEndTime());
            content.setText(mData.getTaskContent());
            modifyIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v, position, "modify");
                }
            });
        }
        if (flag.equals("员工任务task_completed")) {
            Task.ResultObjBean mData = (Task.ResultObjBean) mDataList.get(position);
            TextView content = holder.getView(R.id.content);
            TextView updateState = holder.getView(R.id.update_state);
            TextView taskTitle = holder.getView(R.id.task_title);
            content.setText(mData.getTaskContent());
            updateState.setVisibility(View.GONE);
            taskTitle.setText("当日已完成任务");
            taskTitle.setTextColor(Color.rgb(56, 125, 254));


        }
        if (flag.equals("员工任务task_no_completed")) {
            Task.ResultObjBean mData = (Task.ResultObjBean) mDataList.get(position);
            TextView content = holder.getView(R.id.content);
            TextView updateState = holder.getView(R.id.update_state);
            TextView taskTitle = holder.getView(R.id.task_title);
            content.setText(mData.getTaskContent());
            updateState.setVisibility(View.GONE);
            taskTitle.setText("当日未完成任务");
            taskTitle.setTextColor(Color.rgb(247, 5, 5));
        }
    }

}
