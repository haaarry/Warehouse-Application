package com.sky.pinkvelocity.warehouse.app;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by hac10 on 27/09/15.
 */
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    List<ItemInfo> data = Collections.emptyList();
    Context context;
    public ItemRecyclerAdapter(Context context, List<ItemInfo> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemInfo current = data.get(position);
        String quant = "" +current.quantity;
        holder.itemTV.setText(current.itemName);
        holder.imageV.setImageResource(current.iconId);
        holder.locationTV.setText(current.location);

        holder.quantityTV.setText(quant);//Set to quantity//THIS COULD BREAK IT
        //holder.quantityTV.setText(current.quantity.toString());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        TextView itemTV;
        TextView locationTV;
        ImageView imageV;
        TextView quantityTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemTV = (TextView) itemView.findViewById(R.id.itemRecyclerText);
            imageV = (ImageView) itemView.findViewById(R.id.listIcon);
            locationTV =(TextView) itemView.findViewById(R.id.itemLocationRecyclerText);
            imageV.setOnLongClickListener(this);
            quantityTV = (TextView) itemView.findViewById(R.id.itemQuantTextView);
        }

//        @Override
//        public void onClick(View v) {
//            Toast.makeText(context, ""+getPosition(), Toast.LENGTH_SHORT).show();
//        }

        @Override
        public boolean onLongClick(View v) {


            return false;
        }
    }
}
