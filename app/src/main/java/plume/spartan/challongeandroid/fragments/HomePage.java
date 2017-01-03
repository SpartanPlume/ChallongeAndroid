package plume.spartan.challongeandroid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import plume.spartan.challongeandroid.R;
import plume.spartan.challongeandroid.activities.MainActivity;
import plume.spartan.challongeandroid.adapters.HomePageAdapter;

/**
 * Created by charpe_r on 30/10/16.
 */

public class HomePage extends Fragment {

    private ViewPager viewPager;

    public static HomePage newInstance() {
        return (newInstance(0));
    }

    public static HomePage newInstance(int tab) {
        HomePage homePage = new HomePage();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        homePage.setArguments(bundle);
        return (homePage);
    }

    public static final String TAG = "HomePage";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        getActivity().setTitle(getString(R.string.home_page_title));
        ((MainActivity) getActivity()).setOpenableDrawer(false);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(new HomePageAdapter(getChildFragmentManager()));

        Bundle bundle = getArguments();
        if (bundle != null) {
            viewPager.setCurrentItem(bundle.getInt("tab"));
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("tab", viewPager.getCurrentItem());
        super.onDestroyView();
    }
}
