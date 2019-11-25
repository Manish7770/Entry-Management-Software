package com.example.reception;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reception.Model.VisitorModel;
import com.example.reception.RecyclerViewAdapter.VisitorViewAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    FirebaseDatabase database;
    DatabaseReference visitorIn;

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
        setContentView(R.layout.activity_visitor_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Visitors In");
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
        visitorIn = database.getReference("Hosts");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        novisitor=findViewById(R.id.novisitor);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        novisitor.setVisibility(View.INVISIBLE);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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

        visitorIn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long currentTime2=System.currentTimeMillis();
                String today=CommonData.getDate(currentTime2);
                startlist.clear();
                startlist = new ArrayList();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if(snapshot.child("Visitors").exists())
                    {
                        for(DataSnapshot snapshot1 : snapshot.child("Visitors").getChildren())
                        {
                            String checkin="";
                            checkin+=CommonData.getDate(Long.parseLong(snapshot1.getKey()));
                            if(checkin.equals(today))
                            {
                                checkin+=" "+CommonData.getTime(Long.parseLong(snapshot1.getKey()));

                                VisitorModel newvisitor=new VisitorModel();
                                newvisitor.setHostName(snapshot.child("name").getValue().toString());
                                newvisitor.setVisitorName(snapshot1.child("name").getValue().toString());
                                newvisitor.setVisEmail(snapshot1.child("email").getValue().toString());
                                newvisitor.setVisPhone(snapshot1.child("phone").getValue().toString());
                                newvisitor.setTimestmp(Long.parseLong(snapshot1.getKey()));

                                String checkout="";
                                newvisitor.setCheckInTime(checkin);
                                newvisitor.setCheckOutTime(checkout);
                                startlist.add(newvisitor);
                            }
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

                visitoradapter = new VisitorViewAdapter(Dashboard.this,startlist);
                recyclerView.setAdapter(visitoradapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.allhost) {
            Intent intent = new Intent(Dashboard.this, AllHost.class);
            startActivity(intent);

        } else if (id == R.id.newhost) {

            Intent intent = new Intent(Dashboard.this, RegisterHost.class);
            startActivity(intent);

        } else if (id==R.id.Dashboard){
        } else if (id==R.id.visitorout){
            Intent intent = new Intent(Dashboard.this, VisitorOut.class);
            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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