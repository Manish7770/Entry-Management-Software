package com.example.visitormanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.visitormanagement.Model.HostModel;
import com.example.visitormanagement.Model.VisitorModel;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FButton visitorin,visitorout;
        visitorin=findViewById(R.id.visitorin);
        visitorout=findViewById(R.id.visitorout);

        visitorin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonData.visitordetails=new VisitorModel();
                CommonData.selectedhost=new HostModel();
                Intent intent = new Intent(MainActivity.this, VisitorIn.class);
                startActivity(intent);
            }
        });

        visitorout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VisitorOut.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
