package com.pstech.hydmetro.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.pstech.hydmetro.FullMapViewActivity;
import com.pstech.hydmetro.MetroApplication;
import com.pstech.hydmetro.R;
import com.pstech.hydmetro.SearchStationActivity;
import com.pstech.hydmetro.adapters.SearchRouterAdapter;
import com.pstech.hydmetro.model.StationItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pstech.hydmetro.utils.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link AppFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchRouteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchRouteFragment extends AppFragment implements
        WorkaroundMapFragment.OnTouchListener,
        OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.nestedScrollViewHome) NestedScrollView mScrollView;
    @BindView(R.id.source) TextView sourceTxtView;
    @BindView(R.id.destination) TextView destinationTxtView;
    @BindView(R.id.fareValue) TextView fareValue;
    @BindView(R.id.varshikFare) TextView varshikFare;
    @BindView(R.id.showFullMap) TextView showFullMap;
    @BindView(R.id.dstLayout) LinearLayout dstLayout;
    @BindView(R.id.sourceLayout) LinearLayout srcLayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.routeFindBtn) Button routeFindBtn;

    private String mParam1;
    private String mParam2;
    private List<StationItem> stations;
    private StationItem sourceStn;
    private StationItem destStn;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    private GoogleMap mMap;
    private Unbinder unbinder;

    private boolean srcSelected = false;
    private boolean dstSelected = false;
    private int srcId = -1;
    private int dstId = -1;

    private Cap startCap;

    private OnFragmentInteractionListener mListener;
    private Context context;
    private ArrayList<Integer> routeIds = AppConstants.EMPTY_ARRAY_LIST;
    //

    private static final int INITIAL_STROKE_WIDTH_PX = 8;

    public SearchRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchRouteFragment newInstance(String param1, String param2) {
        SearchRouteFragment fragment = new SearchRouteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_search_route);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        unbinder = ButterKnife.bind(this, view);

        showFullMap.setVisibility(View.INVISIBLE);

        startCap = new CustomCap(
                BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10);

        srcLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchStationOnClick(view);
            }
        });
        dstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchStationOnClick(view);
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        stations = ((MetroApplication)getActivity().getApplication()).getStations();

        WorkaroundMapFragment fragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        fragment.setListener(this);
        fragment.getMapAsync(this);

        return view;
    }

    private void searchStationOnClick(View view) {
        showFullMap.setVisibility(View.INVISIBLE);
        if (view == srcLayout) {
            srcSelected = true;
            dstSelected = false;
        } else if (view == dstLayout) {
            dstSelected = true;
            srcSelected = false;
        }
        Intent searchStationIntent = new Intent(getActivity(), SearchStationActivity.class);
        startActivityForResult(searchStationIntent, AppConstants.SEARCH_STATION_CODE);
    }

    @OnClick(R.id.showFullMap)
    void performFullMapActivity(View view) {
        Intent showFullMap = new Intent(context, FullMapViewActivity.class);
        showFullMap.putIntegerArrayListExtra(AppConstants.STATIONS, getRouteIds());
        showFullMap.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(showFullMap, AppConstants.FULL_MAP_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.SEARCH_STATION_CODE) {
            if (resultCode != Activity.RESULT_CANCELED) {
                int stationId = data.getIntExtra(AppConstants.STATION_ID, 0);
                if (srcSelected) {
                    srcId = stationId;
                    sourceStn = stations.get(stationId-1);
                    sourceTxtView.setText(sourceStn.getStation());
                }
                else if (dstSelected) {
                    dstId = stationId;
                    destStn = stations.get(stationId-1);
                    destinationTxtView.setText(destStn.getStation());
                }
            }
        }
    }

    @OnClick(R.id.routeFindBtn)
    void showRoute(View view) {
        if (sourceStn == null) {
            Toast.makeText(this.context, AppConstants.CHOOSE_SOURCE_STATION, Toast.LENGTH_SHORT).show();
            return;
        } else if (destStn == null) {
            Toast.makeText(this.context, AppConstants.CHOOSE_DEST_STATION, Toast.LENGTH_SHORT).show();
            return;
        }

        showFullMap.setVisibility(View.VISIBLE);
        int sourceId = sourceStn.getId();
        int destId = destStn.getId();
        boolean reverse = false;
        if (sourceId ==  destId) {
            Toast.makeText(this.context, AppConstants.SOURCE_DEST_SAME, Toast.LENGTH_SHORT).show();
        } else {
            if (sourceId > destId) {
                int temp = sourceId;
                sourceId = destId;
                destId = temp;
                reverse = true;
            }
            List<StationItem> route = new ArrayList<>();
            ArrayList<Integer> routeIntList = new ArrayList<>();
            for (int idx = sourceId-1; idx < destId; idx++) {
                route.add(stations.get(idx));
                routeIntList.add(stations.get(idx).getId());
                setRouteIds(routeIntList);
            }
            if (reverse) {
                Collections.reverse(route);
            }

            mAdapter = new SearchRouterAdapter(route);
            recyclerView.setAdapter(mAdapter);
            double distance = Math.abs(sourceStn.getDistanceFareFromBase() - destStn.getDistanceFareFromBase());
            double fare = AppUtils.getFare(distance);
            fareValue.setText(String.valueOf(fare));
            varshikFare.setText(String.valueOf(fare*.95));

            AppUtils.drawRoute(route, mMap);
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTouch() {
         mScrollView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        StationItem baseStation = stations.get(0);
        LatLng location = new LatLng(baseStation.getLattitude(), baseStation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(location)
                .title(baseStation.getStation()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10.0f));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc2, 10.0f));
    }

    public ArrayList<Integer> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(ArrayList<Integer> routeIds) {
        this.routeIds = routeIds;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
