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

        holder.itemTV.setText(current.itemName);
        holder.imageV.setImageResource(current.iconId);
        holder.locationTV.setText(current.location);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemTV;
        TextView locationTV;
        ImageView imageV;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemTV = (TextView) itemView.findViewById(R.id.itemRecyclerText);
            imageV = (ImageView) itemView.findViewById(R.id.listIcon);
            locationTV =(TextView) itemView.findViewById(R.id.itemLocationRecyclerText);
            imageV.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, ""+getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
