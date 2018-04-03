package money.paybox.paybox;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import money.paybox.paybox.Fragments.BaseFragment;
import money.paybox.paybox.Fragments.FragmentCard;
import money.paybox.paybox.Fragments.FragmentPaymentA;
import money.paybox.paybox.Fragments.FragmentPaymentB;

/**
 * Created by arman on 19.11.2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;
    Context context;
    public ViewPagerAdapter(Context context, FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.titles)[position];
    }
}
