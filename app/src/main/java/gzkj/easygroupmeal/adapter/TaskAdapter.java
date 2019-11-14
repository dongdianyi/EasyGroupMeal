package gzkj.easygroupmeal.adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.ListBaseAdapter;
import gzkj.easygroupmeal.base.SuperViewHolder;
import gzkj.easygroupmeal.bean.Task;
import gzkj.easygroupmeal.view.OnChangeView;
import gzkj.easygroupmeal.view.OnChangeViewB;

/**
 * 用接口回调
 */
public class TaskAdapter extends ListBaseAdapter {

    private Context context;
    private int layout;
    private String flag;

    private SparseBooleanArray mCheckStates=new SparseBooleanArray();

    //声明接口对象
    private OnChangeView mOnChangeView;
    private OnChangeViewB mOnChangeViewB;
    private CheckBox taskCheck;
    private Task.ResultObjBean mData;

    //设置监听器,实例化接口
    public void setOnChangeListener(OnChangeView onChangeView) {
        mOnChangeView = onChangeView;
    }
    public void setOnChangeBListener(OnChangeViewB onChangeView) {
        mOnChangeViewB = onChangeView;
    }
    public TaskAdapter(Context context, int layoutId, String flag) {
        super(context);
        this.context = context;
        this.layout = layoutId;
        this.flag = flag;
        }

    @Override
    public List getDataList() {
        return mDataList;
    }

    @Override
    public int getLayoutId() {
        return layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        mData = (Task.ResultObjBean) mDataList.get(position);
        TextView content=holder.getView(R.id.content);
        //任务模板
        if (flag.equals("general_task")) {
            TextView startTime = holder.getView(R.id.start_time_tv);
            TextView endTime = holder.getView(R.id.end_time_tv);
            taskCheck = holder.getView(R.id.task_checkbox);
            ImageView updateIv = holder.getView(R.id.update_iv);
            startTime.setText(mData.getStartTime());
            endTime.setText(mData.getEndTime());
            content.setText(mData.getTaskContent());
            taskCheck.setTag(position);
            taskCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos= (int) buttonView.getTag();
                    if (isChecked) {
                        mCheckStates.put(pos,true);
                    }else {
                        mCheckStates.delete(pos);
                    }
                    mOnChangeViewB.onChange(buttonView, position, "isCheck",isChecked);
                }
            });
            taskCheck.setChecked(mCheckStates.get(position,false));
            if(taskCheck.isChecked()){
                taskCheck.setChecked(true);
            }else{
                taskCheck.setChecked(false);
            }
            updateIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v, position, "update");
                }
            });
        }
        if (flag.equals("task")) {
            TextView exceptionSubmit = holder.getView(R.id.exception_submit);
            TextView submitNormally = holder.getView(R.id.submit_normally);
            TextView updateState = holder.getView(R.id.update_state);
            TextView noComplete = holder.getView(R.id.no_complete);
            TextView time = holder.getView(R.id.time);
            TextView iswarnTv = holder.getView(R.id.iswarn_tv);
            if ("1".equals(mData.getIsWarn())) {
                iswarnTv.setBackgroundResource(R.drawable.fillet_red_left);
            }else {
                iswarnTv.setBackgroundResource(R.drawable.fillet_blue_left);
            }
            if ("3".equals(mData.getTaskType())) {
                updateState.setVisibility(View.GONE);
                noComplete.setVisibility(View.GONE);
                exceptionSubmit.setVisibility(View.VISIBLE);
                submitNormally.setVisibility(View.VISIBLE);
            }
            if ("2".equals(mData.getTaskType())) {
                updateState.setVisibility(View.VISIBLE);
                noComplete.setVisibility(View.GONE);
                exceptionSubmit.setVisibility(View.GONE);
                submitNormally.setVisibility(View.GONE);
            }
            if ("1".equals(mData.getTaskType())) {
                noComplete.setVisibility(View.VISIBLE);
                updateState.setVisibility(View.GONE);
                exceptionSubmit.setVisibility(View.GONE);
                submitNormally.setVisibility(View.GONE);
            }
            exceptionSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v, position, "exception");
                }
            });
            submitNormally.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v, position, "normally");
                }
            });
            updateState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v, position, "update_state");
                }
            });
            noComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v, position, "no_complete");
                }
            });

            content.setText(mData.getTaskContent());
            time.setText(mData.getStartTime()+"-"+ mData.getEndTime());
        }
        if (flag.equals("个人任务task_completed")) {
            TextView updateState = holder.getView(R.id.update_state);
            TextView taskTitle = holder.getView(R.id.task_title);
            taskTitle.setText("当日已完成任务");
            taskTitle.setTextColor(Color.rgb(56,125,254));
            updateState.setVisibility(View.VISIBLE);
            updateState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v, position, "update_state");
                }
            });
            content.setText(mData.getTaskContent());
        }
        if (flag.equals("个人任务task_no_completed")) {
            TextView updateState = holder.getView(R.id.update_state);
            TextView taskTitle = holder.getView(R.id.task_title);
            taskTitle.setText("当日未完成任务");
            taskTitle.setTextColor(Color.rgb(247,5,5));
            updateState.setVisibility(View.GONE);
            content.setText( mData.getTaskContent());
        }
    }

}
