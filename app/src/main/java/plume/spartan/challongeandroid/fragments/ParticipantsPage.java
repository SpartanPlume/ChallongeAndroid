package plume.spartan.challongeandroid.fragments;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import plume.spartan.challongeandroid.R;
import plume.spartan.challongeandroid.activities.MainActivity;
import plume.spartan.challongeandroid.adapters.ParticipantsListAdapter;
import plume.spartan.challongeandroid.async.GetMethod;
import plume.spartan.challongeandroid.async.PushMethod;
import plume.spartan.challongeandroid.components.DynamicListView;
import plume.spartan.challongeandroid.global.MyApplication;
import plume.spartan.challongeandroid.helpers.CustomDialog;
import plume.spartan.challongeandroid.helpers.Keyboard;
import plume.spartan.challongeandroid.store.Participant;
import plume.spartan.challongeandroid.store.Tournament;

/**
 * Created by charpe_r on 05/11/16.
 */

public class ParticipantsPage extends Fragment implements GetMethod.GetMethodResponse, ParticipantsListAdapter.RemoveParticipant {

    private final int UPDATE_TIMER = 10000;
    private DynamicListView participantsListView;
    private Handler handler;
    private Runnable runnable;
    private Button btNewParticipant;
    private EditText etNewParticipant;

    public static ParticipantsPage newInstance() {
        return (new ParticipantsPage());
    }
    public static final String TAG = "ParticipantsPage";

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participants_page, container, false);

        getActivity().setTitle(getString(R.string.participants_page_title));
        ((MainActivity) getActivity()).setOpenableDrawer(true);

        Tournament tournament = ((MyApplication) getActivity().getApplicationContext()).getTournament();

        URL url_tmp = null;
        try {
            url_tmp = new URL(getString(R.string.challonge_default_url) + "/" + tournament.getUrl() + getString(R.string.challonge_participants_extension) + getString(R.string.challonge_file_extension));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final URL url = url_tmp;

        etNewParticipant = (EditText) view.findViewById(R.id.new_participant_name);

        btNewParticipant = (Button) view.findViewById(R.id.new_participant_bt);
        btNewParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                String name = etNewParticipant.getText().toString();
                List<Participant> participantList = (List<Participant>)(List<?>)participantsListView.getCheeseList();
                if (name.equals("") || containsName(participantList, name)) {
                    handler.postDelayed(runnable, UPDATE_TIMER);
                    return;
                }
                Keyboard.hide(getActivity());
                etNewParticipant.setText("");
                Participant newParticipant = new Participant();
                newParticipant.setName(name);
                newParticipant.setSeed(participantList.size() + 1);
                participantsListView.addItemInCheeseList(newParticipant);
                ParticipantsListAdapter adapter = new ParticipantsListAdapter(ParticipantsPage.this, getActivity(), participantsListView, (List<Participant>)(List<?>)participantsListView.getCheeseList());
                participantsListView.setAdapter(adapter);
                Map<String, String> parameters = new HashMap<>();
                parameters.put("participant[name]", newParticipant.getName());
                parameters.put("participant[seed]", String.valueOf(newParticipant.getSeed()));
                PushMethod postMethod = new PushMethod(getActivity().getApplicationContext(), PushMethod.POST, parameters);
                postMethod.execute(url);
                handler.postDelayed(runnable, UPDATE_TIMER);
            }
        });
        btNewParticipant.setEnabled(false);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (((MyApplication) getActivity().getApplicationContext()).getCurrentFragmentTag().equals(ParticipantsPage.TAG)) {
                    ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            if (appProcess.processName.equals(getActivity().getPackageName())) {
                                KeyguardManager myKM = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);
                                if (myKM.inKeyguardRestrictedInputMode()) {
                                    //it is locked
                                } else {
                                    GetMethod getMethod = new GetMethod(getActivity().getApplicationContext(), ParticipantsPage.this);
                                    getMethod.execute(url);
                                    //System.out.println("yee");
                                }
                            }
                        }
                    }
                    if (((MyApplication) getActivity().getApplicationContext()).getCurrentFragmentTag().equals(ParticipantsPage.TAG))
                        handler.postDelayed(this, UPDATE_TIMER);
                }
            }
        };
        handler.postDelayed(runnable, UPDATE_TIMER);

        participantsListView = (DynamicListView) view.findViewById(R.id.participants_list);

        if (((MyApplication) getActivity().getApplicationContext()).getTournament().isParticipantsLocked()) {
            etNewParticipant.setVisibility(View.GONE);
            btNewParticipant.setVisibility(View.GONE);
        }

        GetMethod getMethod = new GetMethod(getActivity().getApplicationContext(), this);
        getMethod.execute(url);

        return view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void processFinish(String output, int responseCode) {
        if (responseCode >= 400) {
            CustomDialog.showDialogConnectionError(getContext(), getActivity(), ((MyApplication) getActivity().getApplicationContext()).getCurrentFragmentTag());
        } else if (output != null) {
            final List<Participant> list = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(output);
                Participant participant;
                int i = jsonArray.length() - 1;
                while (i >= 0) {
                    participant = new Participant(jsonArray.getJSONObject(i).getJSONObject(getString(R.string.challonge_get_participant_object)));
                    list.add(participant);
                    i--;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                CustomDialog.showDialogConnectionError(getContext(), getActivity(), ((MyApplication) getActivity().getApplicationContext()).getCurrentFragmentTag());
            }

            List<Participant> previous_list = (List<Participant>)(List<?>)participantsListView.getCheeseList();
            if (previous_list != null && previous_list.equals(list))
                return;

            Collections.sort(list, new Comparator<Participant>() {
                @Override
                public int compare(Participant participant, Participant t1) {
                    return (participant.getSeed() - t1.getSeed());
                }
            });

            participantsListView.setCheeseList((List<Object>)(List<?>)list);
            ParticipantsListAdapter adapter = new ParticipantsListAdapter(this, getActivity(), participantsListView, list);
            participantsListView.setAdapter(adapter);

            btNewParticipant.setEnabled(true);
        }
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(runnable);
        super.onDestroyView();
    }

    private boolean containsName(List<Participant> participantList, String name) {
        for (Participant p : participantList) {
            if (p != null && p.getName().equals(name))
                return (true);
        }
        return (false);
    }

    @Override
    public void processBegin() {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void processFinish() {
        handler.postDelayed(runnable, UPDATE_TIMER);
    }
}
