package com.image.imagenext.imagenextbig;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.image.imagenext.imagenextbig.Adapter.LikeRecyclerAdapter;
import com.image.imagenext.imagenextbig.Models.Like;
import com.image.imagenext.imagenextbig.Models.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LikeActivity extends AppCompatActivity {
String post_id;
    ArrayList<String> likeList = new ArrayList<>();
ArrayList<Like> LikerDetailList= new ArrayList<>();
RecyclerView recycler_like;
LikeRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        post_id = getIntent().getStringExtra("post_id");
        recycler_like = (RecyclerView) findViewById(R.id.recycler_like);
        adapter= new LikeRecyclerAdapter(getApplicationContext(), LikerDetailList);
        recycler_like.setLayoutManager(new LinearLayoutManager(this));
        recycler_like.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("likeId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot chiSnapshot : dataSnapshot.getChildren()) {
                        likeList.add(chiSnapshot.getKey());
                    }
                    Prepare_Detail_Of_Likers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Prepare_Detail_Of_Likers() {
        for(final String user_id : likeList)

            FirebaseDatabase.getInstance().getReference().child("user").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        Map<String, Object> likerDetail = (Map<String, Object>) dataSnapshot.getValue();
                          adapter.add(
                                  new Like(user_id, (String) likerDetail.get(user.PROFILE_IMAGE_URL), (String)likerDetail.get(user.NAME))

                          );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
}
