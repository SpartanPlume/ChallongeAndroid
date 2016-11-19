package plume.spartan.challongeandroid.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import plume.spartan.challongeandroid.R;
import plume.spartan.challongeandroid.global.MyApplication;

/**
 * Created by charpe_r on 31/10/16.
 */

public class ShowFragment {

    public static void changeFragment(final FragmentActivity activity, final Fragment fragment, final String tag) {
        if (fragment == null)
            return;
        final FragmentManager fm = activity.getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.content_frame, fragment, tag);
        ft.addToBackStack(null);
        ft.commit();
        ((MyApplication) activity.getApplicationContext()).setCurrentFragmentTag(tag);
    }
}