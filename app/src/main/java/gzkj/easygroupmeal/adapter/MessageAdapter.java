package gzkj.easygroupmeal.adapter;


import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.ListBaseAdapter;
import gzkj.easygroupmeal.base.SuperViewHolder;
import gzkj.easygroupmeal.bean.Company;
import gzkj.easygroupmeal.utli.DateUtil;
import gzkj.easygroupmeal.view.OnChangeView;
import gzkj.easygroupmeal.view.OnChangeViewB;

/**
 * 用接口回调
 */
public class MessageAdapter extends ListBaseAdapter {

    private Context context;
    private int layout;
    private String flag;

    //声明接口对象
    private OnChangeView mOnChangeView;
    private OnChangeViewB mOnChangeViewB;
    private SparseBooleanArray mCheckStates=new SparseBooleanArray();

    //设置监听器,实例化接口
    public void setOnChangeListener(OnChangeView onChangeView) {
        mOnChangeView = onChangeView;
    }
    public void setOnChangeBListener(OnChangeViewB onChangeView) {
        mOnChangeViewB = onChangeView;
    }
    //是否点击编辑按钮
    private boolean isDelete =false;

    public void setCurrentItem(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public MessageAdapter(Context context, int layoutId, String flag) {
        super(context);
        this.context = context;
        this.layout = layoutId;
        this.flag = flag;
    }

    @Override
    public int getLayoutId() {
        return layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        Company.ResultObjBean mData = (Company.ResultObjBean) mDataList.get(position);
        TextView content=holder.getView(R.id.content);
        LinearLayout moreLinear=holder.getView(R.id.more_linear);
        TextView messageTime=holder.getView(R.id.message_time);
        TextView isReadTv=holder.getView(R.id.is_read_tv);
        CheckBox messageCheckbox=holder.getView(R.id.message_checkbox);
        if ("0".equals(mData.getFlag())) {
            isReadTv.setVisibility(View.VISIBLE);
        }else {
            isReadTv.setVisibility(View.GONE);
        }
        content.setText(mData.getTitle());
        messageTime.setText(DateUtil.timeStamp2Date(mData.getCreatetime(),"yyyy-MM-dd HH:mm"));
        if (isDelete) {
            messageCheckbox.setVisibility(View.VISIBLE);
        }else {
            messageCheckbox.setVisibility(View.GONE);
        }
        moreLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnChangeView.onChange(v,position,"详情");
            }
        });
        messageCheckbox.setTag(position);
        if (mData.isCheck()) {
            mCheckStates.put(position,true);
        }else {
            mCheckStates.delete(position);
        }
        messageCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        messageCheckbox.setChecked(mCheckStates.get(position,false));
    }

}
