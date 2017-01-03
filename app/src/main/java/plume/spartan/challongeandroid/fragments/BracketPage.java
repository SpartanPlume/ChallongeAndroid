package plume.spartan.challongeandroid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import plume.spartan.challongeandroid.R;
import plume.spartan.challongeandroid.activities.MainActivity;
import plume.spartan.challongeandroid.global.MyApplication;
import plume.spartan.challongeandroid.store.Tournament;

/**
 * Created by charpe_r on 02/11/16.
 */

public class BracketPage extends Fragment {

    public static BracketPage newInstance() {
        return (new BracketPage());
    }
    public static final String TAG = "BracketPage";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bracket_page, container, false);

        getActivity().setTitle(getString(R.string.bracket_page_title));
        ((MainActivity) getActivity()).setOpenableDrawer(true);

        Tournament tournament = ((MyApplication) getActivity().getApplicationContext()).getTournament();

        WebView bracketWebView = (WebView) view.findViewById(R.id.bracket_web_view);
        bracketWebView.setWebViewClient(new MyBrowser());
        bracketWebView.getSettings().setLoadsImagesAutomatically(true);
        //bracketWebView.getSettings().setJavaScriptEnabled(true);
        bracketWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        bracketWebView.loadUrl("https://challonge.com/" + tournament.getUrl());

        return view;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
