package com.image.imagenext.imagenextbig.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.image.imagenext.imagenextbig.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by saurav on 7/22/2018.
 */

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.viewHolder> {
    ArrayList<String> urlList;
    Context context;
    public ProfileRecyclerAdapter(Context context,ArrayList<String> urlList) {
    this.context =context;
    this.urlList=urlList;


     }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_profile_image, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Picasso.get().load(urlList.get(position)).placeholder(R.mipmap.ic_image_avater).into(holder.unit_view);
    }
    public void add(String url){
        urlList.add(url);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
ImageView unit_view;
        public viewHolder(View itemView) {
            super(itemView);
            unit_view = (ImageView) itemView.findViewById(R.id.unit_view);
        }
    }
}
