package com.bbel.eatnow.adapter;


import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbel.eatnow.R;
import com.bbel.eatnow.adapter.CardAdapter1;
import com.bbel.eatnow.bean.CardItem1;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter1 extends PagerAdapter implements CardAdapter1 {

    private List<CardView> mViews;
    private List<CardItem1> mData;
    private float mBaseElevation;

    public CardPagerAdapter1() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem1 item) {
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
                .inflate(R.layout.adapter1, container, false);
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItem1 item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        TextView markTextView=(TextView)view.findViewById(R.id.cardMark) ;
        titleTextView.setText(item.getTitle());
        contentTextView.setText(item.getText());
        markTextView.setText(item.getMark());
    }

}
