package plume.spartan.challongeandroid.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import plume.spartan.challongeandroid.R;

/**
 * Created by charpe_r on 03/01/17.
 */

public class CustomDialog {

    static public void showDialogConnectionError(final Context context, final FragmentActivity fragmentActivity, final String tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
        builder.setTitle(context.getString(R.string.connection_error));
        builder.setMessage(context.getString(R.string.connection_error_message));
        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ReloadCurrentFragment.execute(fragmentActivity, tag);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    static public void showDialogLogInError(final Context context, final FragmentActivity fragmentActivity, final String tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
        builder.setTitle(context.getString(R.string.log_in_error));
        builder.setMessage(context.getString(R.string.log_in_error_message));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ReloadCurrentFragment.execute(fragmentActivity, tag);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
