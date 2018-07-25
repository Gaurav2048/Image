package com.image.imagenext.imagenextbig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.fxn.pix.Pix;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.image.imagenext.imagenextbig.Adapter.ImageRecyclerAdapter;
import com.image.imagenext.imagenextbig.Models.Image;
import com.image.imagenext.imagenextbig.Models.upload;
import com.image.imagenext.imagenextbig.Models.user;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ImageActivity extends AppCompatActivity {
RecyclerView recyclerView;
boolean isBottomReached= false;
ImageView home,collage,notification,profile_person,logout;
CircleImageView circle_a;
 DatabaseReference databaseReference;
 ArrayList<String> upload_list = new ArrayList<>();
ArrayList<Image> ImageList
        = new ArrayList<>();
ImageRecyclerAdapter adapter;
SwipeRefreshLayout swipelayout;
 int LIMIT_PAGE = 10;
 ImageView search_activity;
 int current_page=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        profile_person= (ImageView) findViewById(R.id.profile_person);
        notification= (ImageView) findViewById(R.id.notification);
        swipelayout= (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        collage=(ImageView) findViewById(R.id.collage);
        home= (ImageView) findViewById(R.id.home);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_main);
        circle_a= (CircleImageView) findViewById(R.id.circle_a);
        search_activity= (ImageView) findViewById(R.id.search_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logout= (ImageView) findViewById(R.id.logout);
        adapter =new ImageRecyclerAdapter(getApplicationContext(),ImageList, ImageActivity.this);
        recyclerView.setAdapter(adapter);
        printHashKey(getApplicationContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("post_location_data");
         initLoad();
collage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),user.WAIT, Toast.LENGTH_SHORT).show();
    }
});
      swipelayout.setRefreshing(true);
         swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {


                 upload_list.clear();
                 ImageList.clear();
                 adapter.notifyDataSetChanged();
                 initLoad();
                 LIMIT_PAGE =10;
                 current_page=0;


             }
         });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                LoginManager.getInstance().logOut();
                  finish();
            }
        });

circle_a.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), FilterAndCropActivity.class);
        startActivity(intent);

    }
});
        search_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), ProfileSearchActivity.class));
             }
        });
profile_person.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AccountProfileActivity.class);
        intent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        startActivity(intent);
    }
});
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    //you have reached to the bottom of your recycler view
                    if(isBottomReached==false)
                    {if(upload_list.size()>ImageList.size())
                    {
                        loadMore();
                    }
                        Log.e( "onScrolled: ","reached" + lastVisible);
                    isBottomReached=true;
                    }
                }
            }
        });
    }
    public  void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e(" ", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(" ", "printHashKey()", e);
        } catch (Exception e) {
            Log.e(" ", "printHashKey()", e);
        }
    }
    private void loadMore( ) {

        for(int i=0;i<LIMIT_PAGE;i++){

            if(upload_list.size()!=LIMIT_PAGE*current_page+i )
            {
                final int count = LIMIT_PAGE*current_page+i;
                FirebaseDatabase.getInstance().getReference().child("posts").child(upload_list.get(current_page*LIMIT_PAGE+i)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Map<String, Object>  post = (Map<String, Object>) dataSnapshot.getValue();
                            Image image = new Image((String)post.get(upload.CAPTION),
                                    (String)post.get(upload.LATITUDE),
                                    (String) post.get(upload.LONGITUDE),
                                    (String) post.get(upload.LOCATION),
                                    (String)   post.get(upload.POST_IMAGE),
                                    (String) post.get(upload.TIME),
                                    (String)  post.get(upload.USER_ID),
                                    upload_list.get(count),
                                    (String) post.get(upload.LIKE),
                                    (String) post.get(upload.ROOT_COMMENT_COUNT)
                            );
                            adapter.add(image);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }else {
                Toast.makeText(getApplicationContext(), "No more", Toast.LENGTH_SHORT).show();
                break;
            }
        }
 isBottomReached=false;
        Log.e( "loadMore: ","loadmor" );
        swipelayout.setEnabled(true);
      current_page++;
    }
public void initLoad(){
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                upload_list= (ArrayList<String>) dataSnapshot.getValue();
                Collections.reverse(upload_list);
                loadMore();
                swipelayout.setEnabled(false);
                swipelayout.setRefreshing(false);

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
}
