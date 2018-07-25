package com.image.imagenext.imagenextbig.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.image.imagenext.imagenextbig.Models.usermodel;
import com.image.imagenext.imagenextbig.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saurav on 7/24/2018.
 */

public class UserREcyclerAdapter extends RecyclerView.Adapter<UserREcyclerAdapter.viewHolder> {
    ArrayList<usermodel> userArrayList ;
    Context context;
    public UserREcyclerAdapter(Context context,ArrayList<usermodel> userArrayList ) {

        this.context =context;
        this.userArrayList=userArrayList;


     }
public void add(usermodel usermodel){
        userArrayList.add(usermodel);
        notifyDataSetChanged();
}
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_user, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Slabo27px-Regular.ttf");

        Picasso.get().load(userArrayList.get(position).getProfile_pic()).placeholder(R.mipmap.ic_def_avater).into(holder.pic_profile);
        holder.name.setText(userArrayList.get(position).getName());
        holder.user_name.setText(userArrayList.get(position).getUsername());
        holder.name.setTypeface(typeface);
        holder.user_name.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
CircleImageView pic_profile;
TextView name;
TextView user_name;
        public viewHolder(View itemView) {
            super(itemView);
            pic_profile = (CircleImageView) itemView.findViewById(R.id.pic_profile);
            name= (TextView) itemView.findViewById(R.id.name);
            user_name= (TextView)itemView.findViewById(R.id.user_name);
        }
    }
}
