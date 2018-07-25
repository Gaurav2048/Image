package com.image.imagenext.imagenextbig;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.image.imagenext.imagenextbig.Models.Image;
import com.image.imagenext.imagenextbig.Models.upload;
import com.image.imagenext.imagenextbig.Models.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareActivity extends AppCompatActivity {
ImageView sharable_image;
      byte[] array;
      EditText post_caption;
      String user_id ;
      String location="null";
      double latitude= 0;
      TextView location_name;
      double longitude=0;
      ImageView canceladdress;
      ProgressDialog progressDialog;
RelativeLayout layout_place;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

            array = getIntent().getByteArrayExtra("uri");
        layout_place = (RelativeLayout) findViewById(R.id.two_layout);
            sharable_image= (ImageView) findViewById(R.id.sharable_image);
        post_caption = (EditText) findViewById(R.id.status_caption);
        location_name = (TextView) findViewById(R.id.location);
        progressDialog = new ProgressDialog(ShareActivity.this);
        progressDialog.setTitle("uploading...");
        progressDialog.setCancelable(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        canceladdress= (ImageView) findViewById(R.id.canceladdress);
        canceladdress.setVisibility(View.GONE);
canceladdress.setEnabled(false);
toolbar.inflateMenu(R.menu.postmenu);
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (array!=null){
                    progressDialog.show();
                    postToImage();
                }

                return false;
            }
        });
        layout_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(ShareActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        canceladdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = "null";
                latitude =0;
                longitude=0;
                location_name.setText("");
                canceladdress.setVisibility(View.GONE);
                canceladdress.setEnabled(false);
            }
        });
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);

        sharable_image.setImageBitmap(bitmap);
    }

    private void postToImage() {

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("posts");
            final String key = databaseReference.push().getKey();
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("post").child(key);

            UploadTask uploadTask = filepath.putBytes(array);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    downloadUri.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Map newImage = new HashMap();
                            newImage.put(upload.CAPTION , post_caption.getText().toString());
                            newImage.put(upload.TIME,  String.valueOf(System.currentTimeMillis()));
                            newImage.put(upload.LOCATION, location);
                            newImage.put(upload.USER_ID, user_id);
                            newImage.put(upload.LATITUDE, String.valueOf(latitude));
                            newImage.put(upload.LONGITUDE, String.valueOf(longitude));
                            newImage.put(upload.ROOT_COMMENT_COUNT,"0");
                            Log.e( "onComplete: ",task.getResult().toString() );
                            Uri imri = task.getResult();
                           newImage.put(upload.LIKE, "0");
                            newImage.put(upload.POST_IMAGE, imri.toString());
                          databaseReference.child(key).setValue(newImage);

                          createReference(key);

                            return;
                        }
                    });

                }
            });
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

    }

    private void createReference(final String key) {
 FirebaseDatabase.getInstance().getReference().child("user").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
     @Override
     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
         if (dataSnapshot.exists()){
             Map<String,Object> data = (Map) dataSnapshot.getValue();
             int count = Integer.valueOf((String) data.get(user.POSTCOUNT));


             count++;
             Map<String, Object> update= new HashMap<>();
             update.put(user.POSTCOUNT,String.valueOf(count));
 DatabaseReference database=  FirebaseDatabase.getInstance().getReference().child("user").child(user_id);
database.updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
           FirebaseDatabase.getInstance().getReference().child("user").child(user_id).child(user.POSTIDS).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if(dataSnapshot.exists()){
                      List<String> listcode = (List<String>) dataSnapshot.getValue();
                      listcode.add(key);
                      FirebaseDatabase.getInstance().getReference().child("user").child(user_id).child(user.POSTIDS).setValue(listcode).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              FirebaseDatabase.getInstance().getReference().child("post_location_data").addListenerForSingleValueEvent(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      if(dataSnapshot.exists()){
                                          List<String> post_location = (List<String>) dataSnapshot.getValue();
                                          post_location.add(key);
                                          FirebaseDatabase.getInstance().getReference().child("post_location_data").setValue(post_location).addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void aVoid) {
                                                  progressDialog.dismiss();
                                                  Intent returnIntent = new Intent();
                                                  setResult(500,returnIntent);
                                                  finish();

                                              }
                                          });
                                      }else {
                                          ArrayList<String> data_list = new ArrayList<>();
                                          data_list.add(key);
                                          FirebaseDatabase.getInstance().getReference().child("post_location_data").setValue(data_list).addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void aVoid) {



                                                  Intent returnIntent = new Intent();
                                                  setResult(500,returnIntent);
                                                  finish();
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

                  }else {

                      ArrayList<String> list = new ArrayList<>();
                      list.add(key);
                      FirebaseDatabase.getInstance().getReference().child("user").child(user_id).child(user.POSTIDS).setValue(list).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              FirebaseDatabase.getInstance().getReference().child("post_location_data").addListenerForSingleValueEvent(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      if(dataSnapshot.exists()){
                                          List<String> post_location = (List<String>) dataSnapshot.getValue();
                                          post_location.add(key);
                                          FirebaseDatabase.getInstance().getReference().child("post_location_data").setValue(post_location).addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void aVoid) {
                                                  Intent returnIntent = new Intent();
                                                  setResult(500,returnIntent);
                                                  finish();

                                              }
                                          });
                                      }else {
                                          ArrayList<String> data_list = new ArrayList<>();
                                          data_list.add(key);
                                          FirebaseDatabase.getInstance().getReference().child("post_location_data").setValue(data_list).addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void aVoid) {



                                                  Intent returnIntent = new Intent();
                                                  setResult(500,returnIntent);
                                                  finish();
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
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });

    }
});

          }
     }

     @Override
     public void onCancelled(@NonNull DatabaseError databaseError) {

     }
 });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                location = place.getName().toString();
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                location_name.setText(location);
                canceladdress.setVisibility(View.VISIBLE);
                canceladdress.setEnabled(true);
                Log.i("TAG", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
