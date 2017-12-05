package com.pstech.hydmetro.utils;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.pstech.hydmetro.enums.LineType;
import com.pstech.hydmetro.model.StationItem;

import java.util.List;

/**
 * Created by pagrawal on 28-11-2017.
 */

public class AppUtils {
    public static int getColor(LineType lineType) {
        switch (lineType) {
            case GREEN:
                return Color.GREEN;
            case RED:
                return Color.RED;
            case YELLOW:
                return Color.YELLOW;
            case BLUE:
                return Color.BLUE;
        }
        return Color.CYAN;
    }

    public static int getIntFromBoolean(boolean value) {
        return value ? 1 :  0;
    }

    public static boolean getBooleanFromInt(int value) {
        return value == 1 ? Boolean.TRUE : Boolean.FALSE;
    }
    public static String getYesNoFromBoolean(boolean value) {
        return value ? AppConstants.YES : AppConstants.NO;
    }

    public static double getFare(double distance) {
        if (distance < 2) {
            return 10.0;
        } else if (distance > 2 && distance <= 4) {
            return 15.0;
        }  else if (distance > 4 && distance <= 6) {
            return 25.0;
        } else if (distance > 6 && distance <= 8) {
            return 30.0;
        } else if (distance > 8 && distance <= 10) {
            return 35.0;
        } else if (distance > 10 && distance <= 14) {
            return 40.0;
        } else if (distance > 14 && distance <= 18) {
            return 45.0;
        } else if (distance > 18 && distance <= 22) {
            return 50.0;
        } else if (distance > 22 && distance <= 26) {
            return 55.0;
        } else {
            return 60;
        }
    }

    public static boolean drawRoute(List<StationItem> route, GoogleMap mMap) {
        boolean pointsAdded = false;
        PolylineOptions rectOptions = new PolylineOptions();

        LatLng baseLoc = null;
        LatLng currLoc = null;

        for (StationItem stationItem : route) {
            double lattitude = stationItem.getLattitude();
            double longitude = stationItem.getLongitude();
            if (longitude == AppConstants.DEFAULT_DOUBLE) {
                break;
            }

            if (baseLoc == null)
                baseLoc = new LatLng(lattitude, longitude);

            currLoc = new LatLng(lattitude, longitude);
            mMap.addMarker(new MarkerOptions().position(currLoc).title(stationItem.getShortCode()));
            rectOptions.add(currLoc);
            pointsAdded = true;
        }

        Polyline polyline;
        if (pointsAdded) {
            polyline = mMap.addPolyline(rectOptions);
            stylePolyline(polyline);
//            polyline.setStartCap(startCap);
//            polyline.setEndCap(new RoundCap());
//            polyline.setJointType(JointType.ROUND);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(baseLoc, 10.0f));
        return true;
    }

    private static void stylePolyline(Polyline polyline) {
//        polyline.setStartCap(startCap);
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(AppConstants.INITIAL_STROKE_WIDTH_PX);
//        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }

}
