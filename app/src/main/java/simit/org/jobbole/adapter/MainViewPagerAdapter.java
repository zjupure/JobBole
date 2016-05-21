package simit.org.jobbole.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by liuchun on 2016/3/31.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] pageTitles;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainViewPagerAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    public void setFragments(List<Fragment> fragments){
        this.fragments = fragments;
    }

    public void setPageTitles(String[] pageTitles){
        this.pageTitles = pageTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments == null ? null : fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(pageTitles == null || getCount() == 0){
            return "";
        }
        return pageTitles[position];
    }
}
