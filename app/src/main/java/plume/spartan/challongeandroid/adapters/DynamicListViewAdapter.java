package plume.spartan.challongeandroid.adapters;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;

public abstract class DynamicListViewAdapter extends BaseAdapter {

    protected Context context;
    protected LayoutInflater inflater;
    private final int INVALID_ID = -1;
    protected HashMap<Object, Integer> mIdMap = new HashMap<>();
    protected List<Object> objectList;
    protected int posSwitchView = -1;

    protected DynamicListViewAdapter(Context context, List<Object> objects) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
        this.objectList = objects;
    }

    public void notifyDataSetChanged(List<Object> newList) {
        objectList.clear();
        objectList.addAll(newList);
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<Object> newList, int pos) {
        posSwitchView = pos;
        objectList.clear();
        objectList.addAll(newList);
        super.notifyDataSetChanged();
    }

    public void beginCellMove() {

    }

    public void endCellMove(int pos) {

    }

    @Override
    public int getCount() {
        return mIdMap.size();
    }

    @Override
    public Object getItem(int i) {
        return objectList.get(i);
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Object item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
