package com.pstech.hydmetro.cardview;

import android.support.v7.widget.CardView;

/**
 * Created by pagrawal on 02-12-2017.
 */

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
