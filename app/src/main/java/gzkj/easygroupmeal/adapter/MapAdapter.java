package gzkj.easygroupmeal.adapter;


import android.content.Context;
import android.widget.TextView;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.ListBaseAdapter;
import gzkj.easygroupmeal.base.SuperViewHolder;
import gzkj.easygroupmeal.bean.MapData;

/**
 * 日程工作列表适配器
 */
public class MapAdapter extends ListBaseAdapter {

    private Context context;
    private int layout;
    private String flag;


    public MapAdapter(Context context, int layoutId, String flag) {
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
        MapData mData = (MapData) mDataList.get(position);
        TextView nameTv=holder.getView(R.id.name_tv);
        TextView addressTv=holder.getView(R.id.address_tv);
        nameTv.setText(mData.getTitle());
        addressTv.setText(mData.getSnippet());
    }

}
