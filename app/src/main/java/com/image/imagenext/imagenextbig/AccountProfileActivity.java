package com.image.imagenext.imagenextbig;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.image.imagenext.imagenextbig.Adapter.ProfileRecyclerAdapter;
import com.image.imagenext.imagenextbig.Models.upload;
import com.image.imagenext.imagenextbig.Models.user;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountProfileActivity extends AppCompatActivity {
RecyclerView recycler_main;
CircleImageView profile_pic;
String userId;
ArrayList<String> urlPost = new ArrayList<>();
TextView profile_name,userNmae;
ArrayList<String> PostIdList = new ArrayList<>();
ProfileRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Slabo27px-Regular.ttf");

adapter = new ProfileRecyclerAdapter(getApplicationContext(), urlPost);
        profile_pic= (CircleImageView) findViewById(R.id.profile_pic);
        profile_name= (TextView) findViewById(R.id.profile_name);
        userNmae= (TextView) findViewById(R.id.userNmae);
        profile_name.setTypeface(typeface);
        userNmae.setTypeface(typeface);
        recycler_main= (RecyclerView) findViewById(R.id.recycler_main);
        recycler_main.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
recycler_main.setAdapter(adapter);
userId = getIntent().getStringExtra("userid");

FirebaseDatabase.getInstance().getReference().child("user").child(userId).child("postIds").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){
            PostIdList = (ArrayList<String>) dataSnapshot.getValue();
             loadPost();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

        FirebaseDatabase.getInstance().getReference().child("user").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                    Picasso.get().load((String)userMap.get(user.PROFILE_IMAGE_URL)).placeholder(R.mipmap.ic_def_avater).into(profile_pic);
                    profile_name.setText((String)userMap.get(user.NAME));
                    userNmae.setText((String) userMap.get(user.USERNAME));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadPost() {
        for (String post : PostIdList){
            if(post!=null)
            {
                FirebaseDatabase.getInstance().getReference().child("posts").child(post).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            adapter.add((String)map.get(upload.POST_IMAGE));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
