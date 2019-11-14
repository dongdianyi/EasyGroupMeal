package gzkj.easygroupmeal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.bean.Task;

/**
 * 日历
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Task.ResultObjBean> mDataList = new ArrayList<>();

    public RecyclerViewAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Task.ResultObjBean> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Task.ResultObjBean> list) {
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.schedule_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Task.ResultObjBean listItem = mDataList.get(position);

        ViewHolder viewHolder = (ViewHolder) holder;
        if ("1".equals(listItem.getIsWarn())) {
            viewHolder.iswarnTv.setBackgroundResource(R.drawable.fillet_red_left);
        } else {
            viewHolder.iswarnTv.setBackgroundResource(R.drawable.fillet_blue_left);
        }
        viewHolder.time.setText(listItem.getStartTime() + "-" + listItem.getEndTime());
        viewHolder.content.setText(listItem.getTaskContent());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<Task.ResultObjBean> getDataList() {
        return mDataList;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView time;
        private TextView content;
        private TextView iswarnTv;
        private ImageView modifyIv;

        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            content = itemView.findViewById(R.id.content);
            modifyIv = itemView.findViewById(R.id.modify_iv);
            iswarnTv = itemView.findViewById(R.id.iswarn_tv);
            modifyIv.setVisibility(View.GONE);
        }
    }
}
