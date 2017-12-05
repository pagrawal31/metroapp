package com.pstech.hydmetro.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pstech.hydmetro.MetroApplication;
import com.pstech.hydmetro.R;
import com.pstech.hydmetro.SearchStationActivity;
import com.pstech.hydmetro.model.StationItem;

import java.util.ArrayList;
import java.util.List;
import com.pstech.hydmetro.utils.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StationFragment extends AppFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView parkingView ;
    private CardView mmtsView;
    private CardView interchangeView;
    private CardView smartBikeView;
    private LinearLayout stationInfo;

    private OnFragmentInteractionListener mListener;
//    private Spinner stationSpinner;
    private TextView selectItemBox;
    private ImageView searchIcon;
    private Context context;
    List<StationItem> stations;

    public StationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StationFragment newInstance(String param1, String param2) {
        StationFragment fragment = new StationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        getActivity().setTitle(R.string.title_station);

        selectItemBox = view.findViewById(R.id.txt_station);
        searchIcon = view.findViewById(R.id.station_search_icon);

        parkingView = view.findViewById(R.id.amenityParking);
        mmtsView = view.findViewById(R.id.amenityMMTS);
        interchangeView = view.findViewById(R.id.interchange);
        smartBikeView = view.findViewById(R.id.smartBike);
        stationInfo = view.findViewById(R.id.stationInfo);

        stations = ((MetroApplication)getActivity().getApplication()).getStations();

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchStationOnClick(view);
            }
        });
        List<StationItem> stations = ((MetroApplication)getActivity().getApplication()).getStations();
        List<String> list = new ArrayList<String>();

        for (StationItem station : stations) {
            list.add(station.toString());
        }

        LinearLayout stationLayout = view.findViewById(R.id.station_id);
        stationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchStationOnClick(view);
            }
        });
        return view;
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

    private void searchStationOnClick(View view) {
        Intent searchStationIntent = new Intent(getActivity(), SearchStationActivity.class);
        startActivityForResult(searchStationIntent, AppConstants.SEARCH_STATION_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.SEARCH_STATION_CODE) {
            if (resultCode != Activity.RESULT_CANCELED) {
                int stationId = data.getIntExtra(AppConstants.STATION_ID, 0);
                StationItem station = stations.get(stationId-1);
                selectItemBox.setText(station.getStation());
                populateStationInformation(station);
            }
        }
    }

    private void populateStationInformation(StationItem stationItem) {
        stationInfo.setVisibility(View.VISIBLE);
        TextView ixType = interchangeView.findViewById(R.id.amenityType);
        TextView ixValue = interchangeView.findViewById(R.id.amenityValue);

        TextView parkingType = parkingView.findViewById(R.id.amenityType);
        TextView parkingValue = parkingView.findViewById(R.id.amenityValue);

        TextView mmtsType = mmtsView.findViewById(R.id.amenityType);
        TextView mmtsValue = mmtsView.findViewById(R.id.amenityValue);

        TextView smartBikeType = smartBikeView.findViewById(R.id.amenityType);
        TextView smartBikeValue = smartBikeView.findViewById(R.id.amenityValue);

        ixType.setText(AppConstants.IS_INTERCHANGE);
        ixValue.setText(AppUtils.getYesNoFromBoolean(stationItem.isInterchange()));

        parkingType.setText(AppConstants.PARKING_AVAIL);
        parkingValue.setText(AppUtils.getYesNoFromBoolean(stationItem.isParkingAvailable()));

        mmtsType.setText(AppConstants.MMTS_AVAIL);
        mmtsValue.setText(AppUtils.getYesNoFromBoolean(stationItem.isMmts()));

        smartBikeType.setText(AppConstants.SMART_BIKE_AVAIL);
        smartBikeValue.setText(AppUtils.getYesNoFromBoolean(stationItem.isSmartBikeAvailable()));

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
}
