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

import static com.example.visitormanagement.VisitorIn.md5;

public class VisitorOut extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference visitorIn,emailprovider;

    public EditText tokenId;
    public FButton visitoutbutton;

    private String tokennumber;

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
        visitoutbutton=findViewById(R.id.visitoroutbutton);

        tokennumber=CommonData.visitordetails.getToken();
        tokenId.setText(tokennumber);

        database = FirebaseDatabase.getInstance();
        visitorIn = database.getReference("Visitors-In");
        emailprovider=database.getReference("Mail-Provider");

        mdialog = new ProgressDialog(VisitorOut.this);
        mdialog.setMessage("Please Wait...");

        visitoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mdialog.show();
                mdialog.setCanceledOnTouchOutside(false);
                if ((!(tokenId.getText().toString().isEmpty())) && (!(tokenId.getText().toString().equals("")))) {
                        tokennumber=tokenId.getText().toString();

                        final int[] found = {0};
                        final long[] checkintime = new long[1];
                        // check for the token whether it exists in the database or not
                        visitorIn.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                                    for (DataSnapshot snapshot1 : snapshot.child("Visitors").getChildren()) {
                                        String usedtoken = snapshot1.child("token").getValue().toString();
                                        if (usedtoken.equals(tokennumber)) {
                                            CommonData.visitordetails.setName(snapshot1.child("name").getValue().toString());
                                            CommonData.visitordetails.setPhone(snapshot1.child("phone").getValue().toString());
                                            CommonData.visitordetails.setEmail(snapshot1.child("email").getValue().toString());
                                            checkintime[0] = Long.parseLong(snapshot1.getKey());
                                            CommonData.selectedhost.setName(snapshot.child("name").getValue().toString());
                                            CommonData.selectedhost.setEmail(snapshot.child("email").getValue().toString());
                                            CommonData.selectedhost.setAddress(snapshot.child("address").getValue().toString());
                                            CommonData.selectedhost.setPhone(snapshot.child("phone").getValue().toString());

                                            found[0] = 1;
                                            break;
                                        }
                                    }
                                }
                                final String md5email;
                                if(found[0]==1)
                                {
                                    md5email = md5(CommonData.selectedhost.getEmail());
                                    mdialog.dismiss();
                                    final long currentTime=System.currentTimeMillis();
                                    final String checkoutdate=CommonData.getDate(currentTime);
                                    final String checkouttime=CommonData.getTime(currentTime);

                                    final String checkindate=CommonData.getDate(checkintime[0]);
                                    final String checkintime2=CommonData.getTime(checkintime[0]);
                                    String intimestamp=String.valueOf(checkintime[0]);

                                    String checkout=checkoutdate+" "+checkouttime;

                                    VisitedOutModel visited=new VisitedOutModel(CommonData.visitordetails.getName(),CommonData.visitordetails.getEmail(),CommonData.visitordetails.getPhone(),checkout);
                                    database.getReference("Visitors-Out").child(md5email).child("Visitors").child(intimestamp).setValue(visited);
                                    visitorIn.child(md5email).child("Visitors").child(intimestamp).removeValue();

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
                                                String emailBody="Dear "+arrnames[0]+",<br /><br />"+"    You have just checked out<br />Name : "+CommonData.visitordetails.getName()+"<br />Phone Number : "+CommonData.visitordetails.getPhone()+"<br />Check-in : "+checkindate+" "+checkintime2+"<br />Check-out : "+checkoutdate+" "+checkouttime+"<br />Host Name : "+CommonData.selectedhost.getName()+"<br />Visited Address : "+CommonData.selectedhost.getAddress()+"<br /><br />Thanks!";
                                                //send email to the Visitor with complete details form
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
