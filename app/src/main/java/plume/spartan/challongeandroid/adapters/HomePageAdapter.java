package plume.spartan.challongeandroid.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import plume.spartan.challongeandroid.fragments.HomePage;
import plume.spartan.challongeandroid.fragments.TournamentsPage;

/**
 * Created by charpe_r on 19/11/16.
 */

public class HomePageAdapter extends FragmentPagerAdapter {

    public HomePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return (TournamentsPage.newInstance(position));
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "CURRENT";
            case 1:
                return "COMPLETED";
            default:
                return "ALL";
        }
    }
}
