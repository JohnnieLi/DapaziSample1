package com.example.johnnie.ottawainfo.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.ottawainfo.MainActivity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johnnie on 2016-08-28.
 */
public class RecyclerButtonsAdpter extends RecyclerView.Adapter<RecyclerButtonsAdpter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageButton imgBut;


        public ViewHolder(View v) {
            super(v);
            imgBut = (ImageButton)v.findViewById(R.id.imageButton);

        }

    }


    private OnItemClickListener mOnItemClickListener;
    private Map<String,Integer> imageButtonsMap;
    private String[] keys;


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public RecyclerButtonsAdpter(Map<String,Integer> map,OnItemClickListener onItemClickListener) {

        this.imageButtonsMap = map;
        this.mOnItemClickListener = onItemClickListener;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_button_main, parent, false);

        //todo: set the card view size,margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        keys = imageButtonsMap.keySet().toArray(new String[imageButtonsMap.size()]);
        holder.imgBut.setImageResource(imageButtonsMap.get(keys[position]));
        holder.imgBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return imageButtonsMap.size();
    }
}
