package com.example.johnnie.ottawainfo.list;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.model.DealerModel;

import java.util.List;

/**
 * Created by Johnnie on 2016-08-25.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {

    private List<DealerModel> models;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView img;
        public TextView info;
        public TextView address;
        public TextView faq;

        public ViewHolder(View v) {
            super(v);
            info = (TextView)v.findViewById(R.id.card_info);
            img = (ImageView)v.findViewById(R.id.card_img);
            address =  (TextView)v.findViewById(R.id.card_address);
            faq = (TextView)v.findViewById(R.id.card_faq);

        }

    }

    public RecyclerListAdapter(List<DealerModel> models) {
                this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card, parent, false);

      //todo: set the card view size,margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DealerModel model = models.get(position);

        holder.info.setText(model.getInformation());
        holder.address.setText(model.getAddress());
        holder.faq.setText(model.getFAQ());
        holder.img.setImageResource(R.drawable.dealer_img_sample);

    }


    @Override
    public int getItemCount() {
        return models.size();
    }
}
