package com.example.visitormanagement.ViewHolder;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visitormanagement.Interface.ItemClickListener;
import com.example.visitormanagement.R;

public class HostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name,phone,email,address,available;
    private ItemClickListener itemClickListener;

    public HostViewHolder(View itemView) {
        super(itemView);

        name=(TextView) itemView.findViewById(R.id.name);
        phone=(TextView) itemView.findViewById(R.id.contact);
        email=(TextView) itemView.findViewById(R.id.emailid);
        address=(TextView) itemView.findViewById(R.id.address2);
        available=(TextView) itemView.findViewById(R.id.available);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
