package tastyorder.tastyorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pptroll on 2017-07-25.
 */

public class Menu_ViewPagerAdapter extends FragmentPagerAdapter{

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<String>();

    public Menu_ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public void addFragment(String businessnumber, String topcategoryname, String categoryname, Fragment fragment) {
        Bundle bundle = new Bundle(3);
        bundle.putString("businessnumber", businessnumber);
        bundle.putString("topcategoryname", topcategoryname);
        bundle.putString("categoryname", categoryname);
        fragment.setArguments(bundle);
        mFragmentList.add(fragment);
        mFragmentTitleList.add(categoryname);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}