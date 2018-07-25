package com.image.imagenext.imagenextbig;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.image.imagenext.imagenextbig.Models.user;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class NextActivity extends AppCompatActivity {
ImageView unit_profile;
EditText profile_name, profile_user_name, profile_phon_no;
Button Submit;
String user_id;
Bitmap file = null;
Button ok;
String name, username, contaact;
ProgressDialog progress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        unit_profile=(ImageView)findViewById(R.id.iamge_profile);
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        profile_name=(EditText) findViewById(R.id.name);
        profile_user_name=(EditText)findViewById(R.id.username);
        profile_phon_no=(EditText)findViewById(R.id.phone_no);
        Submit = (Button) findViewById(R.id.Submit);
        ok = (Button) findViewById(R.id.OK);
        progress = new ProgressDialog(NextActivity.this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Slabo27px-Regular.ttf");
        profile_name.setTypeface(typeface);
        profile_phon_no.setTypeface(typeface);
        profile_user_name.setTypeface(typeface);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progress.setTitle("Please wait ..");
progress.show();
        FirebaseDatabase.getInstance().getReference().child("user").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Map<String, Object> return_user_data = (Map<String, Object>) dataSnapshot.getValue();
                    Picasso.get().load((String)return_user_data.get(user.PROFILE_IMAGE_URL)).into(unit_profile);
                    profile_name.setText((String)return_user_data.get(user.NAME));
                    profile_user_name.setText((String) return_user_data.get(user.USERNAME));
                    profile_phon_no.setText((String) return_user_data.get(user.PHONENO));
                    progress.dismiss();


                }else {
                    progress.dismiss();
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        unit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pix.start( NextActivity.this,     7               //Activity or Fragment Instance7
                );
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file!=null){
                    uploadImage(file);
                }else {
                    Raw_upload();
                }
            }
        });
    }

    private void Raw_upload() {
        progress.show();
        Map newImage = new HashMap();
        newImage.put(user.NAME, profile_name.getText().toString());
        newImage.put(user.USERNAME, profile_user_name.getText().toString());
        newImage.put(user.PHONENO, profile_phon_no.getText().toString());
        newImage.put(user.POSTCOUNT, "0");
        newImage.put(user.FOLLOWER, "0");
        newImage.put(user.FOLLOWING, 0);


        FirebaseDatabase.getInstance().getReference().child("user").child(user_id).setValue(newImage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "you are welcome", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==7 ){
            if(resultCode== Activity.RESULT_OK){
                ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);



                try {
                    Bitmap compressedImage = new Compressor(getApplicationContext())
                              .setMaxWidth(200)
                              .setMaxHeight(200)
                              .setQuality(50)
                              .setCompressFormat(Bitmap.CompressFormat.JPEG)
                              .compressToBitmap(new File(returnValue.get(0)));

                    unit_profile.setImageBitmap(compressedImage);
                    file = compressedImage;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e( "onActivityResult: ",e.getCause()+" "+e.getMessage() +" ");
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(Bitmap compressedImage) {
        final String user_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference filepath = FirebaseStorage.getInstance().getReference().child("image_profile").child(user_id);

        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        compressedImage.compress(Bitmap.CompressFormat.JPEG, 50, boas);
        byte[] data = boas.toByteArray();
        UploadTask uploadTask = filepath.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e( "onFailure: ",e.getCause()+" "+e.getMessage() );

                return;
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                downloadUri.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Map newImage = new HashMap();
                        newImage.put(user.NAME, profile_name.getText().toString());
                        newImage.put(user.USERNAME, profile_user_name.getText().toString());
                        newImage.put(user.PHONENO, profile_phon_no.getText().toString());
                        newImage.put(user.POSTCOUNT, "0");
                        newImage.put(user.FOLLOWER, "0");
                        newImage.put(user.FOLLOWING, 0);
                        Log.e( "onComplete: ",task.getResult().toString() );
                        Uri imri = task.getResult();
                        newImage.put(user.PROFILE_IMAGE_URL, imri.toString());
                        FirebaseDatabase.getInstance().getReference().child("user").child(user_id).setValue(newImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "you are welcome", Toast.LENGTH_SHORT).show();
                          finish();
                            }
                        });


                        return;
                    }
                });
            }
        });


    }
}
