package gzkj.easygroupmeal.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.adapter.AdministratorsAdapter;
import gzkj.easygroupmeal.app.MyApplication;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.utli.MyGridLayoutManager;

public class AdministrationActivity extends BaseActivity {

    @BindView(R.id.administrators_recycler)
    LRecyclerView administratorsRecycler;
    //管理员列表
    private AdministratorsAdapter mAdministratorsAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<String> mData;

    @Override
    public int intiLayout() {
        return R.layout.activity_administrators;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        mData=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add("公司"+i);
        }
        mAdministratorsAdapter = new AdministratorsAdapter(MyApplication.getContextObject(), R.layout.administrators_item, "administrators");
        mAdministratorsAdapter.setDataList(mData);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdministratorsAdapter);
        MyGridLayoutManager manager = new MyGridLayoutManager(MyApplication.getContextObject(), 1, LinearLayoutManager.VERTICAL, false);
        administratorsRecycler.setLayoutManager(manager);
        administratorsRecycler.setAdapter(mLRecyclerViewAdapter);
    }

    @Override
    public void setListener() {
        administratorsRecycler.setPullRefreshEnabled(false);
        administratorsRecycler.setLoadMoreEnabled(false);
    }

    @OnClick({R.id.back, R.id.add_administrators})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add_administrators:
                Intent intent=new Intent(this,AddAdministratorsActivity.class);
                intent.putExtra("flag",getString(R.string.add_administrators));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {

    }

}
