package com.sky.pinkvelocity.warehouse.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by hac10 on 27/09/15.
 */
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    List<ItemInfo> data = Collections.emptyList();

    public ItemRecyclerAdapter(Context context, List<ItemInfo> data){
        inflater = LayoutInflater.from(context);
        this.data = data;

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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemTV;
        ImageView imageV;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemTV = (TextView) itemView.findViewById(R.id.itemRecyclerText);
            imageV = (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
}
