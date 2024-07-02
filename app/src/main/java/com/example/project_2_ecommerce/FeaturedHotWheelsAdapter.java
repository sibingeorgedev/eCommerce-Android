package com.example.project_2_ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeaturedHotWheelsAdapter extends RecyclerView.Adapter<FeaturedHotWheelsAdapter.MyViewHolder> {

    public ArrayList<FeaturedHotWheels> hotWheelsList;
    public FeaturedHotWheelsAdapter(ArrayList<FeaturedHotWheels> hotWheelsList) {
        this.hotWheelsList = hotWheelsList;
    }

    @NonNull
    @Override
    public FeaturedHotWheelsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedHotWheelsAdapter.MyViewHolder holder, int position) {

        FeaturedHotWheels featuredHotWheels = hotWheelsList.get(position);
        Context actContext = holder.itemView.getContext();
        int resid = actContext.getResources().getIdentifier(featuredHotWheels.getImage(), "drawable", actContext.getPackageName());

        holder.imgHotWheel.setImageResource(resid);
    }

    @Override
    public int getItemCount() {
        return hotWheelsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotWheel;

        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.featured_pager_layout, parent, false));

            imgHotWheel = itemView.findViewById(R.id.imgHotWheel);
        }
    }
}
