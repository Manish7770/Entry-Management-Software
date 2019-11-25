package com.example.reception.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reception.Model.VisitorModel;
import com.example.reception.R;
import com.example.reception.ViewHolder.VisitorViewHolder;

import java.util.List;

public class VisitorViewAdapter extends RecyclerView.Adapter<VisitorViewHolder>{

    public List<VisitorModel> displayedList;
    private Context context;
    public VisitorViewAdapter(Context context,List<VisitorModel> displayedList){
        this.displayedList = displayedList;
        this.context=context;
    }

    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_visitor, parent, false);
        return new VisitorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolder holder, int position) {
        VisitorModel model = displayedList.get(position);
        holder.visitorname.setText(model.getVisitorName());
        holder.visitoremail.setText(model.getVisEmail());
        holder.visitorphone.setText(model.getVisPhone());
        holder.hostname.setText(model.getHostName());
        holder.checkintime.setText(model.getCheckInTime());
        holder.checkouttime.setText(model.getCheckOutTime());
    }

    @Override
    public int getItemCount() {
        return displayedList.size();
    }

    public void updateList(List<VisitorModel> list){
        displayedList = list;
        notifyDataSetChanged();
    }
}
