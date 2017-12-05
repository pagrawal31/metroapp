package com.pstech.hydmetro.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pstech.hydmetro.MetroApplication;
import com.pstech.hydmetro.R;
import com.pstech.hydmetro.enums.LineType;
import com.pstech.hydmetro.fragments.WorkaroundMapFragment;
import com.pstech.hydmetro.model.StationItem;

import java.util.Collections;
import java.util.List;

public class LineInformationActivity extends AppCompatActivity
        implements WorkaroundMapFragment.OnTouchListener, OnMapReadyCallback {

    private int mTitleId = R.string.title_green;
    private TextView lineTypeView;
    private TextView lineLength;
    private TextView numStation;
    private TextView numStationInactive;
    private TextView numInterchangeStn;
    private ImageView lineTypeImageView;
    private TextView freq;
    private GoogleMap mMap;
    private WorkaroundMapFragment fragment;
    private NestedScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lineTypeView = findViewById(R.id.lineType);
        lineLength = findViewById(R.id.lineLength);
        numStation = findViewById(R.id.numStation);
        numStationInactive = findViewById(R.id.numStationInactive);
        numInterchangeStn = findViewById(R.id.numInterchangeStn);;
        lineTypeImageView = findViewById(R.id.lineTypeImageView);
        freq = findViewById(R.id.freq);;
        mScrollView = findViewById(R.id.nestedScrollViewHome);

        Intent i = getIntent();

        String lineType = i.getStringExtra(AppConstants.LINE_TYPE);

        mTitleId = getTitleId(lineType);
        setTitle(mTitleId);
        lineTypeImageView.setImageResource(getImageId(lineType));
        lineTypeView.setText(mTitleId);
        lineTypeView.setTextColor(AppUtils.getColor(LineType.valueOf(lineType)));
        Pair<Integer, Integer> stations = getNumStation(LineType.valueOf(lineType));
        List<StationItem> lineStations = getLineStation(LineType.valueOf(lineType));

        numStation.setText(getResources().getString(R.string.active_station) +
                String.valueOf(stations.second) +
                getResources().getString(R.string.title_km));

        numStationInactive.setText(getResources().getString(R.string.inactive_station) + String.valueOf(stations.second));

        fragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        fragment.setListener(this);
        fragment.getMapAsync(this);

    }

    private List<StationItem> getLineStation(LineType lineType) {
//        switch (lineType) {
//            case BLUE:
//                return ((MetroApplication)getApplication()).getBlueStations();
//            case GREEN:
//                return ((MetroApplication)getApplication()).getGreenStations();
//            case RED:
//                return ((MetroApplication)getApplication()).getRedStations();
//        }
        return Collections.EMPTY_LIST;
    }

    public static int getImageId(String lineType) {
        int imageId = R.drawable.metro_blue_round;
        switch (LineType.valueOf(lineType)) {
            case BLUE:
                imageId = R.drawable.metro_blue_round;
                break;
            case GREEN:
                imageId = R.drawable.metro_green_round;
                break;
            case RED:
                imageId = R.drawable.metro_red_round;
                break;
        }
        return imageId;
    }

    public static int getTitleId(String lineType) {
        int titleId = R.string.title_blue;
        switch (LineType.valueOf(lineType)) {
            case BLUE:
                titleId = R.string.title_blue;
                break;
            case GREEN:
                titleId = R.string.title_green;
                break;
            case RED:
                titleId = R.string.title_red;
                break;
        }
        return titleId;
    }

    public static Pair<Integer, Integer> getNumStation(LineType lineType) {
        switch (lineType) {
            case BLUE:
                return Pair.create(14, 10);
            case GREEN:
                return Pair.create(0, 15);
            case RED:
                return Pair.create(11, 16);
        }
        return Pair.create(0, 0);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Miyapur
        LatLng miyapur = new LatLng(17.496538, 78.373023);

        mMap.addMarker(new MarkerOptions().position(miyapur).title("Miyapur"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(miyapur,10.0f));

    }

    @Override
    public void onTouch() {
        mScrollView.requestDisallowInterceptTouchEvent(true);
    }
}
