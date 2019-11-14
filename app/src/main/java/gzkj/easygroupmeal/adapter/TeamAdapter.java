package gzkj.easygroupmeal.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.ListBaseAdapter;
import gzkj.easygroupmeal.base.SuperViewHolder;
import gzkj.easygroupmeal.bean.Company;
import gzkj.easygroupmeal.bean.Date;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.utli.GlideLoadUtils;
import gzkj.easygroupmeal.view.OnChangeView;

/**
 * 加入公司搜索公司列表
 */
public class TeamAdapter extends ListBaseAdapter {

    private Context context;
    private int layout;
    private String flag;
    private String name;

    //声明接口对象
    private OnChangeView mOnChangeView;
    //设置监听器,实例化接口
    public void setOnChangeListener(OnChangeView onChangeView) {
        mOnChangeView = onChangeView;
    }


    public TeamAdapter(Context context, int layoutId, String flag) {
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
        if ("apply".equals(flag)) {
            Company.ResultObjBean company= (Company.ResultObjBean) mDataList.get(position);
            TextView contentTv=holder.getView(R.id.content_tv);
            TextView agreeTv=holder.getView(R.id.agree_tv);
            TextView refuseTv=holder.getView(R.id.refuse_tv);
            contentTv.setText(company.getVerfityName());
            agreeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v,position,"1");
                }
            });
            refuseTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v,position,"2");
                }
            });

        }
        if ("team".equals(flag)) {
            Company.ResultObjBean mData = (Company.ResultObjBean) mDataList.get(position);
            TextView nameTv = holder.getView(R.id.name_tv);
            TextView positionTv = holder.getView(R.id.position_tv);
            CircleImageView avatar = holder.getView(R.id.avatar);
            TextView avatarTv = holder.getView(R.id.avatar_tv);
            name = mData.getUserName();
            if (mData.getHead() != null && !mData.getHead().equals("")) {
                avatarTv.setVisibility(View.GONE);
                avatar.setVisibility(View.VISIBLE);
                GlideLoadUtils.getInstance().glideLoad(context, mData.getHead(), avatar, R.mipmap.photo);
            } else {
                avatar.setVisibility(View.GONE);
                avatarTv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(name) && name.length() >= 2) {
                    avatarTv.setText(name.substring(name.length() - 2, name.length()));
                } else {
                    avatarTv.setText(name);
                }
            }
            nameTv.setText(name);
            positionTv.setText(mData.getPostName());
        }
        if ("leave".equals(flag)) {
            Date.ResultObjBean date= (Date.ResultObjBean) mDataList.get(position);
            TextView nameTv=holder.getView(R.id.name_tv);
            TextView causeTv=holder.getView(R.id.reason_tv);
            TextView timeTv=holder.getView(R.id.time_tv);
            TextView agreeTv=holder.getView(R.id.agree_tv);
            TextView refuseTv=holder.getView(R.id.refuse_tv);
            TextView leaveTv=holder.getView(R.id.leave_tv);
            nameTv.setText(date.getUserName());
            timeTv.setText(date.getStartTime()+"一"+date.getEndTime());
            causeTv.setText(date.getCause());
            if (HttpUrl.STATUSONE.equals(date.getCheck_status())) {
                leaveTv.setVisibility(View.VISIBLE);
                agreeTv.setVisibility(View.GONE);
                refuseTv.setVisibility(View.GONE);
                leaveTv.setText("已同意");
            } else if (HttpUrl.STATUSTWO.equals(date.getCheck_status())) {
                leaveTv.setVisibility(View.VISIBLE);
                agreeTv.setVisibility(View.GONE);
                refuseTv.setVisibility(View.GONE);
                leaveTv.setText("已拒绝");
            }else {
                leaveTv.setVisibility(View.GONE);
                agreeTv.setVisibility(View.VISIBLE);
                refuseTv.setVisibility(View.VISIBLE);
            }
            agreeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v,position,HttpUrl.STATUSONE);
                }
            });
            refuseTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChangeView.onChange(v,position,HttpUrl.STATUSTWO);
                }
            });
        }
    }

}
