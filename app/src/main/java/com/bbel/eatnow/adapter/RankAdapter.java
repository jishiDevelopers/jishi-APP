package com.bbel.eatnow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbel.eatnow.R;
import com.bbel.eatnow.bean.RankItem;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<RankItem> mFruitList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView rankImage;
        TextView rankRestName;
        TextView rankDishName;
        TextView rankCanteenName;
        TextView rankCount;
        public ViewHolder(View view) {
            super(view);
            rankImage = view.findViewById(R.id.rank_image);
            rankDishName = view.findViewById(R.id.rank_dish_name);
            rankRestName = view.findViewById(R.id.rank_rest_name);
            rankCanteenName = view.findViewById(R.id.rank_canteen_name);
            rankCount = view.findViewById(R.id.rank_count_name);
        }
    }
    public RankAdapter(List<RankItem> fruitList) {
        mFruitList = fruitList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rank_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RankItem fruit = mFruitList.get(position);
        holder.rankImage.setImageResource(fruit.getImageId());
        holder.rankDishName.setText(fruit.getDishName());
        holder.rankRestName.setText(fruit.getRestName());
        holder.rankCount.setText("" + fruit.getCount());
        holder.rankCanteenName.setText(fruit.getCanteenName());
    }
    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}