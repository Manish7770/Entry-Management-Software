package com.example.visitormanagement.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.visitormanagement.CommonData;
import com.example.visitormanagement.Interface.ItemClickListener;
import com.example.visitormanagement.MainActivity;
import com.example.visitormanagement.Model.HostModel;
import com.example.visitormanagement.R;
import com.example.visitormanagement.SelectHost;
import com.example.visitormanagement.ViewHolder.HostViewHolder;
import com.example.visitormanagement.VisitorIn;
import com.example.visitormanagement.VisitorOut;

import java.util.List;

public class HostViewAdapter extends RecyclerView.Adapter<HostViewHolder>{
    public List<HostModel> displayedList;
    private Context context;
    public HostViewAdapter(Context context,List<HostModel> displayedList){
        this.displayedList = displayedList;
        this.context=context;
    }

    @Override
    public HostViewHolder onCreateViewHolder(ViewGroup parent, int position)    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewhost, parent, false);
        return new HostViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(HostViewHolder holder, int position) {
        final HostModel model = displayedList.get(position);
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.address.setText(model.getAddress());
        holder.phone.setText(model.getPhone());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int positon, boolean isLongClick) {
                CommonData.selectedhost=model;
                Intent intent;
                if(CommonData.flag==0)
                    intent = new Intent(context, VisitorIn.class);
                else
                    intent = new Intent(context, VisitorOut.class);

                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return displayedList.size();
    }

    public void updateList(List<HostModel> list){
        displayedList = list;
        notifyDataSetChanged();
    }
}