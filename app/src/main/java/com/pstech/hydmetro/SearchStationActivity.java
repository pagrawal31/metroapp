package com.pstech.hydmetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pstech.hydmetro.adapters.ShowStationAdapter;
import com.pstech.hydmetro.model.StationItem;

import java.util.List;
import com.pstech.hydmetro.utils.AppConstants;

public class SearchStationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView stationList;
    private TextView selectItemBox;
    private List<StationItem> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(AppConstants.SEARCH_STATION);

        stations = ((MetroApplication)getApplication()).getStations();
        selectItemBox = findViewById(R.id.selectItemBox);
        stationList = findViewById(R.id.stationList);
        final ArrayAdapter adapter = new ShowStationAdapter(getApplicationContext(), stations);
        stationList.setAdapter(adapter);
        stationList.setOnItemClickListener(this);

        selectItemBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                System.out.println("Text ["+s+"]");
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        TextView stationIdView = view.findViewById(R.id.stationId);
        int stationId = -1;
        try {
            stationId = Integer.parseInt(stationIdView.getText().toString());
        } catch (NumberFormatException nfe) {
            stationId = 1;
        }

        Intent intent = new Intent();
        intent.putExtra(AppConstants.STATION_ID, stationId);
        setResult(AppConstants.SEARCH_STATION_CODE, intent);
        finish();
    }
}
