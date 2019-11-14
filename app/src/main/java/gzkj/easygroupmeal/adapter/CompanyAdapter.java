package gzkj.easygroupmeal.adapter;


import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.meetsl.scardview.SCardView;

import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.ListBaseAdapter;
import gzkj.easygroupmeal.base.SuperViewHolder;
import gzkj.easygroupmeal.bean.Company;

/**
 * 加入公司搜索公司列表
 */
public class CompanyAdapter extends ListBaseAdapter {

    private Context context;
    private int layout;
    private String flag;

    //当前Item被点击的位置
    private int currentItem = -1;

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    public CompanyAdapter(Context context, int layoutId, String flag) {
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
        TextView companyTv = holder.getView(R.id.company_tv);
        final SCardView companyCard = holder.getView(R.id.company_cardview);
        if ("search_company".equals(flag)) {
            companyTv.setText(mData.getCompanyName());
        }
        if ("search_team".equals(flag)){
            companyTv.setText(mData.getTeamName());
        }
        if ("search_school".equals(flag)){
            companyTv.setText(mData.getSchoolZone());
        }
        if (currentItem == position) { //如果被点击，设置当前TextView被选中
            companyCard.setSelected(true);
            companyTv.setTextColor(Color.WHITE);
        } else { //如果没有被点击，设置当前TextView未被选中
            companyCard.setSelected(false);
            companyTv.setTextColor(Color.rgb(59,59,59));
        }
    }

}
