package com.image.imagenext.imagenextbig;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.image.imagenext.imagenextbig.Adapter.SubCommentREcyclerAdapter;
import com.image.imagenext.imagenextbig.Models.BranchComment;
import com.image.imagenext.imagenextbig.Models.RootComment;
import com.image.imagenext.imagenextbig.Models.SubComment;
import com.image.imagenext.imagenextbig.Models.user;
import com.image.imagenext.imagenextbig.utils.TimeTeller;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubCommentActivity extends AppCompatActivity {
String post_id,root_comment_id,user_id;
EditText make_sub_a_comment;
    String profile_url, profile_name;
    ImageView commit_sub_for_sure;
    RootComment root_comment;
    CircleImageView root_comment_profile_img;
     RecyclerView subComment_recycler_widger;
     ArrayList<BranchComment> subCommentArrayList = new ArrayList<>();
     SubCommentREcyclerAdapter adapter;
     TextView name, comment,time_of_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_comment);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Slabo27px-Regular.ttf");

        root_comment_profile_img= (CircleImageView) findViewById(R.id.root_comment_profile_img);
        name = (TextView) findViewById(R.id.name);
        comment = (TextView) findViewById(R.id.comment);
        time_of_post= (TextView) findViewById(R.id.time_of_post);
        name.setTypeface(typeface);
        comment.setTypeface(typeface);
        make_sub_a_comment = (EditText) findViewById(R.id.make_sub_a_comment) ;
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        post_id = getIntent().getStringExtra("post_id");
        root_comment_id= getIntent().getStringExtra("root_comment_id");
        root_comment = (RootComment) getIntent().getSerializableExtra("root_comment");
        Picasso.get().load(root_comment.getRoot_comment_image_url()).placeholder(R.mipmap.ic_def_avater).into(root_comment_profile_img);
        name.setText(root_comment.getRoot_comment_name());
        comment.setText(root_comment.getRoot_comment());
        time_of_post.setText(TimeTeller.getTimeAgo(Long.parseLong(root_comment.getRoot_comment_time())).substring(0,4));
        subComment_recycler_widger = (RecyclerView) findViewById(R.id.subComment_recycler_widger);
        subComment_recycler_widger.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubCommentREcyclerAdapter(getApplicationContext(), subCommentArrayList);
        subComment_recycler_widger.setAdapter(adapter);
        commit_sub_for_sure= (ImageView) findViewById(R.id.commit_sub_for_sure);



        FirebaseDatabase.getInstance().getReference().child("user").child(user_id).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            profile_url = (String)map.get(user.PROFILE_IMAGE_URL);
                            profile_name= (String)map.get(user.USERNAME);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("comments").child(root_comment_id).child("subcomments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        String key = childSnapshot.getKey();
                        FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("comments").child(root_comment_id).child("subcomments").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    Map<String, Object> subcomment = (Map<String, Object>) dataSnapshot.getValue();
                                    adapter.add(new BranchComment((String)subcomment.get(SubComment.SUB_COMMENT_NAME), (String)subcomment.get(SubComment.SUB_COMMENT_USER_ID),(String)subcomment.get(SubComment.SUB_COMMENT),String.valueOf((Object) subcomment.get(SubComment.SUB_COMMENT_TIME)),(String) subcomment.get(SubComment.SUB_COMMENT_IMAGE_URL)));
                                 }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        commit_sub_for_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(make_sub_a_comment.getText().toString())){

                    Toast.makeText(getApplicationContext(), "Nothing to comment", Toast.LENGTH_SHORT).show();

                }else{
                    MakeASubComment();
                }
            }
        });
    }

    private void MakeASubComment() {
        Map<String, Object> subComment = new HashMap<>();
        subComment.put(SubComment.SUB_COMMENT_NAME, profile_name);
        subComment.put(SubComment.SUB_COMMENT_TIME, System.currentTimeMillis());
        subComment.put(SubComment.SUB_COMMENT_IMAGE_URL, profile_url);
        subComment.put(SubComment.SUB_COMMENT, make_sub_a_comment.getText().toString());
        subComment.put(SubComment.SUB_COMMENT_USER_ID, user_id);

        final BranchComment branchComment = new BranchComment(profile_name,user_id,make_sub_a_comment.getText().toString(),String.valueOf(System.currentTimeMillis()),profile_url);
        String key = FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("comments").child(root_comment_id).child("subcomments").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("comments").child(root_comment_id).child("subcomments").child(key).setValue(subComment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
if (task.isSuccessful()){
    adapter.add(branchComment);
    make_sub_a_comment.setText("");

}
            }
        });

    }
}
