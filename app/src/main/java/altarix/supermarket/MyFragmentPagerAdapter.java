package altarix.supermarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{

    List<PriceList> priceLists;

    public MyFragmentPagerAdapter(FragmentManager fm, List<PriceList> priceLists,ViewPager viewPager) {
        super(fm);
        this.priceLists = priceLists;
        viewPager.setAdapter(this);
    }


    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        PriceList priceList = priceLists.get(position);
        args.putString(DBHelper.NAME, priceList.getName());
        args.putString(DBHelper.COST, priceList.getCost());
        args.putInt(DBHelper.COUNT,priceList.getCount());

        return  PageFragment.newInstance(args);
    }

    @Override
    public int getCount() {
        return priceLists.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}