package com.example.it2019092_miniproject.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.model.TopPlaces;

import java.util.List;

public class TopPlacesAdapter extends RecyclerView.Adapter<TopPlacesAdapter.TopPlacesViewHolder> {

    Context context;
    List<TopPlaces> placesList;

    public TopPlacesAdapter(Context context, List<TopPlaces> placesList) {
        this.context = context;
        this.placesList = placesList;
    }

    @NonNull
    @Override
    public TopPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.top_places_row_item, parent, false);

        return new TopPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPlacesViewHolder holder, int position) {

        holder.province.setText(placesList.get(position).getProvince());
        holder.placeName.setText(placesList.get(position).getPlaceName());
        holder.placeImage.setImageResource(placesList.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static final class TopPlacesViewHolder extends RecyclerView.ViewHolder{

        ImageView placeImage;
        TextView placeName, province;

        public TopPlacesViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.place_name);
            province = itemView.findViewById(R.id.province);

        }
    }
}

