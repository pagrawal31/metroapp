package com.pstech.hydmetro;

import android.app.Application;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;

import com.pstech.hydmetro.model.StationItem;
import com.pstech.hydmetro.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pagrawal on 25-11-2017.
 */

public class MetroApplication extends Application {

    private static String stationFile = "stations.json";
    private static List<String> tags = Arrays.asList("Id", "Short Code",
            "Short Name", "Station", "underground", "interchange", "Line", "Parking", "MMTS", "Latitude", "Longitude", "DistanceFromBase",
            "SmartBike");

    private List<StationItem> stations = new ArrayList<>();
    private Map<Integer, StationItem> stationMap = new HashMap<>();
    private List<StationItem> redStations = new ArrayList<>();
    private List<StationItem> blueStations = new ArrayList<>();
    private List<StationItem> greenStations = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        loadDataFromJsonFile();
    }

    public void loadDataFromJsonFile() {
        AssetManager manager = getAssets();
        try {
            InputStream is = manager.open(stationFile);
            String stationData = getStringFromInputStream(is);
            JSONArray jsonArray = new JSONArray(stationData);
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject stationJsonObject = (JSONObject) jsonArray.get(i);
                StationItem stationItem = getStation(stationJsonObject);
                stationMap.put(stationItem.getId(), stationItem);
                if (stationItem != null) {
                    stations.add(stationItem);
                    switch (stationItem.getLineType()) {
                        case RED:
                            redStations.add(stationItem);
                            break;
                        case BLUE:
                            blueStations.add(stationItem);
                            break;
                        case GREEN:
                            greenStations.add(stationItem);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            Log.e("IO_EXCEPTION", "Failed In IO", e);
        } catch (JSONException e) {
            Log.e("JSON_EXCEPTION", "Failed To Parse", e);
        } catch (Exception e) {
            Log.e("UNKNOWN_EXCEPTION", "Failed", e);
        }
    }

    private StationItem getStation(JSONObject stationJsonObject) throws JSONException {
        StationItem.Builder builder = new StationItem.Builder();
        String stationName = AppConstants.EMPTY;
        for (String tag : tags) {
            if (stationJsonObject.has(tag)) {
                Object tagValue = stationJsonObject.get(tag);
                if (tagValue instanceof String) {
                    String item = (String) tagValue;
                    switch (tag) {
                        case "Id":
                            int intId = -1;
                            try {
                                intId = Integer.parseInt(item);
                            } catch(NumberFormatException nfe){
                                intId = -1;
                            }
                            builder.setId(Integer.valueOf(intId));
                            break;
                        case "Short Code":
                            builder.setShortCode(item);
                            break;
                        case "Short Name":
                            builder.setShortName(item);
                            break;
                        case "Station":
                            stationName = item;
                            break;
                        case "underground":
                            builder.setUnderground(getBoolean(item));
                            break;
                        case "interchange":
                            builder.setInterchange(getBoolean(item));
                            break;
                        case "Line":
                            builder.setLineType(item);
                            break;
                        case "Parking":
                            builder.setParkingAvailable(getBoolean(item));
                            break;
                        case "MMTS":
                            builder.setMmts(getBoolean(item));
                            break;
                        case "Latitude":
                            builder.setLattitude(getDouble(item));
                            break;
                        case "Longitude":
                            builder.setLongitude(getDouble(item));
                            break;
                        case "DistanceFromBase":
                            builder.setDistanceFareFromBase(getDouble(item));
                            break;
                        case "SmartBike":
                            builder.setSmartBikeAvailable(getBoolean(item));
                        default:
                    }
                }
            }
        }
        if (stationName == AppConstants.EMPTY)
            return null;
        return builder.build(stationName);
    }

    private double getDouble(String item) {
        double defaultValue = AppConstants.DEFAULT_DOUBLE;
        if (item == null)
            return defaultValue;

        try {
            defaultValue = Double.parseDouble(item);
        } catch (NumberFormatException nfe) {

        }
        return defaultValue;
    }

    private boolean getBoolean(String item) {
        if (item == null)
            return false;
        if (item.equalsIgnoreCase("yes") || item.equals("true"))
            return true;
        return false;
    }

    public String getStringFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");

    }

    public List<StationItem> getStations () {
        return Collections.unmodifiableList(stations);
    }

    public List<StationItem> getRedStations() {
        return Collections.unmodifiableList(getSortedStations(redStations));
    }

    public List<StationItem> getBlueStations() {
        return Collections.unmodifiableList(getSortedStations(blueStations));
    }

    private List<StationItem> getSortedStations(List<StationItem> stations) {
        Arrays.sort((StationItem[])stations.toArray(), new Comparator<StationItem>() {
            @Override
            public int compare(StationItem item1, StationItem item2) {
                return (item1.getId() > item2.getId()) ? 1 : -1;
            }
        });
        return stations;
    }
    
    public List<StationItem> getGreenStations() {
        return Collections.unmodifiableList(getSortedStations(greenStations));
    }

    public void shareApplication() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, AppConstants.SHARE_TXT);
        sendIntent.setType("text/html");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    public Map<Integer, StationItem> getStationMap() {
        return stationMap;
    }
}
