package com.image.imagenext.imagenextbig.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.image.imagenext.imagenextbig.FilterAndCropActivity;
import com.image.imagenext.imagenextbig.R;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubfilter;

import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saurav on 7/21/2018.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.viewHolder> {
    ImageCropView cropView;
    int num=10;
    Context context;
    Bitmap mainBitmap;
    String uri;
    Bitmap alternate_bitmap;
    boolean isFilterApplied= false;Activity activity;
    int position_last_view = 87;


    public FilterAdapter(Context context, ImageCropView cropView, String uri,Activity activity) {
    this.context=context;
    this.cropView=cropView;
    this.uri =uri;
    this.activity =activity;





    }
    public Bitmap getBitmap (){
        return  ((FilterAndCropActivity)activity).getimageBitmap(uri);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_filters, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {


         Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.b);
         String string = null;
        Bitmap mutableBitmap = icon.copy(Bitmap.Config.ARGB_8888, true);
        final Filter myFilter = new Filter();
        switch (position)
        {
            case 0:
            myFilter.addSubFilter(new BrightnessSubfilter(50));
            myFilter.addSubFilter(new ContrastSubfilter(3f));
            string = "High contrast";
                break;
            case 1:
                myFilter.addSubFilter(new SaturationSubfilter(1.3f));
                string= "Saturation normal";
                break;
            case 2:
                myFilter.addSubFilter(new SaturationSubfilter(3f));
                string="Saturation medium";
                break;
            case 3:
                myFilter.addSubFilter(new SaturationSubfilter(1.3f));
                string="Subsaturation high";
                break;
            case 4:

                myFilter.addSubFilter(new ColorOverlaySubfilter(250, .5f, .4f, .4f));
                string = "Colored apart";
                break;
            case 5:
                myFilter.addSubFilter(new VignetteSubfilter(context, 100));
                string= "Vigantee";
                break;
            case 6:
                myFilter.addSubFilter(new VignetteSubfilter(context, 50));
                myFilter.addSubFilter(new SaturationSubfilter(2f));
                myFilter.addSubFilter(new ContrastSubfilter(1.9f));
                myFilter.addSubFilter(new ColorOverlaySubfilter(90, .6f,.6f,.6f));
                string= "High mix";
                break;
            case 7:
                myFilter.addSubFilter(new VignetteSubfilter(context, 150));
                myFilter.addSubFilter(new SaturationSubfilter(2.4f));
                myFilter.addSubFilter(new ColorOverlaySubfilter(90, .3f,.4f,.2f));
                string= "Rangoli";
                break;
            case 8:
                myFilter.addSubFilter(new VignetteSubfilter(context, 100));
                myFilter.addSubFilter(new SaturationSubfilter(2f));

                myFilter.addSubFilter(new ColorOverlaySubfilter(90, .6f,.6f,.6f));
                string= "Special";
                break;
            case 9:
                myFilter.addSubFilter(new VignetteSubfilter(context, 75));
                myFilter.addSubFilter(new SaturationSubfilter(2.5f));
                myFilter.addSubFilter(new ContrastSubfilter(2.9f));
                myFilter.addSubFilter(new ColorOverlaySubfilter(110, .8f,.2f,.62f));
                string= "Deep inside";
                break;

        }
        mutableBitmap = myFilter.processFilter(mutableBitmap);

        holder.filter_attach.setImageBitmap(mutableBitmap);
holder.filtername.setText(string);
        holder.filter_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cropView.setImageBitmap(myFilter.processFilter(getBitmap()));

            }
        });}

    @Override
    public int getItemCount() {
        return num;
    }

    public  class viewHolder extends RecyclerView.ViewHolder{
CircleImageView filter_attach;
TextView filtername;
        public viewHolder(View itemView) {
            super(itemView);
            filter_attach= (CircleImageView) itemView.findViewById(R.id.filter_attach);
            filtername = (TextView) itemView.findViewById(R.id.filtername);
        }
    }
}
