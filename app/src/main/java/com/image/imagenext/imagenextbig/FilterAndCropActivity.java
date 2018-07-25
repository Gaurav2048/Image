package com.image.imagenext.imagenextbig;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.fxn.pix.Pix;
import com.image.imagenext.imagenextbig.Adapter.FilterAdapter;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class FilterAndCropActivity extends AppCompatActivity {
ImageCropView imageCropView;
String path;
FilterAdapter adapter;Bitmap compressedImage;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_and_crop);

            System.loadLibrary("NativeImageProcessor");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.cropmenu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.proceed)
                {
                    if(path!=null)
                    {
                        Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                        intent.putExtra("uri",getArrayByte() );
                        startActivityForResult(intent, 500);

                    }}
                return false;
            }
        });
        Pix.start( FilterAndCropActivity.this,     7               //Activity or Fragment Instance7
        );
        imageCropView= (ImageCropView) findViewById(R.id.view);
         imageCropView.setGridInnerMode(ImageCropView.GRID_ON);
        imageCropView.setGridOuterMode(ImageCropView.GRID_ON);
        imageCropView.setAspectRatio(4, 3);


          recyclerView = (RecyclerView) findViewById(R.id.rec_filter);
recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

    }

    private byte[] getArrayByte(){
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        imageCropView.getCroppedImage().compress(Bitmap.CompressFormat.JPEG, 100, boas);
        byte[] data = boas.toByteArray();
        return data;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 7) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            imageCropView.setImageBitmap( getimageBitmap(returnValue.get(0)));
 path=returnValue.get(0);
                adapter = new FilterAdapter(getApplicationContext(), imageCropView,returnValue.get(0), FilterAndCropActivity.this);
                recyclerView.setAdapter(adapter);




        }else if(resultCode==500){
            finish();
        }
    }

    public Bitmap getimageBitmap (String returnval){
        Bitmap bitmap= null;
        try {
             bitmap = new Compressor(getApplicationContext())
                    .setMaxWidth(250)
                    .setMaxHeight(250)
                    .setQuality(70)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToBitmap(new File(returnval));

         } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        return mutableBitmap;
    }
}
