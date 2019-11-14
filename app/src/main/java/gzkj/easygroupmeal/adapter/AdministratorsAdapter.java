package gzkj.easygroupmeal.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.ListBaseAdapter;
import gzkj.easygroupmeal.base.SuperViewHolder;
import gzkj.easygroupmeal.bean.MemberJson;
import gzkj.easygroupmeal.utli.GlideLoadUtils;

/**
 * 管理员列表
 */
public class AdministratorsAdapter extends ListBaseAdapter {

    private Context context;
    private int layout;
    private String flag;
    private String name;


    public AdministratorsAdapter(Context context, int layoutId, String flag) {
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
        if (flag.equals("member")) {
            MemberJson memberJson= (MemberJson) mDataList.get(position);
            CircleImageView avatar=holder.getView(R.id.avatar);
            TextView avatarTv=holder.getView(R.id.avatar_tv);
            TextView nameTv=holder.getView(R.id.name_tv);
            name = memberJson.getUserName();
            nameTv.setText(name);
            if ("add".equals(memberJson.getFlag())) {
                avatarTv.setVisibility(View.GONE);
                avatar.setVisibility(View.VISIBLE);
                GlideLoadUtils.getInstance().glideLoad(mContext, "", avatar, R.mipmap.cricle_add);
            }else {
                avatarTv.setVisibility(View.VISIBLE);
                avatar.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(name) &&name.length()>=2) {
                    avatarTv.setText(name.substring(name.length() - 2, name.length()));
                }else {
                    avatarTv.setText(name);
                }
            }
        }else {
            String mData = (String) mDataList.get(position);
            TextView administartorsName = holder.getView(R.id.administartors_name);
            TextView administartorsPosition = holder.getView(R.id.administartors_position);
            TextView administartorsPhone = holder.getView(R.id.administartors_phone);
            TextView delete = holder.getView(R.id.delete);
            administartorsName.setText(mData);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(position);
                }
            });
        }
    }

}
