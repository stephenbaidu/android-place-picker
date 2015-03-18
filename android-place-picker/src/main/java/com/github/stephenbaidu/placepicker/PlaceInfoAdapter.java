package com.github.stephenbaidu.placepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by stephen on 3/1/15.
 */
public class PlaceInfoAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PlaceInfo> mSuggestedPlaces;
    private int mIcon;

    PlaceInfoAdapter(Context context, int icon) {
        mInflater = LayoutInflater.from(context);
        mSuggestedPlaces = Collections.emptyList();
        mIcon = icon;
    }

    @Override
    public int getCount() {
        return mSuggestedPlaces.size();
    }

    @Override
    public Object getItem(int position) {
        return mSuggestedPlaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        PlaceInfo placeInfo = mSuggestedPlaces.get(position);

        if (convertView == null) {
            if (placeInfo.isLink) {
                view = mInflater.inflate(R.layout.listview_link_row, parent, false);
            } else {
                view = mInflater.inflate(R.layout.listview_row, parent, false);
            }
            holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.description = (TextView) view.findViewById(R.id.description);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(placeInfo.name);

        if (!placeInfo.isLink) {
            holder.icon.setImageResource(mIcon);
            holder.description.setText(placeInfo.description);
        }

        return view;
    }

    private class ViewHolder {
        public ImageView icon;
        public TextView name, description;
    }

    public void updateList(List<PlaceInfo> list) {
        mSuggestedPlaces = list;
        notifyDataSetChanged();
    }
}
