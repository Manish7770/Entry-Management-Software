package com.example.reception.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reception.R;

public class VisitorViewHolder extends RecyclerView.ViewHolder {

    public TextView visitorname,visitorphone,visitoremail,hostname,checkintime,checkouttime;

    public VisitorViewHolder(@NonNull View itemView) {
        super(itemView);

        visitorname=(TextView) itemView.findViewById(R.id.visitorname);
        visitorphone=(TextView) itemView.findViewById(R.id.visitorcontact);
        visitoremail=(TextView) itemView.findViewById(R.id.visitoremailid);
        hostname=itemView.findViewById(R.id.hostname);
        checkintime=itemView.findViewById(R.id.checkintime);
        checkouttime = itemView.findViewById(R.id.checkouttime);
    }
}
