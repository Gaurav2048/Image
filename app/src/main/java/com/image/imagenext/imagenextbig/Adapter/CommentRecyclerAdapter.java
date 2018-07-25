package com.image.imagenext.imagenextbig.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.image.imagenext.imagenextbig.Models.RootComment;
import com.image.imagenext.imagenextbig.Models.SubComment;
import com.image.imagenext.imagenextbig.R;
import com.image.imagenext.imagenextbig.SubCommentActivity;
import com.image.imagenext.imagenextbig.utils.TimeTeller;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saurav on 7/23/2018.
 */

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.viewHolder> {
    String post_id;
    Context context;  int count =0;
    ArrayList<RootComment> rootCommentArrayList;
    public CommentRecyclerAdapter(Context context, String post_id,ArrayList<RootComment> rootCommentArrayList) {
        this.context=context;
        this.post_id=post_id;
        this.rootCommentArrayList=rootCommentArrayList;


     }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Slabo27px-Regular.ttf");
        holder.comment.setTypeface(typeface);
        holder.time_of_post.setTypeface(typeface);
        holder.root_post_like.setTypeface(typeface);
        holder.reply_root_post.setTypeface(typeface);
        //holder..setTypeface(typeface);

        Picasso.get().load(rootCommentArrayList.get(position).getRoot_comment_image_url()).into(holder.root_comment_profile_img);
        holder.name.setText(rootCommentArrayList.get(position).getRoot_comment_name());
        holder.comment.setText(rootCommentArrayList.get(position).getRoot_comment());
        holder.time_of_post.setText(TimeTeller.getTimeAgo(Long.parseLong(rootCommentArrayList.get(position).getRoot_comment_time())).substring(0,4));
        holder.first_subComment.setVisibility(View.GONE);
        holder.next_subcomment.setVisibility(View.GONE);

  /* if(rootCommentArrayList.get(position).getSub_comment_one_name().equals(""))
   {
       holder.first_subComment.setVisibility(View.GONE);
   }else {
       Log.e( "onBindViewHolder: ",rootCommentArrayList.get(position).getSub_comment_one_name()+" " );
       holder.first_subComment.setVisibility(View.VISIBLE);
       holder.Subcomment_name.setText(rootCommentArrayList.get(position).getSub_comment_one_name());
       Log.e( "onBindViewHolder: ",rootCommentArrayList.get(position).getSub_comment_one_comment()+" " );

       holder.subcoment_one.setText(rootCommentArrayList.get(position).getSub_comment_one_comment());
       Log.e( "onBindViewHolder: ",rootCommentArrayList.get(position).getSub_comment_one_comment()+" " );

       Picasso.get().load(rootCommentArrayList.get(position).getSub_comment_one_image_url()).placeholder(R.mipmap.ic_def_avater).into(holder.profile_one);
       Log.e( "onBindViewHolder: ",rootCommentArrayList.get(position).getSub_comment_one_image_url()+" " );

       holder.first_subComment.invalidate();
   }
     if(rootCommentArrayList.get(position).getSub_comment_two_name().equals(""))
     {
         holder.next_subcomment.setVisibility(View.GONE);
     }else {
         Log.e( "onBindViewHolder:__ ",rootCommentArrayList.get(position).getSub_comment_two_name()+" " );
         holder.next_subcomment.setVisibility(View.VISIBLE);
          holder.Subcomment_name_another.setText(rootCommentArrayList.get(position).getSub_comment_two_name());
         holder.subcoment_one_another.setText(rootCommentArrayList.get(position).getSub_comment_two_comment());
         Log.e( "onBindViewHolder:__ ",rootCommentArrayList.get(position).getSub_comment_two_comment()+" " );

         Picasso.get().load(rootCommentArrayList.get(position).getSub_comment_two_image_url()).into(holder.profile_subcomment_another);
         Log.e( "onBindViewHolder:__ ",rootCommentArrayList.get(position).getSub_comment_two_image_url()+" " );
         Log.e(  "onBindViewHolder: "," " );
         Log.e(  "onBindViewHolder: "," " );
         Log.e(  "onBindViewHolder: "," " );
         Log.e(  "onBindViewHolder: "," " );
         Log.e(  "onBindViewHolder: "," " );

           if (dataSnapshot.exists() && count==0){
                                              Map<String , Object > objectMap = (Map<String, Object>) dataSnapshot.getValue();
                                              rootCommentArrayList.get(position).setSub_comment_one_name((String) objectMap.get(SubComment.SUB_COMMENT_NAME));
                                              rootCommentArrayList.get(position).setSub_comment_one_comment((String)objectMap.get(SubComment.SUB_COMMENT));
                                              Picasso.get().load((String)objectMap.get(SubComment.SUB_COMMENT_IMAGE_URL)).placeholder(R.mipmap.ic_def_avater).into(holder.profile_one);
                                              holder.first_subComment.setVisibility(View.VISIBLE);
                                              notifyDataSetChanged();
                                              Log.e( "onDataChange: ","3" );

                                              count=1;
                                          }else if (dataSnapshot.exists() && count==1){
                                              Map<String , Object > objectMap = (Map<String, Object>) dataSnapshot.getValue();
                                              rootCommentArrayList.get(position).setSub_comment_two_name((String) objectMap.get(SubComment.SUB_COMMENT_NAME));
                                              rootCommentArrayList.get(position).setSub_comment_two_comment((String)objectMap.get(SubComment.SUB_COMMENT));
                                              Picasso.get().load((String)objectMap.get(SubComment.SUB_COMMENT_IMAGE_URL)).placeholder(R.mipmap.ic_def_avater).into(holder.profile_one);
                                              holder.next_subcomment.setVisibility(View.VISIBLE);
                                              notifyDataSetChanged();
                                              Log.e( "onDataChange: ","4" );

                                          }

     }*/
count =0;



        holder.reply_root_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubCommentActivity.class);
                intent.putExtra("root_comment_id", rootCommentArrayList.get(position).getRoot_comment_id());
                intent.putExtra("post_id", post_id);
                intent.putExtra("root_comment", rootCommentArrayList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.root_post_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        holder.read_more_sub_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SubCommentActivity.class);
                intent.putExtra("root_comment_id", rootCommentArrayList.get(position).getRoot_comment_id());
                intent.putExtra("post_id", post_id);
                intent.putExtra("root_comment", rootCommentArrayList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
public void  add(RootComment comment){
rootCommentArrayList.add(comment);
notifyDataSetChanged();
}
    @Override
    public int getItemCount() {
        return rootCommentArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
CircleImageView root_comment_profile_img;
TextView name;
TextView comment;
TextView time_of_post;
TextView root_post_like;
TextView reply_root_post;
TextView read_more_sub_comments;
CircleImageView profile_one;
TextView Subcomment_name;
TextView subcoment_one;
CircleImageView profile_subcomment_another;
TextView Subcomment_name_another;
TextView subcoment_one_another;
LinearLayout first_subComment, next_subcomment;
          public viewHolder(View itemView) {
            super(itemView);
            root_comment_profile_img = (CircleImageView) itemView.findViewById(R.id.root_comment_profile_img);
            name = (TextView) itemView.findViewById(R.id.name);
            comment= (TextView) itemView.findViewById(R.id.comment);
            time_of_post= (TextView) itemView.findViewById(R.id.time_of_post);
            root_post_like= (TextView) itemView.findViewById(R.id.root_post_like);
            reply_root_post= (TextView) itemView.findViewById(R.id.reply_root_post);
            read_more_sub_comments= (TextView) itemView.findViewById(R.id.read_more_sub_comments);
            profile_one= (CircleImageView) itemView.findViewById(R.id.profile_subcomment_);
            Subcomment_name= (TextView) itemView.findViewById(R.id.Subcomment_name);
            subcoment_one= (TextView) itemView.findViewById(R.id.subcoment_one);
             profile_subcomment_another= (CircleImageView) itemView.findViewById(R.id.profile_subcomment_another);
              Subcomment_name_another= (TextView) itemView.findViewById(R.id.Subcomment_name_another);
              subcoment_one_another= (TextView) itemView.findViewById(R.id.subcoment_one_another);
              first_subComment = (LinearLayout) itemView.findViewById(R.id.first_subComment);
              next_subcomment = (LinearLayout) itemView.findViewById(R.id.next_subComment);


        }
    }
}
