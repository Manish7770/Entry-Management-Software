package com.example.reception;

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

import com.example.reception.Model.VisitorModel;
import com.example.reception.RecyclerViewAdapter.VisitorViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VisitorOut extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference visitorOut;

    public EditText searchbar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    VisitorViewAdapter visitoradapter;
    List<VisitorModel> startlist;

    ProgressBar progressBar;
    CardView novisitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitorout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Visitors Out");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recycler2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        database = FirebaseDatabase.getInstance();
        visitorOut = database.getReference("Visitors-Out");

        novisitor=findViewById(R.id.novisitor);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        novisitor.setVisibility(View.INVISIBLE);

        searchbar=findViewById(R.id.searchbar);

        searchbar.addTextChangedListener(new TextWatcher() {
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

        visitorOut.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                startlist.clear();
                startlist = new ArrayList();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if(snapshot.child("Visitors").exists())
                    {
                        for(DataSnapshot snapshot1 : snapshot.child("Visitors").getChildren())
                        {
                            VisitorModel newvisitor=new VisitorModel();
                            newvisitor.setHostName(snapshot.child("name").getValue().toString());
                            newvisitor.setVisitorName(snapshot1.child("name").getValue().toString());
                            newvisitor.setVisEmail(snapshot1.child("email").getValue().toString());
                            newvisitor.setVisPhone(snapshot1.child("phone").getValue().toString());
                            newvisitor.setTimestmp(Long.parseLong(snapshot1.getKey()));
                            String checkin="";
                            checkin+=CommonData.getDate(Long.parseLong(snapshot1.getKey()));
                            checkin+=" "+CommonData.getTime(Long.parseLong(snapshot1.getKey()));
                            String checkout=snapshot1.child("checkOut").getValue().toString();
                            newvisitor.setCheckInTime(checkin);
                            newvisitor.setCheckOutTime(checkout);
                            startlist.add(newvisitor);
                        }
                    }
                }
                Collections.sort(startlist, new Comparator<VisitorModel>() {
                    public int compare(VisitorModel c1, VisitorModel c2) {
                        if (c1.getTimestmp() > c2.getTimestmp()) return -1;
                        if (c1.getTimestmp() < c2.getTimestmp()) return 1;
                        return 0;
                    }});

                if(startlist.isEmpty())
                    novisitor.setVisibility(View.VISIBLE);
                else
                    novisitor.setVisibility(View.INVISIBLE);

                progressBar.setVisibility(View.INVISIBLE);

                visitoradapter = new VisitorViewAdapter(VisitorOut.this,startlist);
                recyclerView.setAdapter(visitoradapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(VisitorOut.this, Dashboard.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisitorOut.this, Dashboard.class);
        startActivity(intent);
    }

    void filter(String text){
        List<VisitorModel> temp = new ArrayList();
        for(VisitorModel d: startlist){
            if((d.getVisitorName().toLowerCase()).contains(text.toLowerCase())||(d.getCheckInTime().toLowerCase()).contains(text.toLowerCase())||(d.getCheckOutTime().toLowerCase()).contains(text.toLowerCase())||(d.getHostName().toLowerCase()).contains(text.toLowerCase())||(d.getVisEmail().toLowerCase()).contains(text.toLowerCase())||(d.getVisPhone().toLowerCase()).contains(text.toLowerCase())){
                temp.add(d);
            }
        }

        if(temp.isEmpty())
            novisitor.setVisibility(View.VISIBLE);
        else
            novisitor.setVisibility(View.INVISIBLE);

        progressBar.setVisibility(View.INVISIBLE);

        visitoradapter.updateList(temp);
    }
}