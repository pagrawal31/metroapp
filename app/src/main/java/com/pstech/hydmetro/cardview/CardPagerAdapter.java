package com.pstech.hydmetro.cardview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pstech.hydmetro.R;
import com.pstech.hydmetro.utils.AppUtils;
import com.pstech.hydmetro.utils.LineInformationActivity;

import java.util.ArrayList;
import java.util.List;
import com.pstech.hydmetro.utils.AppConstants;

/**
 * Created by pagrawal on 02-12-2017.
 */

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    private Context context;

    public CardPagerAdapter(Context context) {
        this.context = context;
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.card_view_item, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPageClickHandle(view);
            }
        });
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    private void viewPageClickHandle(View view) {

        TextView lineTypeView = view.findViewById(R.id.lineTypeView);

        Intent lineInfoIntent = new Intent(this.context, LineInformationActivity.class);
        lineInfoIntent.putExtra(AppConstants.LINE_TYPE, lineTypeView.getText().toString());
        this.context.startActivity(lineInfoIntent);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
        mViews.set(position, null);
    }

    private void bind(CardItem item, View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.metroIconImageView);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        TextView lineTypeView = view.findViewById(R.id.lineTypeView);

        imageView.setImageResource(item.getImage());
        contentTextView.setText(item.getTitle());
//        contentTextView.setTextColor(item.getColor());
        contentTextView.setBackgroundColor(Color.WHITE);
        contentTextView.setTextColor(AppUtils.getColor(item.getLineType()));
        lineTypeView.setText(item.getLineType().toString());
    }
}
