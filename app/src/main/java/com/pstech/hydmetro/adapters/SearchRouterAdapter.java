package com.pstech.hydmetro.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pstech.hydmetro.R;
import com.pstech.hydmetro.enums.LineType;
import com.pstech.hydmetro.model.StationItem;
import com.pstech.hydmetro.utils.AppUtils;

import java.util.List;

/**
 * Created by pagrawal on 26-11-2017.
 */

public class SearchRouterAdapter extends RecyclerView.Adapter<SearchRouterAdapter.ViewHolder> {

    private List<StationItem> mDataset;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ImageView lineColor;
        public TextView stationInfo;
        public TextView lineInfo;
        public ViewHolder(LinearLayout linearLayout ) {
            super(linearLayout );
            lineColor = linearLayout.findViewById(R.id.line_color);
            stationInfo = linearLayout.findViewById(R.id.station_info);
            lineInfo = linearLayout.findViewById(R.id.line_info);
        }
    }

    public SearchRouterAdapter(List<StationItem> stations) {
        this.mDataset = stations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StationItem station = mDataset.get(position);
        LineType lineType = station.getLineType();
        int colorId = AppUtils.getColor(lineType);
//        holder.lineColor.setColorFilter(colorId);
        holder.lineColor.setBackgroundColor(colorId);
        holder.lineColor.setImageResource(R.drawable.ic_rail_track_vertical);
        holder.stationInfo.setText(station.toString());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
