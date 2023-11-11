package com.japho.campus.center.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.CrashHandler;
import com.japho.campus.center.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PersonalDetailsFragment extends Fragment {
    String id;
    String visitor;

    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv15,tv16;
    DatabaseReference ref;
    private ProgressDialog progressDialog;
    public PersonalDetailsFragment(String id,String visitor)
    {
      this.id=id;
      this.visitor=visitor;
    }




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getContext()));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_personal_details, container, false);
        progressDialog=new ProgressDialog(getContext());
        tv2 =  view.findViewById(R.id.tv2);
        tv3 = view. findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 =  view.findViewById(R.id.tv5);
        tv6 = view. findViewById(R.id.tv6);
        tv7 =  view.findViewById(R.id.tv7);
        tv8 =  view.findViewById(R.id.tv8);

        tv9 = view.findViewById(R.id.tv9);

        tv15 = view.findViewById(R.id.tv15);
        tv16 = view.findViewById(R.id.tv16);
        if (visitor.equals("friend"))
        {
           tv15.setVisibility(View.INVISIBLE);
          tv16.setVisibility(View.INVISIBLE);

        }
        ref= FirebaseDatabase.getInstance().getReference();
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        ref.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {
                if( teacherSnapshot.exists() &&  teacherSnapshot.hasChild("name") &&  teacherSnapshot.hasChild("image"))
                {



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
                    Date d1 = teacherSnapshot.child(NodeNames.EXPIRY).getValue(Date.class);
                    Date d2=new java.util.Date();
                    long difference_In_Time = d1.getTime() - d2.getTime();

                    Long result = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
                    if(result <=0)
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(d2.getTime());


                        String date = d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h";
                        tv15.setTextColor(Color.RED);
                        tv16.setTextColor(Color.RED);
                        tv15.setText("Account Inactive");
                        tv16.setText("EXPIRED ON \t" + date);

                    }
                    if(result > 0)
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(d2.getTime());
                        String date =d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h";
                        tv15.setTextColor(Color.GREEN);
                        tv16.setTextColor(Color.GREEN);
                        tv15.setText("Account  Active");

                        tv16.setText("ACTIVE TILL \t" + date);

                    }
                    progressDialog.cancel();
                }
                else
                {
                    progressDialog.cancel();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                progressDialog.cancel();
            }
        });
return view;
    }
}