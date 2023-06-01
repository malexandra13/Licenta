package com.example.licenta.owner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SalonAdapter extends RecyclerView.Adapter<SalonAdapter.ViewHolder> {

    List<SalonModel> list;
    Context context;

    public SalonAdapter(List<SalonModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SalonModel model = list.get(position);
        Picasso.get().load(model.getSalonImage()).
                placeholder(R.drawable.upload_image).
                into(holder.itemSalonImageView);
        holder.itemSalonName.setText(model.getSalonName());
        holder.itemSalonCity.setText(model.getSalonCity());
        holder.itemSalonDescription.setText(model.getSalonDescription());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemSalonName, itemSalonCity, itemSalonDescription;
        ImageView itemSalonImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemSalonName = itemView.findViewById(R.id.twNameSalon);
            itemSalonCity = itemView.findViewById(R.id.twAddress);
            itemSalonDescription = itemView.findViewById(R.id.twDescription);
            itemSalonImageView = itemView.findViewById(R.id.imageViewSalon);

        }
    }
}
