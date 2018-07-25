package com.image.imagenext.imagenextbig.Adapter;

  import android.app.Activity;
  import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.adapters.MainImageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.image.imagenext.imagenextbig.AccountProfileActivity;
import com.image.imagenext.imagenextbig.CommentActivity;
import com.image.imagenext.imagenextbig.Models.Image;
import com.image.imagenext.imagenextbig.Models.upload;
import com.image.imagenext.imagenextbig.Models.user;
import com.image.imagenext.imagenextbig.R;
import com.image.imagenext.imagenextbig.utils.TimeTeller;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saurav on 7/22/2018.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.viewHolder> {
    ArrayList<Image> ImageList;
    Context context;
 String user_id;
 Activity activity;
    public ImageRecyclerAdapter(Context context, ArrayList<Image> ImageList,Activity activity ) {
    this.context =context;
    this.ImageList=ImageList;
    user_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
    this.activity=activity;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Slabo27px-Regular.ttf");
        holder.unit_profile_username.setTypeface(typeface);
        holder.unit_comment.setTypeface(typeface);
        holder.unit_time.setTypeface(typeface);
        holder.status.setTypeface(typeface);
        holder.status_detail.setTypeface(typeface);
        holder.location.setTypeface(typeface);

        holder.unit_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Facebook", "Twitter", "Whatsapp"};

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        holder.unit_profile_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountProfileActivity.class);
                intent.putExtra("userid", ImageList.get(position).getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
             holder.like_post.setText(ImageList.get(position).getLike());
holder.unit_comment.setText(ImageList.get(position).getCommentCount());
        FirebaseDatabase.getInstance().getReference().child("user").child(ImageList.get(position).getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    Picasso.get().load((String)data.get(user.PROFILE_IMAGE_URL)).placeholder(R.mipmap.ic_def_avater).into(holder.profile_image);
                    holder.unit_profile_username.setText((String)data.get(user.USERNAME));
                    holder.status.setText((String)data.get(user.USERNAME));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Picasso.get().load(ImageList.get(position).getPostImage()).placeholder(R.mipmap.ic_image_avater).into(holder.unit_interest);
holder.unit_time.setText(TimeTeller.getTimeAgo(Long.parseLong(ImageList.get(position).getTime())));
if(ImageList.get(position).getLocation().equals("null")){
    holder.location.setText("Location hidden");
}else
{
    holder.location.setText(ImageList.get(position).getLocation());
}
        holder.status_detail.setText(ImageList.get(position).getCaption());

isLiked(ImageList.get(position).getPostKey(), holder);



holder.unit_save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(context, user.WAIT, Toast.LENGTH_SHORT).show();
    }
});
holder.like_post.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        FirebaseDatabase.getInstance().getReference().child("posts").child(ImageList.get(position).getPostKey()).child("likeId").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    FirebaseDatabase.getInstance().getReference().child("posts").child(ImageList.get(position).getPostKey()).child("likeId").child(user_id).removeValue();
                    holder.like_post.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_image_like,0);
                    DecreaseLikeCount(ImageList.get(position).getPostKey());
                    int count = Integer.parseInt(holder.like_post.getText().toString());
                    count--;
                    holder.like_post.setText(String.valueOf(count));

                }else {
                    FirebaseDatabase.getInstance().getReference().child("posts").child(ImageList.get(position).getPostKey()).child("likeId").child(user_id).setValue(true);
                    holder.like_post.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_liked_icon,0);
                    IncreaseLikeCount(ImageList.get(position).getPostKey());
                    int count = Integer.parseInt(holder.like_post.getText().toString());
                    count++;
                    holder.like_post.setText(String.valueOf(count));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
});
holder.unit_comment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("postkey_id", ImageList.get(position).getPostKey());
        intent.putExtra("like_count", ImageList.get(position).getLike());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
});

    }

    private void DecreaseLikeCount(final String key) {
        FirebaseDatabase.getInstance().getReference().child("posts").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> map_or_post = (Map<String, Object>) dataSnapshot.getValue();
                    int likeCount = Integer.parseInt((String)map_or_post.get(upload.LIKE));
                    likeCount--;
                    Map<String, Object > likeMAp = new HashMap<>();
                    likeMAp.put(upload.LIKE, String.valueOf(likeCount));
                    FirebaseDatabase.getInstance().getReference().child("posts").child(key).updateChildren(likeMAp);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void IncreaseLikeCount(final String key) {
        FirebaseDatabase.getInstance().getReference().child("posts").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> map_or_post = (Map<String, Object>) dataSnapshot.getValue();
                    int likeCount = Integer.parseInt((String)map_or_post.get(upload.LIKE));
                    likeCount++;
                    Map<String, Object > likeMAp = new HashMap<>();
                    likeMAp.put(upload.LIKE, String.valueOf(likeCount));
                    FirebaseDatabase.getInstance().getReference().child("posts").child(key).updateChildren(likeMAp);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isLiked(String key, final viewHolder holder) {


        FirebaseDatabase.getInstance().getReference().child("posts").child(key).child("likeId")
                .child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    holder.like_post.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_liked_icon,0);

                }else {
                    holder.like_post.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_image_like,0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     }

    public void add(Image image){
        ImageList.add(image);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
CircleImageView profile_image;
TextView unit_profile_username,unit_comment;
ImageView unit_interest;
ImageView unit_save;
TextView unit_time,status,location,status_detail;
ImageView  unit_share;
TextView like_post;
        public viewHolder(View itemView) {
            super(itemView);
            profile_image = (CircleImageView) itemView.findViewById(R.id.profile_image);
            unit_profile_username = (TextView) itemView.findViewById(R.id.unit_profile_username);
            unit_interest= (ImageView) itemView.findViewById(R.id.unit_interest);
            unit_save = (ImageView) itemView.findViewById(R.id.unit_save);
            unit_share= (ImageView) itemView.findViewById(R.id.unit_share);
            unit_comment= (TextView) itemView.findViewById(R.id.unit_comment);
            unit_time = (TextView) itemView.findViewById(R.id.unit_time);
            like_post= (TextView) itemView.findViewById(R.id.like_post);
            status= (TextView) itemView.findViewById(R.id.status);
            location=(TextView) itemView.findViewById(R.id.location);
            status_detail=(TextView) itemView.findViewById(R.id.status_detail);



        }
    }
}
