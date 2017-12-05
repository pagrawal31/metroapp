package com.pstech.hydmetro.cardview;


import com.pstech.hydmetro.enums.LineType;

public class CardItem {

    private int mTitleResource;
    private int mDrawableImage;
    private LineType lineType;

    public CardItem(int title, int image, LineType lineType) {
        this.mTitleResource = title;
        this.mDrawableImage = image;
        this.lineType = lineType;
    }

    public int getTitle() {
        return mTitleResource;
    }

    public int getImage() { return mDrawableImage; }

    public LineType getLineType() { return lineType; }
}
