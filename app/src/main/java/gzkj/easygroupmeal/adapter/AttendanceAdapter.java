package gzkj.easygroupmeal.adapter;


import android.content.Context;
import android.widget.TextView;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.ListBaseAdapter;
import gzkj.easygroupmeal.base.SuperViewHolder;
import gzkj.easygroupmeal.bean.Cycle;

/**
 * 团队餐厅列表适配器
 */
public class AttendanceAdapter extends ListBaseAdapter {

    private Context context;
    private int layout;
    private String flag;


    public AttendanceAdapter(Context context, int layoutId, String flag) {
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
        Cycle mData = (Cycle) mDataList.get(position);
        TextView nameTv=holder.getView(R.id.team_name);
        nameTv.setText(mData.getCycleName());
    }

}
