package com.example.reception.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reception.R;


public class HostViewHolder extends RecyclerView.ViewHolder {

    public TextView name,phone,email,address;

    public HostViewHolder(View itemView) {
        super(itemView);


        name=(TextView) itemView.findViewById(R.id.name);
        phone=(TextView) itemView.findViewById(R.id.contact);
        email=(TextView) itemView.findViewById(R.id.emailid);
        address=(TextView) itemView.findViewById(R.id.address2);
    }

}
