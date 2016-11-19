package plume.spartan.challongeandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import plume.spartan.challongeandroid.R;
import plume.spartan.challongeandroid.store.Tournament;

/**
 * Created by charpe_r on 01/11/16.
 */

public class TournamentsListAdapter extends BaseAdapter {

    private Context context;
    private List<Tournament> tournamentsList;
    private LayoutInflater inflater;

    public TournamentsListAdapter(Context context, List<Tournament> tournamentsList){
        this.context = context;
        this.tournamentsList = tournamentsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tournamentsList.size();
    }

    @Override
    public Tournament getItem(int position) {
        return tournamentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (getItemId(position) >= tournamentsList.size()) {
            return convertView;
        }

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_tournaments, null);
            holder.tvTournamentName = (TextView) convertView.findViewById(R.id.tournament_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTournamentName.setText(getItem(position).getName());

        return convertView;
    }

    public static class ViewHolder {
        public ImageView ivHead;
        public TextView tvTournamentName;
    }
}