package com.image.imagenext.imagenextbig;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.image.imagenext.imagenextbig.Adapter.CommentRecyclerAdapter;
import com.image.imagenext.imagenextbig.Models.RootComment;
import com.image.imagenext.imagenextbig.Models.SubComment;
import com.image.imagenext.imagenextbig.Models.comment;
import com.image.imagenext.imagenextbig.Models.upload;
import com.image.imagenext.imagenextbig.Models.user;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
TextView count_ofLike_description;
ImageView image_like_view;
RecyclerView recyclerView_comment;
EditText make_a_comment;
String post_id;
TextView text_loiek_d;
ImageView commit_for_sure;
String profile_url, profile_name;
ArrayList<String> rootComment_id= new ArrayList<>();
String user_id;
    String string6="";
    String string7="";
    String string8="";
    String string9="";
    String string10="";
    String string11="";
        ArrayList<RootComment> RootCommentList = new ArrayList<>();
;
boolean isPostliked = false;
String like_count;                                int count =0;

    ImageView image_like;
CommentRecyclerAdapter adapter;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        post_id = getIntent().getStringExtra("postkey_id");
        like_count = getIntent().getStringExtra("like_count");
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapter = new CommentRecyclerAdapter(getApplicationContext(), post_id,RootCommentList);
        recyclerView_comment= (RecyclerView) findViewById(R.id.recycler_comment);
    image_like= (ImageView) findViewById(R.id.image_like);
        image_like_view= (ImageView) findViewById(R.id.image_like);
        make_a_comment = (EditText) findViewById(R.id.make_a_comment);
        count_ofLike_description= (TextView) findViewById(R.id.text_loiek_d);
        commit_for_sure = (ImageView) findViewById(R.id.commit_for_sure);
    text_loiek_d= (TextView) findViewById(R.id.text_loiek_d);
    text_loiek_d.setText(like_count +" people likes it");
        recyclerView_comment.setLayoutManager(new LinearLayoutManager(this));
recyclerView_comment.setAdapter(adapter);
FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("likeId").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){
            image_like.setImageResource(R.mipmap.ic_liked_icon);
            isPostliked=true;
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    image_like.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (isPostliked){
              FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("likeId").child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      isPostliked = false;
                      DecreaseLikeCount(post_id);
                      image_like.setImageResource(R.mipmap.ic_image_like);
                  }
              });
          }else{
              FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("likeId").child(user_id).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                     IncreaseLikeCount(post_id);
                      isPostliked=true;
                      image_like.setImageResource(R.mipmap.ic_liked_icon);

                  }
              });
          }
         }
    });


FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("root_comment_id").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){
            rootComment_id = (ArrayList<String>) dataSnapshot.getValue();
            Collections.reverse(rootComment_id);
            creatEViewsRootComment();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    text_loiek_d.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), LikeActivity.class);
            intent.putExtra("post_id", post_id);
            startActivity(new Intent(intent));
        }
    });

        commit_for_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(make_a_comment.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Notihing to push", Toast.LENGTH_SHORT).show();
                }else {
                    makeacomment();
                }
            }
        });

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


    private void creatEViewsRootComment() {
     for(int i=0;i<rootComment_id.size();i++){
         Log.e( "creatEViewsRomment: ","looper" );
        final String root_comment_id = rootComment_id.get(i);
         FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("comments").child(rootComment_id.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()){
                   final Map<String, Object> getMap = (Map<String, Object>) dataSnapshot.getValue();
                 // if((Object)getMap.get("subcomments")== null)
                  //{
                      RootComment rootComment = new RootComment(
                              (String)getMap.get(comment.ROOT_COMMENT_NAME),
                              (String)getMap.get(comment.ROOT_COMMENT_USER_ID),
                              (String)getMap.get(comment.ROOT_COMMENT),
                              String.valueOf((Long)getMap.get(comment.ROOT_COMMENT_TIME)),
                              (String)getMap.get(comment.ROOT_COMMENT_IMAGE_URL),
                              "",
                              "",
                              "",
                              "",
                              "",
                              "",
                              root_comment_id

                      );
                   //   CheckForBranchComments(rootComment);
                      adapter.add(rootComment);
                  //}
                     /*else {

DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("comments").child( root_comment_id).child("subcomments");

Query query = database.limitToFirst(2);

                       query.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                count =0;

                               if(dataSnapshot.exists()){
                                   final long child_no = dataSnapshot.getChildrenCount();
                                   for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                       final String string1= (String)getMap.get(comment.ROOT_COMMENT_NAME);
                                       final String string12=  root_comment_id;
                                       final String string2=  (String)getMap.get(comment.ROOT_COMMENT_USER_ID);
                                       final String string3=  (String)getMap.get(comment.ROOT_COMMENT);
                                       final String string4=  String.valueOf((Long)getMap.get(comment.ROOT_COMMENT_TIME));
                                       final String string5=   (String)getMap.get(comment.ROOT_COMMENT_IMAGE_URL);
                                       string6="";
                                       string7="";
                                       string8="";
                                       string9="";
                                       string10="";
                                       string11="";
                                       String key = childSnapshot.getKey();
                                       Log.e( "onDataChange: ",key );
                                       FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("comments").child(root_comment_id).child("subcomments").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                               if(dataSnapshot.exists())
                                               {

                                                   Log.e(  "onDataChange: ","1" );
                                                   Map<String, Object> subcmt = (Map<String, Object>) dataSnapshot.getValue();

                                                   if (count==0)
                                                   {
                                                       Log.e( "onDataChange: ","2" );


                                                       string6= (String)subcmt.get(SubComment.SUB_COMMENT_NAME);
                                                         string7= (String)subcmt.get(SubComment.SUB_COMMENT_IMAGE_URL);

                                                         string8=  (String)subcmt.get(SubComment.SUB_COMMENT);

                                                         if(child_no==1){
                                                             adapter.add(new RootComment(string1,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11,string12));

                                                         }


                                                       count++;
                                                   }else if (count==1){
                                                       Log.e( "onDataChange: ","3" );

                                                       string9= (String)subcmt.get(SubComment.SUB_COMMENT_NAME);

                                                         string10=   (String)subcmt.get(SubComment.SUB_COMMENT_IMAGE_URL);

                                                        string11=   (String)subcmt.get(SubComment.SUB_COMMENT);

                                                       if(child_no==2){
                                                           adapter.add(new RootComment(string1,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11,string12));

                                                       }
                                                   }
                                               }else {
                                                   Log.e( "onDataChange: ","no snap crashing..." );
                                               }


                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError databaseError) {
                                               Log.e( "onDataChange: ","2"+databaseError.getMessage() );

                                           }
                                       });
                                   }

                               }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });

                  }*/

                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
     }
    }
/*
    private void CheckForBranchComments(RootComment rootComment) {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("comments").child(rootCommentArrayList.get(position).getRoot_comment_id()).child("subcomments");
        Query query = databaseReference.limitToFirst(2);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.e( "onDataChange: size ",dataSnapshot.getChildrenCount()+" " );

                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        String key = childSnapshot.getKey();
                        Log.e( "onDataChange: ",key+" " );
                        FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("comments").child(rootCommentArrayList.get(position).getRoot_comment_id()).child("subcomments").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists() && count==0){
                                    Map<String , Object > objectMap = (Map<String, Object>) dataSnapshot.getValue();
                                    rootComment.setSub_comment_one_name((String) objectMap.get(SubComment.SUB_COMMENT_NAME));
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
*/

    private void makeacomment() {
     DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("posts").child(post_id);
     final String key= databaseReference.child("comments").push().getKey();
        Map<String, Object> commentMAp = new HashMap<>();
        commentMAp.put(comment.ROOT_COMMENT_IMAGE_URL, profile_url);
        commentMAp.put(comment.ROOT_COMMENT_NAME, profile_name);
        commentMAp.put(comment.ROOT_COMMENT, make_a_comment.getText().toString());
        commentMAp.put(comment.ROOT_COMMENT_USER_ID, user_id);
commentMAp.put(comment.ROOT_COMMENT_TIME, System.currentTimeMillis());

     databaseReference.child("comments").child(key).setValue(commentMAp).addOnSuccessListener(new OnSuccessListener<Void>() {
         @Override
         public void onSuccess(Void aVoid) {
             FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("root_comment_id").addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if(dataSnapshot.exists()){
                         // if some root comment already exists.
                         ArrayList<String> rootComment_id_list = (ArrayList<String>) dataSnapshot.getValue();
                         rootComment_id_list.add(key);
                         FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("root_comment_id").setValue(rootComment_id_list).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Toast.makeText(getApplicationContext(), "Commented posted", Toast.LENGTH_SHORT).show();
                                 addToadapter(key);
                                 make_a_comment.setText("");

                             }
                         });
                     }else {
                         // TODO when post is commented first time
                         ArrayList<String> first_root_comment = new ArrayList<>();
                         first_root_comment.add(key);
                         FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child("root_comment_id").setValue(first_root_comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Toast.makeText(getApplicationContext(), "Commented posted", Toast.LENGTH_SHORT).show();

                                 addToadapter(key);
                                 make_a_comment.setText("");

                             }
                         });
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
         }
     });
    }

    private void addToadapter(String key) {

       adapter.add(new RootComment(
                profile_name,
                user_id,
                make_a_comment.getText().toString(),
                String.valueOf(System.currentTimeMillis()),
                profile_url,
                "",
                "",
                "",
                "",
                "",
                "",
                key));
       IncreaseCount();

    }

    private void IncreaseCount() {
    FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child(upload.ROOT_COMMENT_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                int count = Integer.parseInt((String)dataSnapshot.getValue());
                count++;
                FirebaseDatabase.getInstance().getReference().child("posts").child(post_id).child(upload.ROOT_COMMENT_COUNT).setValue(String.valueOf(count));

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


}
}
