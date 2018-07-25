package com.image.imagenext.imagenextbig.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.image.imagenext.imagenextbig.Models.BranchComment;
import com.image.imagenext.imagenextbig.Models.SubComment;
import com.image.imagenext.imagenextbig.R;
import com.image.imagenext.imagenextbig.utils.TimeTeller;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saurav on 7/24/2018.
 */

public class SubCommentREcyclerAdapter extends RecyclerView.Adapter<SubCommentREcyclerAdapter.viewHolder> {
    ArrayList<BranchComment> subCommnetArrayList;
    Context context;
    public SubCommentREcyclerAdapter(Context context, ArrayList<BranchComment> subCommnetArrayList) {
        this.context =context;
        this.subCommnetArrayList = subCommnetArrayList;


    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_subcomment, parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Slabo27px-Regular.ttf");

        Picasso.get().load(subCommnetArrayList.get(position).getSub_comment_image_url()).placeholder(R.mipmap.ic_def_avater).into(holder.root_comment_profile_img);
        holder.name.setText(subCommnetArrayList.get(position).getSub_comment_name());
        holder.comment.setText(subCommnetArrayList.get(position).getSub_comment());
        holder.time_of_post.setText(TimeTeller.getTimeAgo(Long.parseLong(subCommnetArrayList.get(position).getSub_comment_time())).substring(0,4));
        holder.name.setTypeface(typeface);
        holder.comment.setTypeface(typeface);


    }

    @Override
    public int getItemCount() {
        return subCommnetArrayList.size();
    }

    public void add(BranchComment subComment){
        subCommnetArrayList.add(subComment);
        notifyDataSetChanged();

    }

    public class viewHolder extends RecyclerView.ViewHolder{

        CircleImageView root_comment_profile_img;
        TextView name;
        TextView comment;
        TextView time_of_post;
        TextView root_post_like;
        TextView reply_root_post;

        public viewHolder(View itemView) {
            super(itemView);
            root_comment_profile_img = (CircleImageView) itemView.findViewById(R.id.root_comment_profile_img);
            name = (TextView) itemView.findViewById(R.id.name);
            comment= (TextView) itemView.findViewById(R.id.comment);
            time_of_post= (TextView) itemView.findViewById(R.id.time_of_post);
            root_post_like= (TextView) itemView.findViewById(R.id.root_post_like);
            reply_root_post= (TextView) itemView.findViewById(R.id.reply_root_post);

        }
    }

}
