package com.pstech.hydmetro.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.pstech.hydmetro.R;
import com.pstech.hydmetro.model.StationItem;
import com.pstech.hydmetro.utils.AppUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pagrawal on 28-11-2017.
 */

public class ShowStationAdapter extends ArrayAdapter implements Filterable{


    private final Context context;
    private List<StationItem> mStations;
    private List<StationItem> origStationList;
    private StationFilter stationFilter;

    public ShowStationAdapter(Context context, List<StationItem> stations) {
        super(context, -1, stations);
        this.context = context;
        this.origStationList = stations;
        this.mStations = stations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.station_list_item, parent, false);

        StationItem station = mStations.get(position);
        ImageView lineColor = rootView.findViewById(R.id.line_color);
        TextView stationInfo = rootView.findViewById(R.id.station_info);
        TextView lineInfo = rootView.findViewById(R.id.line_info);
        TextView stationId = rootView.findViewById(R.id.stationId);

//        lineColor.setBackgroundColor(AppUtils.getColor(station.getLineType()));
        lineColor.setColorFilter(AppUtils.getColor(station.getLineType()));
        stationInfo.setText(station.getStation());
        stationId.setText(String.valueOf(station.getId()));
        return rootView;
    }

    @Override
    public int getCount() {
        return mStations.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (stationFilter == null)
            stationFilter = new StationFilter();
        return stationFilter;
    }

    private class StationFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origStationList;
                results.count = origStationList.size();
            }
            else {
                // We perform filtering operation
                List<StationItem> stationList = new ArrayList<StationItem>();

                for (StationItem p : origStationList) {
                    if (p.getStation().toUpperCase()
                            .startsWith(constraint.toString().toUpperCase()))
                        stationList.add(p);
                }

                results.values = stationList;
                results.count = stationList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            if (results.count == 0) {
                mStations = Collections.EMPTY_LIST;
                notifyDataSetInvalidated();
            }
            else {
                mStations = (List<StationItem>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
