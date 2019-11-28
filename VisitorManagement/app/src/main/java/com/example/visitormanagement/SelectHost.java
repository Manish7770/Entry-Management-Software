package com.example.visitormanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visitormanagement.Model.HostModel;
import com.example.visitormanagement.RecyclerViewAdapter.HostViewAdapter;
import com.example.visitormanagement.ViewHolder.HostViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectHost extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference visitorIn;

    public EditText findhost;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    HostViewAdapter hostadapter;
    List<HostModel> startlist;
    List<HostModel> newlist;

    ProgressBar progressBar;
    CardView nohost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecthost);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Select Host");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView=(RecyclerView)findViewById(R.id.recyclerhost);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        database = FirebaseDatabase.getInstance();
        visitorIn = database.getReference("Visitors-In");

        nohost=findViewById(R.id.nohost);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        nohost.setVisibility(View.INVISIBLE);

        findhost=findViewById(R.id.findhost);

        findhost.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        startlist = new ArrayList();

        visitorIn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HostModel host3 = snapshot.getValue(HostModel.class);
                    if(snapshot.child("Visitors").exists())
                        host3.setAvailable(0); //Host is already busy in a meeting
                    else
                        host3.setAvailable(1);
                    startlist.add(host3);
                }

                if(startlist.isEmpty())
                    nohost.setVisibility(View.VISIBLE); //No host Available
                else
                    nohost.setVisibility(View.INVISIBLE);

                progressBar.setVisibility(View.INVISIBLE);

                hostadapter = new HostViewAdapter(SelectHost.this,startlist);
                recyclerView.setAdapter(hostadapter);

                visitorIn.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        newlist=new ArrayList();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HostModel host3 = snapshot.getValue(HostModel.class);
                            if(snapshot.child("Visitors").exists())
                                host3.setAvailable(0);
                            else
                                host3.setAvailable(1);
                            newlist.add(host3);
                        }

                        startlist=newlist;
                        String searched=findhost.getText().toString();
                        filter(searched);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if(CommonData.flag==0)
            intent = new Intent(SelectHost.this, VisitorIn.class);
        else
            intent = new Intent(SelectHost.this, VisitorOut.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent;
        if(CommonData.flag==0)
            intent = new Intent(SelectHost.this, VisitorIn.class);
        else
            intent = new Intent(SelectHost.this, VisitorOut.class);
        startActivity(intent);
        return true;
    }

    void filter(String text){ //implements the search option
        List<HostModel> temp = new ArrayList();
        for(HostModel d: startlist){
            //or use .equal(text) with you want equal match
            if((d.getName().toLowerCase()).contains(text.toLowerCase())||(d.getEmail().toLowerCase()).contains(text.toLowerCase())||(d.getPhone().toLowerCase()).contains(text.toLowerCase())||(d.getAddress().toLowerCase()).contains(text.toLowerCase())){
                temp.add(d);
            }
        }

        if(temp.isEmpty())
            nohost.setVisibility(View.VISIBLE);
        else
            nohost.setVisibility(View.INVISIBLE);

        progressBar.setVisibility(View.INVISIBLE);

        hostadapter.updateList(temp);
    }

}

