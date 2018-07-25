package com.image.imagenext.imagenextbig.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.image.imagenext.imagenextbig.AccountProfileActivity;
import com.image.imagenext.imagenextbig.Models.Like;
import com.image.imagenext.imagenextbig.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saurav on 7/24/2018.
 */

public class LikeRecyclerAdapter extends RecyclerView.Adapter<LikeRecyclerAdapter.viewHolder> {
    ArrayList<Like> LikerDetailList;
    Context context;
    public LikeRecyclerAdapter(Context context, ArrayList<Like> LikerDetailList) {
        this.LikerDetailList=LikerDetailList;
        this.context=context;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_liker,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Slabo27px-Regular.ttf");
        holder.textView.setTypeface(typeface);
        holder.textView.setText(LikerDetailList.get(position).getName());
        Picasso.get().load(LikerDetailList.get(position).getProfileImage()).placeholder(R.mipmap.ic_def_avater).into(holder.circleImageView);
        holder.follow_him.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountProfileActivity.class);
                intent.putExtra("user_id", LikerDetailList.get(position).getUserid());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return LikerDetailList.size();
    }

    public void add(Like lIke){
          LikerDetailList.add(lIke);
          notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
CircleImageView circleImageView;
TextView textView;
TextView follow_him;
        public viewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.liker_face);
            textView = (TextView) itemView.findViewById(R.id.likers_name);
            follow_him = (TextView) itemView.findViewById(R.id.follow_him);

        }
    }
}
