package com.japho.campus.center;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import  com.japho.campus.center.Common.NodeNames;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
public class ProfileActivity extends AppCompatActivity {
    private String reciever_id,sender_user_id;
    private ImageView visit_profile;
    private TextView visit_name,visit_status;

    private FirebaseAuth mauth;
    DatabaseReference ref,chatrequestref,contactsRef,NotificationRef;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv15,tv16;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mauth=FirebaseAuth.getInstance();
        sender_user_id=mauth.getCurrentUser().getUid();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        reciever_id=getIntent().getExtras().get("userid").toString();

        visit_profile=findViewById(R.id.visit_profile_image);
        visit_name=findViewById(R.id.visit_user_name);
        visit_status=findViewById(R.id.visit_status);


        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
       tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
         tv6 = (TextView) findViewById(R.id.tv6);
         tv7 = (TextView) findViewById(R.id.tv7);
         tv8 = (TextView) findViewById(R.id.tv8);

        tv9 = (TextView) findViewById(R.id.tv9);

        tv15 = (TextView) findViewById(R.id.tv15);
        tv16 = (TextView) findViewById(R.id.tv16);



        ref= FirebaseDatabase.getInstance().getReference();


        ref.child("Users").child(reciever_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot  teacherSnapshot) {
                if( teacherSnapshot.exists() &&  teacherSnapshot.hasChild("name") &&  teacherSnapshot.hasChild("image"))
                {
                    String retrieveusername= teacherSnapshot.child("name").getValue().toString();
                    String retrieveuserstatus= teacherSnapshot.child("bio").getValue().toString();
                    String retrieveuserimage= teacherSnapshot.child("image").getValue().toString();


                    visit_name.setText(retrieveusername);
                    visit_status.setText(retrieveuserstatus);
                    Picasso.get().load(retrieveuserimage).into(visit_profile);


                    String gender = teacherSnapshot.child(NodeNames.GENDER).getValue(String.class);
                    String age = teacherSnapshot.child(NodeNames.AGE).getValue(String.class);
                    String dateofb = teacherSnapshot.child(NodeNames.DATEOB).getValue(String.class);


                    String tribe = teacherSnapshot.child(NodeNames.TRIBE).getValue(String.class);

                    String hobby = teacherSnapshot.child(NodeNames.UNIV).getValue(String.class);
                    String county = teacherSnapshot.child(NodeNames.COUNTY).getValue(String.class);
                    String consti = teacherSnapshot.child(NodeNames.CONSTITUENCY).getValue(String.class);
                    String ward = teacherSnapshot.child(NodeNames.WARD).getValue(String.class);

                    String bio = teacherSnapshot.child(NodeNames.BIO).getValue(String.class);
                    tv2.setText("Gender:\t"+gender);
                    tv3.setText("Age:\t"+age);

                    tv4.setText("Tribe:\t"+tribe);

                    tv5.setText("Hobby: \t"+hobby);
                    tv6.setText("From : \n "+ county +"\t County,");
                    tv7.setText(consti +"\t Constituency,");
                    tv8.setText(ward+"\t Ward");
                    tv9.setText(bio);
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss");

                    long milliSeconds=teacherSnapshot.child(NodeNames.EXPIRY).getValue(Long.class);
                    long result=milliSeconds-System.currentTimeMillis();
                    if(result <=0)
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(milliSeconds);
                        String date = formatter.format(calendar.getTime());

                        tv15.setText("Inactive");
                        tv16.setText("EXPIRED ON \t" + date);

                    }
                    if(result > 0)
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(milliSeconds);
                        String date = formatter.format(calendar.getTime());
                        tv15.setText("Active");

                        tv16.setText("ACTIVE TILL \t" + date);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    public void GoBack(View view)
    {
        Intent mainIntent = new Intent(ProfileActivity.this,Dashboard.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(mainIntent);
        finish();
    }
    public void Edit(View view)
    {
        Helper.setEdit(1);
        Intent mainIntent = new Intent(ProfileActivity.this,RegisterNameActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(mainIntent);
        finish();
    }
}
