package gzkj.easygroupmeal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.bean.Cycle;

public class SpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<Cycle> mList;

    public SpinnerAdapter(Context pContext, List pList) {
        this.mContext = pContext;
        mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position );
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }
    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.tv_item, null);
        if (convertView != null) {
            TextView tv = convertView.findViewById(R.id.tv);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setTextColor(Color.BLACK);
            tv.setText(mList.get(position).getCycleName());
        }
        return convertView;
    }

    /**
     * 下拉列表的展示
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.tv_item, null);
        if (convertView != null) {
            TextView tv = convertView.findViewById(R.id.tv);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
            tv.setLayoutParams(lp);
            tv.setTextColor(Color.BLACK);
            tv.setText(mList.get(position).getCycleName());
        }
        return convertView;

    }
}
