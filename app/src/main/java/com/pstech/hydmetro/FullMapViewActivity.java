package com.pstech.hydmetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.pstech.hydmetro.fragments.WorkaroundMapFragment;
import com.pstech.hydmetro.model.StationItem;
import com.pstech.hydmetro.utils.AppConstants;
import com.pstech.hydmetro.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FullMapViewActivity extends AppCompatActivity implements WorkaroundMapFragment.OnTouchListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private List<StationItem> stations = new ArrayList<>();
    private boolean routeDrawn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_map_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WorkaroundMapFragment fragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        fragment.setListener(this);
        fragment.getMapAsync(this);

        Intent i = getIntent();

        ArrayList<Integer> stationIds = i.getIntegerArrayListExtra(AppConstants.STATIONS);
        Map<Integer, StationItem> stationMap = ((MetroApplication)getApplication()).getStationMap();

        if (stationIds != null) {
            for (int id : stationIds) {
                stations.add(stationMap.get(id));
            }
        }

        if (mMap != null) {
            AppUtils.drawRoute(stations, mMap);
            routeDrawn = true;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!routeDrawn ) {
            AppUtils.drawRoute(stations, mMap);
            routeDrawn = true;
        }
    }

    @Override
    public void onTouch() {
//        mScrollView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
