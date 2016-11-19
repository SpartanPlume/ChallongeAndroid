package plume.spartan.challongeandroid.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by charpe_r on 05/11/16.
 */

public class ReloadCurrentFragment {

    static public void execute(final FragmentActivity fragmentActivity, final String tag)
    {
        Fragment fragment;
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
        final FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    static public void execute(final FragmentManager fragmentManager, final String tag)
    {
        Fragment fragment;
        fragment = fragmentManager.findFragmentByTag(tag);
        final FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }
}
