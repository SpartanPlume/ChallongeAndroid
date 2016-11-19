package plume.spartan.challongeandroid.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import plume.spartan.challongeandroid.R;
import plume.spartan.challongeandroid.async.PushMethod;
import plume.spartan.challongeandroid.components.DynamicListView;
import plume.spartan.challongeandroid.global.MyApplication;
import plume.spartan.challongeandroid.helpers.Keyboard;
import plume.spartan.challongeandroid.store.Participant;

/**
 * Created by charpe_r on 06/11/16.
 */

public class ParticipantsListAdapter extends DynamicListViewAdapter {

    public interface RemoveParticipant {
        void processBegin();
        void processFinish();
    }

    private DynamicListView listView;
    private RemoveParticipant delegate;
    private int editPosition = -1;

    @SuppressWarnings("unchecked")
    public ParticipantsListAdapter(RemoveParticipant delegate, Context context, DynamicListView listView, List<Participant> participants) {
        super(context, (List<Object>)(List<?>)participants);
        this.delegate = delegate;
        this.listView = listView;
    }

    @Override
    public void notifyDataSetChanged(List<Object> newList, int pos) {
        newList = updateSeedInList(newList);
        super.notifyDataSetChanged(newList, pos);
    }

    @Override
    public void beginCellMove() {
        delegate.processBegin();
    }

    @Override
    public void endCellMove(int pos) {
        URL url = null;
        if (pos < 0 || posSwitchView < 0)
            return;
        try {
            url = new URL(context.getString(R.string.challonge_default_url) + "/" + ((MyApplication) context.getApplicationContext()).getTournament().getUrl() + context.getString(R.string.challonge_participants_extension) + "/" + String.valueOf(((Participant) objectList.get(pos - 1)).getId()) + context.getString(R.string.challonge_file_extension));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put("participant[seed]", String.valueOf(((Participant) objectList.get(posSwitchView)).getSeed()));
        PushMethod putMethod = new PushMethod(context.getApplicationContext(), PushMethod.PUT, parameters);
        putMethod.execute(url);
        posSwitchView = -1;
        delegate.processFinish();
    }

    @SuppressWarnings("unchecked")
    private List<Object> updateSeedInList(List<Object> newList) {
        List<Participant> participants = (List<Participant>)(List<?>) newList;
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).setSeed(i + 1);
            //mIdMap.put(participants.get(i), i + 1);
        }
        return ((List<Object>)(List<?>) participants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        listView = (DynamicListView) parent;
        final ViewHolder holder;
        if (getItemId(position) - 1 >= getCount()) {
            return convertView;
        }

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_participants, null);
            holder.seed = -1;
            holder.tvParticipantSeed = (TextView) convertView.findViewById(R.id.participant_seed);
            holder.tvParticipantName = (TextView) convertView.findViewById(R.id.participant_name);
            holder.etParticipantName = (EditText) convertView.findViewById(R.id.participant_name_edit);
            holder.etParticipantName.setVisibility(View.GONE);
            holder.btEdit = (ImageButton) convertView.findViewById(R.id.participant_edit_button);
            holder.btEdit.setOnClickListener(editParticipant);
            holder.btEdit.setTag(holder);
            holder.btRemove = (ImageButton) convertView.findViewById(R.id.participant_remove_button);
            holder.btRemove.setOnClickListener(removeParticipant);
            holder.btRemove.setTag(holder);
            holder.btSave = (Button) convertView.findViewById(R.id.participant_save_button);
            holder.btSave.setOnClickListener(saveParticipant);
            holder.btSave.setTag(holder);
            holder.btSave.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.seed = ((Participant) getItem(position)).getSeed();
        holder.tvParticipantSeed.setText(String.valueOf(holder.seed) + context.getString(R.string.participant_seed_separator));
        holder.tvParticipantName.setText(((Participant) getItem(position)).getName());
        holder.btEdit.setFocusable(false);
        holder.btRemove.setFocusable(false);
        if (((MyApplication) context.getApplicationContext()).getTournament().isParticipantsLocked()) {
            holder.btRemove.setVisibility(View.GONE);
        }
        if (posSwitchView == position) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            convertView.setVisibility(View.VISIBLE);
        }

        /*if (editPosition == position) {
            showItemToEditItem(holder);
        }*/

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvParticipantSeed;
        public TextView tvParticipantName;
        public ImageButton btEdit;
        public ImageButton btRemove;

        public EditText etParticipantName;
        public Button btSave;

        public int seed;
    }

    private View.OnClickListener editParticipant = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            delegate.processBegin();
            final ViewHolder holder = (ViewHolder) view.getTag();

            editPosition = holder.seed - 1;
            showItemToEditItem(holder);
        }
    };

    private View.OnClickListener removeParticipant = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            delegate.processBegin();
            ViewHolder holder = (ViewHolder) view.getTag();

            /*try {
                URL url = new URL(context.getString(R.string.challonge_default_url) + "/" + ((MyApplication) context.getApplicationContext()).getTournament().getUrl() + context.getString(R.string.challonge_file_extension));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            GetMethod getMethod = new GetMethod(context.getApplicationContext());*/

            String name = holder.tvParticipantName.getText().toString();
            Object participantToRemove = null;
            for (Object o : objectList) {
                if (((Participant) o).getName().equals(name)) {
                    participantToRemove = o;
                    break;
                }
            }
            if (participantToRemove != null) {
                objectList.remove(participantToRemove);
                mIdMap.remove(participantToRemove);
                objectList = updateSeedInList(objectList);
                listView.setCheeseList(objectList);
                notifyDataSetChanged();
                URL url = null;
                try {
                    url = new URL(context.getString(R.string.challonge_default_url) + "/" + ((MyApplication) context.getApplicationContext()).getTournament().getUrl() + context.getString(R.string.challonge_participants_extension) + "/" + String.valueOf(((Participant) participantToRemove).getId()) + context.getString(R.string.challonge_file_extension));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                PushMethod deleteMethod = new PushMethod(context.getApplicationContext(), PushMethod.DELETE);
                deleteMethod.execute(url);
            }
            delegate.processFinish();
        }
    };

    private View.OnClickListener saveParticipant = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();

            String name = holder.tvParticipantName.getText().toString();
            Object participantToRemove = null;
            for (Object o : objectList) {
                if (((Participant) o).getName().equals(name)) {
                    participantToRemove = o;
                    break;
                }
            }

            holder.tvParticipantName.setText(holder.etParticipantName.getText());
            editItemToShowItem(holder);

            name = holder.etParticipantName.getText().toString();

            if (participantToRemove != null) {
                objectList.remove(participantToRemove);
                Integer id = mIdMap.get(participantToRemove);
                mIdMap.remove(participantToRemove);
                ((Participant) participantToRemove).setName(name);
                objectList.add(((Participant) participantToRemove).getSeed() - 1, participantToRemove);
                mIdMap.put(participantToRemove, id);
                listView.setCheeseList(objectList);
                notifyDataSetChanged();

                URL url = null;
                try {
                    url = new URL(context.getString(R.string.challonge_default_url) + "/" + ((MyApplication) context.getApplicationContext()).getTournament().getUrl() + context.getString(R.string.challonge_participants_extension) + "/" + String.valueOf(((Participant) participantToRemove).getId()) + context.getString(R.string.challonge_file_extension));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Map<String, String> parameters = new HashMap<>();
                parameters.put("participant[name]", name);
                PushMethod putMethod = new PushMethod(context.getApplicationContext(), PushMethod.PUT, parameters);
                putMethod.execute(url);
            }
        }
    };

    private void editItemToShowItem(ViewHolder holder) {
        holder.tvParticipantName.setVisibility(View.VISIBLE);
        holder.btRemove.setVisibility(View.VISIBLE);
        holder.btEdit.setVisibility(View.VISIBLE);
        holder.etParticipantName.setVisibility(View.GONE);
        holder.btSave.setVisibility(View.GONE);
        Keyboard.hide((Activity) context);
        listView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }

    private void showItemToEditItem(final ViewHolder holder) {
        holder.etParticipantName.setText(holder.tvParticipantName.getText());
        holder.tvParticipantName.setVisibility(View.GONE);
        holder.btRemove.setVisibility(View.GONE);
        holder.btEdit.setVisibility(View.GONE);
        holder.etParticipantName.setVisibility(View.VISIBLE);
        holder.btSave.setVisibility(View.VISIBLE);
        listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        holder.etParticipantName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    view.requestFocus();
                    //editItemToShowItem(holder);
                } else {
                    Keyboard.show(context, holder.etParticipantName);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != editPosition) {
                    listView.setOnItemClickListener(null);
                    editItemToShowItem(holder);
                }
            }
        });
        holder.etParticipantName.requestFocus();
    }
}
