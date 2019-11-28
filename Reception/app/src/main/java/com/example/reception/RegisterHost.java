package com.example.reception;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.reception.Model.NewHostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import info.hoang8f.widget.FButton;

public class RegisterHost extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference host,visitorOut;

    public EditText fullname,address,email,phonenumber;
    public FButton register;
    ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newhost);

        Toolbar toolbar = (Toolbar) findViewById(R.id.addressToolbar);
        toolbar.setTitle("Register New Host");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        host = database.getReference("Hosts");
        visitorOut = database.getReference("Visitors-Out");

        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        phonenumber=findViewById(R.id.contactnumber);
        register=findViewById(R.id.registernew);
        fullname=findViewById(R.id.fullname);

        mdialog = new ProgressDialog(RegisterHost.this);
        mdialog.setMessage("Please Wait...");
        mdialog.dismiss();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.show();
                mdialog.setCanceledOnTouchOutside(false);
                if ((!(fullname.getText().toString().isEmpty())) && (!(fullname.getText().toString().equals("")))) {

                    if ((!(address.getText().toString().isEmpty())) && (!(address.getText().toString().equals("")))) {

                        if ((!(email.getText().toString().isEmpty())) && (!(email.getText().toString().equals("")))) {
                            if(email.getText().toString().trim().matches(emailPattern))
                            {
                                if ((!(phonenumber.getText().toString().isEmpty())) && (!(phonenumber.getText().toString().equals("")))) {
                                    if (phonenumber.getText().toString().length() == 10) {
                                        final String email2=email.getText().toString().trim();
                                        final String md5email= md5(email2);
                                        host.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.child(md5email).exists())
                                                {
                                                    mdialog.dismiss();
                                                    Toast.makeText(RegisterHost.this, "Host already exists in the database", Toast.LENGTH_LONG).show();

                                                }
                                                else
                                                {
                                                    NewHostModel newmodel=new NewHostModel(fullname.getText().toString(),email2,address.getText().toString(),phonenumber.getText().toString());
                                                    host.child(md5email).setValue(newmodel);
                                                    visitorOut.child(md5email).setValue(newmodel);
                                                    mdialog.dismiss();
                                                    Toast.makeText(RegisterHost.this, "Host registered in the database", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(RegisterHost.this, Dashboard.class);
                                                    startActivity(intent);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                    else
                                    {
                                        mdialog.dismiss();
                                        Toast.makeText(RegisterHost.this, "Enter 10 digit Phone Number", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    mdialog.dismiss();
                                    Toast.makeText(RegisterHost.this, "Enter your Phone Number", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                mdialog.dismiss();
                                Toast.makeText(RegisterHost.this, "Invalid email address", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            mdialog.dismiss();
                            Toast.makeText(RegisterHost.this, "Enter your Email Id", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        mdialog.dismiss();
                        Toast.makeText(RegisterHost.this, "Enter your Office Address", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    mdialog.dismiss();
                    Toast.makeText(RegisterHost.this, "Enter your Full Name", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(RegisterHost.this, Dashboard.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterHost.this, Dashboard.class);
        startActivity(intent);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {

            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();


            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
