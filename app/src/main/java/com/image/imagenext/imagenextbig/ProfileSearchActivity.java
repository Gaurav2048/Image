package com.image.imagenext.imagenextbig;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.image.imagenext.imagenextbig.Adapter.UserREcyclerAdapter;
import com.image.imagenext.imagenextbig.Models.user;
import com.image.imagenext.imagenextbig.Models.usermodel;

import java.util.ArrayList;
import java.util.Map;

public class ProfileSearchActivity extends AppCompatActivity {
RecyclerView user_recycler;
UserREcyclerAdapter adapter;
ArrayList<usermodel> userArrayList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_search);
        user_recycler = (RecyclerView) findViewById(R.id.user_recycler);
        adapter= new UserREcyclerAdapter(getApplicationContext(), userArrayList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        user_recycler.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(user_recycler.getContext(),
                layoutManager.getOrientation());
        DividerItemDecoration dividerItem1Decoration = new DividerItemDecoration(user_recycler.getContext(),
                StaggeredGridLayoutManager.HORIZONTAL);
        user_recycler.addItemDecoration(dividerItemDecoration);
        user_recycler.addItemDecoration(dividerItem1Decoration);
        user_recycler.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){

                        String key = childSnapShot.getKey();
                        Log.e( "onDataChange: ",key );
                        FirebaseDatabase.getInstance().getReference().child("user").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    Map<String, Object> ggg = (Map<String, Object>) dataSnapshot.getValue();
                                    adapter.add(new usermodel((String)ggg.get(user.NAME),(String)ggg.get(user.USERNAME),(String)ggg.get(user.PROFILE_IMAGE_URL),(String)ggg.get(user.FOLLOWER),String.valueOf((Object)ggg.get(user.FOLLOWING))));
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
    }
}
