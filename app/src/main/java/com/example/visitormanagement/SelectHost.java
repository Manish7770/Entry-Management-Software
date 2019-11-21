package com.example.visitormanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    DatabaseReference host;

    public EditText findhost;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    HostViewAdapter hostadapter;
    List<HostModel> startlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecthost);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Select Host");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        recyclerView=(RecyclerView)findViewById(R.id.recyclerhost);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        database = FirebaseDatabase.getInstance();
        host = database.getReference("Hosts");

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

        host.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HostModel host3 = snapshot.getValue(HostModel.class);
                    startlist.add(host3);
                }

                hostadapter = new HostViewAdapter(SelectHost.this,startlist);
                recyclerView.setAdapter(hostadapter);
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
        Intent intent = new Intent(SelectHost.this, VisitorIn.class);
        startActivity(intent);
    }

    void filter(String text){
        List<HostModel> temp = new ArrayList();
        for(HostModel d: startlist){
            //or use .equal(text) with you want equal match
            if((d.getName().toLowerCase()).contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        hostadapter.updateList(temp);
    }

}

