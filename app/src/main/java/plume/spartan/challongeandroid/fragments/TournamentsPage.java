package plume.spartan.challongeandroid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import plume.spartan.challongeandroid.R;
import plume.spartan.challongeandroid.activities.MainActivity;
import plume.spartan.challongeandroid.adapters.TournamentsListAdapter;
import plume.spartan.challongeandroid.async.GetMethod;
import plume.spartan.challongeandroid.global.MyApplication;
import plume.spartan.challongeandroid.helpers.DialogConnectionError;
import plume.spartan.challongeandroid.helpers.ShowFragment;
import plume.spartan.challongeandroid.store.Tournament;

/**
 * Created by charpe_r on 19/11/16.
 */

public class TournamentsPage extends Fragment implements GetMethod.GetMethodResponse {

    private List<Tournament> tournamentsList = null;
    private ListView tournamentsListView;

    public static TournamentsPage newInstance() {
        return (newInstance(0));
    }

    public static TournamentsPage newInstance(int tab) {
        TournamentsPage tournamentsPage = new TournamentsPage();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        tournamentsPage.setArguments(bundle);
        return (tournamentsPage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tournaments_page, container, false);

        getActivity().setTitle(getString(R.string.home_page_title));
        ((MainActivity) getActivity()).setOpenableDrawer(false);

        /*Button button = (Button) findViewById(R.id.buttonn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.challonge_url_api_key)));
                startActivity(intent);
            }
        });*/

        List<String> urls = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            int tab = bundle.getInt("tab");
            switch (tab) {
                case 0:
                    urls.add("?state=in_progress");
                    urls.add("?state=pending");
                    break;
                case 1:
                    urls.add("?state=ended");
                    break;
                default:
                    urls.add("?state=all");
            }
        } else {
            urls.add("?state=all");
        }

        tournamentsListView = (ListView) view.findViewById(R.id.tournaments_list);
        tournamentsList = null;

        for (String url_s : urls) {
            URL url = null;
            try {
                url = new URL(getString(R.string.challonge_default_url) + getString(R.string.challonge_file_extension) + url_s);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            GetMethod getMethod = new GetMethod(this, getActivity().getApplicationContext());
            getMethod.execute(url);
        }

        return view;
    }

    @Override
    public void processFinish(String output, boolean connectionError) {
        if (connectionError) {
            DialogConnectionError.show(getContext(), getActivity(), ((MyApplication) getActivity().getApplicationContext()).getCurrentFragmentTag());
        } else if (output != null) {
            System.out.println(output);
            if (tournamentsList == null)
                tournamentsList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(output);
                Tournament tournament;
                int i = jsonArray.length() - 1;
                while (i >= 0) {
                    tournament = new Tournament(jsonArray.getJSONObject(i).getJSONObject(getString(R.string.challonge_get_tournament_object)));
                    tournamentsList.add(tournament);
                    i--;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                DialogConnectionError.show(getContext(), getActivity(), ((MyApplication) getActivity().getApplicationContext()).getCurrentFragmentTag());
            }

            TournamentsListAdapter adapter = new TournamentsListAdapter(getActivity(), tournamentsList);
            tournamentsListView.setAdapter(adapter);
            tournamentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Tournament tournament = tournamentsList.get(i);
                    if (tournament.getUrl() != null) {
                        ((MyApplication) getActivity().getApplicationContext()).setTournament(tournament);
                        ShowFragment.changeFragment(getActivity(), BracketPage.newInstance(), "BracketPage");
                    }
                }
            });
        }
    }
}
