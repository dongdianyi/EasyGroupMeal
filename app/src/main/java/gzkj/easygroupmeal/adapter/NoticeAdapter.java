package gzkj.easygroupmeal.adapter;


import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.ListBaseAdapter;
import gzkj.easygroupmeal.base.SuperViewHolder;
import gzkj.easygroupmeal.bean.Date;
import gzkj.easygroupmeal.bean.Notice;
import gzkj.easygroupmeal.httpUtil.HttpUrl;
import gzkj.easygroupmeal.utli.DateUtil;

/**
 * 公告适配器
 */
public class NoticeAdapter extends ListBaseAdapter {

    private Context context;
    private int layout;
    private String flag;


    public NoticeAdapter(Context context, int layoutId, String flag) {
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
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        if ("notice".equals(flag)) {
            Notice.ResultObjBean.NoticesBean notice = (Notice.ResultObjBean.NoticesBean) mDataList.get(position);
            TextView content = holder.getView(R.id.content);
            TextView nameTv = holder.getView(R.id.name_tv);
            TextView positionTv = holder.getView(R.id.position_tv);
            TextView timeTv = holder.getView(R.id.time_tv);
            content.setText(notice.getContent());
            nameTv.setText(notice.getUser_name());
            positionTv.setText(notice.getPost_name());
            timeTv.setText(DateUtil.timeStamp2Date(notice.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
        }
        if ("leave".equals(flag)) {
            Date.ResultObjBean date= (Date.ResultObjBean) mDataList.get(position);
            TextView timeTv = holder.getView(R.id.time_tv);
            TextView causeTv = holder.getView(R.id.reason_tv);
            ImageView leaveIv = holder.getView(R.id.leave_iv);
            timeTv.setText(date.getStartTime()+"一"+date.getEndTime());
            causeTv.setText(date.getCause());
            if (HttpUrl.STATUSZERO.equals(date.getCheck_status())) {
                //审核中
                leaveIv.setImageResource(R.mipmap.checkin);
            }
            if (HttpUrl.STATUSONE.equals(date.getCheck_status())) {
                //同意
                leaveIv.setImageResource(R.mipmap.pass);
            }
            if (HttpUrl.STATUSTWO.equals(date.getCheck_status())) {
                //拒绝
                leaveIv.setImageResource(R.mipmap.nopass);
            }
        }
    }

}
