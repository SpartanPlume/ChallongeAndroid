package plume.spartan.challongeandroid.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.net.MalformedURLException;
import java.net.URL;

import plume.spartan.challongeandroid.R;
import plume.spartan.challongeandroid.activities.MainActivity;
import plume.spartan.challongeandroid.async.GetMethod;
import plume.spartan.challongeandroid.global.MyApplication;
import plume.spartan.challongeandroid.helpers.CustomDialog;
import plume.spartan.challongeandroid.helpers.Keyboard;
import plume.spartan.challongeandroid.helpers.ShowFragment;

/**
 * Created by charpe_r on 29/11/16.
 */

public class LogInPage extends Fragment implements GetMethod.GetMethodResponse {

    public static LogInPage newInstance() {
        return (new LogInPage());
    }
    public static final String TAG = "LogInPage";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in_page, container, false);

        getActivity().setTitle(getString(R.string.log_in_page_title));
        ((MainActivity) getActivity()).setOpenableDrawer(false);

        final TextInputEditText etUsername = (TextInputEditText) view.findViewById(R.id.username);
        final TextInputEditText etApiKey = (TextInputEditText) view.findViewById(R.id.api_key);

        Button btApiKey = (Button) view.findViewById(R.id.get_api_key);
        btApiKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyboard.hide(getActivity());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.challonge_url_api_key)));
                startActivity(intent);
            }
        });

        Button btLogIn = (Button) view.findViewById(R.id.log_in);
        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyboard.hide(getActivity());
                String username = etUsername.getText().toString();
                String api_key = etApiKey.getText().toString();
                ((MyApplication) getActivity().getApplicationContext()).setUser(username, api_key);

                URL url = null;
                try {
                    url = new URL(getString(R.string.challonge_default_url) + getString(R.string.challonge_file_extension));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                GetMethod getMethod = new GetMethod(getActivity().getApplicationContext(), LogInPage.this);
                getMethod.execute(url);
            }
        });

        return (view);
    }

    @Override
    public void processFinish(String output, int responseCode) {
        if (responseCode == 401)
            CustomDialog.showDialogLogInError(getContext(), getActivity(), ((MyApplication) getActivity().getApplicationContext()).getCurrentFragmentTag());
        else if (responseCode >= 400)
            CustomDialog.showDialogConnectionError(getContext(), getActivity(), ((MyApplication) getActivity().getApplicationContext()).getCurrentFragmentTag());
        else
            ShowFragment.changeFragment(getActivity(), HomePage.newInstance(), HomePage.TAG);
    }
}