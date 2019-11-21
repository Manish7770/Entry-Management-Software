package com.example.visitormanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.visitormanagement.Email.SendMailTask;
import com.example.visitormanagement.Model.VisitorModelOnly;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import info.hoang8f.widget.FButton;

public class VisitorIn extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference host,emailprovider;

    public EditText fullname,email,phonenumber;
    public TextView hostname;
    public ImageView search;
    public FButton visitinbutton;

    private String name,emailid,phone,hostname2;

    ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitorin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.addressToolbar);
        toolbar.setTitle("Visitor In");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email=findViewById(R.id.email);
        phonenumber=findViewById(R.id.contactnumber);
        hostname=findViewById(R.id.hostname);
        fullname=findViewById(R.id.fullname);
        search=findViewById(R.id.search);
        visitinbutton=findViewById(R.id.visitorinbutton);

        name= CommonData.visitordetails.getName();
        emailid=CommonData.visitordetails.getEmail();
        phone=CommonData.visitordetails.getPhone();
        hostname.setText(CommonData.selectedhost.getName());
        fullname.setText(name);
        email.setText(emailid);
        phonenumber.setText(phone);

        database = FirebaseDatabase.getInstance();
        host = database.getReference("Hosts");
        emailprovider=database.getReference("Mail-Provider");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=fullname.getText().toString();
                emailid=email.getText().toString();
                phone=phonenumber.getText().toString();

                if ((!(name.isEmpty())) && (!(name.equals("")))) {
                    CommonData.visitordetails.setName(name);
                }

                if ((!(emailid.isEmpty())) && (!(emailid.equals("")))) {
                    CommonData.visitordetails.setEmail(emailid);
                }

                if ((!(phone.isEmpty())) && (!(phone.equals("")))) {
                    CommonData.visitordetails.setPhone(phone);
                }

                Intent intent = new Intent(VisitorIn.this, SelectHost.class);
                startActivity(intent);
            }
        });

        mdialog = new ProgressDialog(VisitorIn.this);
        mdialog.setMessage("Please Wait...");

        visitinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mdialog.show();
                mdialog.setCanceledOnTouchOutside(false);
                if ((!(fullname.getText().toString().isEmpty())) && (!(fullname.getText().toString().equals("")))) {

                        if ((!(email.getText().toString().isEmpty())) && (!(email.getText().toString().equals("")))) {

                            if ((!(phonenumber.getText().toString().isEmpty())) && (!(phonenumber.getText().toString().equals("")))) {
                                if (phonenumber.getText().toString().length() == 10) {

                                    if((!(hostname.getText().toString().isEmpty())) && (!(hostname.getText().toString().equals("")))) {

                                        name=fullname.getText().toString();
                                        emailid=email.getText().toString();
                                        phone=phonenumber.getText().toString();
                                        hostname2=hostname.getText().toString();
                                        final String md5email= md5(CommonData.selectedhost.getEmail());

                                        final HashMap<String,Integer> map=new HashMap<>();

                                        host.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                                {
                                                    for(DataSnapshot snapshot1:snapshot.child("Visitors").getChildren())
                                                    {
                                                        String usedtoken=snapshot1.child("token").getValue().toString();
                                                        map.put(usedtoken,1);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        String newtoken=String.valueOf((long)(Math.floor(Math.random()*90000) + 10000));
                                        while (map.containsKey(newtoken))
                                        {
                                            newtoken=String.valueOf((long)(Math.floor(Math.random()*90000) + 10000));
                                        }


                                        final String finalNewtoken = newtoken;
                                        final String finalNewtoken1 = newtoken;
                                        host.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.child(md5email).exists()) {
                                                    mdialog.dismiss();
                                                    final long currentTime=System.currentTimeMillis();
                                                    String visitingkey=String.valueOf(currentTime);
                                                    VisitorModelOnly newentry=new VisitorModelOnly(name,emailid,phone, finalNewtoken);
                                                    host.child(md5email).child("Visitors").child(visitingkey).setValue(newentry);
                                                    Toast.makeText(VisitorIn.this, "Your entry has been recorded", Toast.LENGTH_LONG).show();

                                                    final String[] fromEmail = new String[1];
                                                    final String[] fromPassword = new String[1];

                                                    final List toEmailList = Arrays.asList(CommonData.selectedhost.getEmail()
                                                            .split("\\s*,\\s*"));

                                                    emailprovider.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists())
                                                            {
                                                                fromEmail[0] =dataSnapshot.child("FromMail").getValue().toString();
                                                                fromPassword[0] =dataSnapshot.child("Frompass").getValue().toString();
                                                                String emailSubject="Regarding New Visitor";
                                                                String[] arrnames=hostname2.split(" ");
                                                                String emailBody="Dear "+arrnames[0]+",<br /><br />"+"    You have a new Visitor <br />Name : "+name+"<br />Email-Id : "+emailid+"<br />Contact Number : "+phone+"<br /><br />Thanks!<br />";
                                                                new SendMailTask(VisitorIn.this).execute(fromEmail[0],
                                                                        fromPassword[0], toEmailList, emailSubject, emailBody);

                                                                emailSubject="Regarding Check In";
                                                                String[] arrnames2=name.split(" ");
                                                                List toEmailList2 = Arrays.asList(emailid
                                                                        .split("\\s*,\\s*"));
                                                                String date=CommonData.getDate(currentTime);
                                                                String time=CommonData.getTime(currentTime);

                                                                emailBody="Dear "+arrnames2[0]+",<br /><br />"+"    You have checked in at the "+CommonData.selectedhost.getAddress()+" on "+date+" at "+time+" ,<br />Token Number : "+ finalNewtoken1 +", use this while Check-out.<br />Host Name : "+hostname2+"<br />Host Contact Number : "+CommonData.selectedhost.getPhone()+"<br /><br />Thanks!";
                                                                new SendMailTask(VisitorIn.this).execute(fromEmail[0],
                                                                        fromPassword[0], toEmailList2, emailSubject, emailBody);

                                                                Intent intent = new Intent(VisitorIn.this, MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                } else {
                                                    mdialog.dismiss();
                                                    Toast.makeText(VisitorIn.this, "Selected Host doesn't exists in the database", Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(VisitorIn.this, "Select your Host", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    mdialog.dismiss();
                                    Toast.makeText(VisitorIn.this, "Enter 10 digit Phone Number", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                mdialog.dismiss();
                                Toast.makeText(VisitorIn.this, "Enter your Phone Number", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            mdialog.dismiss();
                            Toast.makeText(VisitorIn.this, "Enter your Email Id", Toast.LENGTH_LONG).show();
                        }
                }
                else
                {
                    mdialog.dismiss();
                    Toast.makeText(VisitorIn.this, "Enter your Full Name", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisitorIn.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(VisitorIn.this, MainActivity.class);
        startActivity(intent);
        return true;
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
