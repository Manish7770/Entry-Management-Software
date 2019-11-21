package com.example.visitormanagement;

import android.app.ProgressDialog;
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
import com.example.visitormanagement.Model.VisitedOutModel;
import com.example.visitormanagement.Model.VisitorModelOnly;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import info.hoang8f.widget.FButton;

public class VisitorOut extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference host,emailprovider;

    public EditText tokenId;
    public TextView hostname;
    public ImageView search;
    public FButton visitoutbutton;

    private String tokennumber,hostname2;

    ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitorout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.addressToolbar);
        toolbar.setTitle("Visitor Out");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tokenId=findViewById(R.id.token);
        hostname=findViewById(R.id.hostname);
        search=findViewById(R.id.search);
        visitoutbutton=findViewById(R.id.visitoroutbutton);

        tokennumber=CommonData.visitordetails.getToken();
        hostname2=CommonData.selectedhost.getName();
        tokenId.setText(tokennumber);
        hostname.setText(hostname2);

        database = FirebaseDatabase.getInstance();
        host = database.getReference("Hosts");
        emailprovider=database.getReference("Mail-Provider");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokennumber=tokenId.getText().toString();

                if ((!(tokennumber.isEmpty())) && (!(tokennumber.equals("")))) {
                    CommonData.visitordetails.setToken(tokennumber);
                }

                Intent intent = new Intent(VisitorOut.this, SelectHost.class);
                startActivity(intent);
            }
        });

        mdialog = new ProgressDialog(VisitorOut.this);
        mdialog.setMessage("Please Wait...");

        visitoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mdialog.show();
                mdialog.setCanceledOnTouchOutside(false);
                if ((!(tokenId.getText().toString().isEmpty())) && (!(tokenId.getText().toString().equals("")))) {
                    if ((!(hostname.getText().toString().isEmpty())) && (!(hostname.getText().toString().equals("")))) {
                        tokennumber=tokenId.getText().toString();
                        hostname2=hostname.getText().toString();
                        final String md5email= md5(CommonData.selectedhost.getEmail());
                        final int[] found = {0};
                        final long[] checkintime = new long[1];
                        host.child(md5email).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot1:dataSnapshot.child("Visitors").getChildren())
                                    {
                                        String usedtoken=snapshot1.child("token").getValue().toString();
                                        if(usedtoken.equals(tokennumber))
                                        {
                                            CommonData.visitordetails.setName(snapshot1.child("name").getValue().toString());
                                            CommonData.visitordetails.setPhone(snapshot1.child("phone").getValue().toString());
                                            CommonData.visitordetails.setEmail(snapshot1.child("email").getValue().toString());
                                            checkintime[0] =Long.parseLong(snapshot1.getKey());
                                            found[0]=1;
                                        }
                                    }

                                if(found[0]==1)
                                {
                                    mdialog.dismiss();
                                    final long currentTime=System.currentTimeMillis();
                                    String checkoutdate=CommonData.getDate(currentTime);
                                    final String checkouttime=CommonData.getTime(currentTime);

                                    String checkindate=CommonData.getDate(checkintime[0]);
                                    final String checkintime2=CommonData.getTime(checkintime[0]);
                                    String intimestamp=String.valueOf(checkintime[0]);

                                    VisitedOutModel visited=new VisitedOutModel(CommonData.visitordetails.getName(),CommonData.visitordetails.getEmail(),CommonData.visitordetails.getPhone());
                                    database.getReference("Visitors-Out").child(md5email).child("Visitors").child(intimestamp).setValue(visited);
                                    host.child(md5email).child("Visitors").child(intimestamp).removeValue();

                                    final String[] fromEmail = new String[1];
                                    final String[] fromPassword = new String[1];

                                    final List toEmailList = Arrays.asList(CommonData.visitordetails.getEmail()
                                            .split("\\s*,\\s*"));

                                    emailprovider.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                fromEmail[0] =dataSnapshot.child("FromMail").getValue().toString();
                                                fromPassword[0] =dataSnapshot.child("Frompass").getValue().toString();
                                                String emailSubject="Regarding your Visit";
                                                String[] arrnames=CommonData.visitordetails.getName().split(" ");
                                                String emailBody="Dear "+arrnames[0]+",<br /><br />"+"    You have just checked out<br />Name : "+CommonData.visitordetails.getName()+"<br />Phone Number : "+CommonData.visitordetails.getPhone()+"<br />Check-in time : "+checkintime2+"<br />Check-out time : "+checkouttime+"<br />Host Name : "+CommonData.selectedhost.getName()+"<br />Visited Address : "+CommonData.selectedhost.getAddress()+"<br /><br />Thanks!";
                                                new SendMailTask(VisitorOut.this).execute(fromEmail[0],
                                                        fromPassword[0], toEmailList, emailSubject, emailBody);
                                                Intent intent = new Intent(VisitorOut.this, MainActivity.class);
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
                                    Toast.makeText(VisitorOut.this, "Token number doesn't exists", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        mdialog.dismiss();
                        Toast.makeText(VisitorOut.this, "Select your Host", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mdialog.dismiss();
                    Toast.makeText(VisitorOut.this, "Enter the Token Number", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(VisitorOut.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(VisitorOut.this, MainActivity.class);
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
