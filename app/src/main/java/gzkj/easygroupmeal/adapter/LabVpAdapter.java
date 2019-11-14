package gzkj.easygroupmeal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import gzkj.easygroupmeal.fragment.TaskTwoFragment;

/**
 * Created by Administrator on 2017/4/20.
 */

public class LabVpAdapter extends FragmentPagerAdapter {
    private List<String> mTitles;
    private String flag;
    public LabVpAdapter(FragmentManager fm, List<String> titles, String flag) {
        super(fm);
        mTitles = titles;
        this.flag = flag;
    }

    @Override
    public Fragment getItem(int position) {
        if (flag.equals("task")) {
            return TaskTwoFragment.newInstance(mTitles.get(position), flag);
        }
        return null;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
