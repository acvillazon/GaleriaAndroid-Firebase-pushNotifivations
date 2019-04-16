package com.asaad.firebase.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.MyViewHolder> {

    public JSONArray images;
    public Context context;

    public AdapterImage(Context context, JSONArray iamges) {
        this.images = iamges;
        this.context=context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.content_image,viewGroup,false);

        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;

        /*View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;*/
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try{
            ImageView imageView = myViewHolder.imageView;
            Glide.with(context)
                    .load(images.get(i))
                    .centerCrop()
                    .override(200)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.common_full_open_on_phone)
                    .into(imageView);
        }catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return images.length();
    }

}
