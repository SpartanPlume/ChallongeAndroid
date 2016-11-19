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

/**
 * Created by charpe_r on 31/10/16.
 */

public class NavDrawerAdapter extends BaseAdapter {

    private List<String> navDrawerItemList;
    private LayoutInflater inflater;

    public NavDrawerAdapter(Context context, List<String> navDrawerItems){
        this.navDrawerItemList = navDrawerItems;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return navDrawerItemList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return 0;
        } else {
            return position - 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int row = getItemViewType(position);
        if (getItemId(position) >= navDrawerItemList.size()) {
            return convertView;
        }

        if (convertView == null) {
            holder = new ViewHolder();
            if (row == 0) {
                convertView = inflater.inflate(R.layout.list_item_nav_drawer_head, null);
                holder.ivHead = (ImageView) convertView.findViewById(R.id.nav_drawer_head);
                convertView.setTag(holder);
            } else {
                convertView = inflater.inflate(R.layout.list_item_nav_drawer, null);
                holder.tvItem = (TextView) convertView.findViewById(R.id.nav_drawer_item);
                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (row != 0) {
            holder.tvItem.setText(navDrawerItemList.get(position - 1));
        }

        return convertView;
    }

    public static class ViewHolder {
        public ImageView ivHead;
        public TextView tvItem;
    }
}
