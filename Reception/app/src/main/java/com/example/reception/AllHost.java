package com.example.reception;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reception.Model.NewHostModel;
import com.example.reception.ViewHolder.HostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AllHost extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference visitorIn;
    FirebaseRecyclerAdapter adapter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allhost);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("All Host");
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorlight)  );

        database = FirebaseDatabase.getInstance();
        visitorIn = database.getReference("Visitors-In");

        recyclerView=(RecyclerView)findViewById(R.id.recyclerhost);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        loadHost();

    }

    private void loadHost()
    {
        Query query = visitorIn.orderByKey();

        FirebaseRecyclerOptions<NewHostModel> options =
                new FirebaseRecyclerOptions.Builder<NewHostModel>()
                        .setQuery(query, NewHostModel.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<NewHostModel, HostViewHolder>(options) {

            @NonNull
            @Override
            public HostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardviewhost, parent, false);

                return new HostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final HostViewHolder holder, final int position, @NonNull final NewHostModel model) {

                holder.name.setText(model.getName());
                holder.address.setText(model.getAddress());
                holder.email.setText(model.getEmail());
                holder.phone.setText(model.getPhone());
                adapter.getRef(position).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Visitors").exists())
                        {
                            holder.available.setText("Not Available");
                            holder.available.setTextColor(getResources().getColor(R.color.red));
                        }
                        else
                        {
                            holder.available.setText("Available");
                            holder.available.setTextColor( getResources().getColor(R.color.green));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        };

        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null)
            adapter.startListening();

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(AllHost.this, Dashboard.class);
        startActivity(intent);
        return true;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AllHost.this, Dashboard.class);
        startActivity(intent);
    }
}
