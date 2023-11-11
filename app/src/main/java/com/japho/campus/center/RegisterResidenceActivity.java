package com.japho.campus.center;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.EditText;
import android.widget.ListView;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.AdapterView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japho.campus.center.Common.NodeNames;

import java.util.ArrayList;

public class RegisterResidenceActivity extends AppCompatActivity {
    private TextView textview,textview2,textview3,getStartedTextView2,getStartedTextView3;
    private Button genderContinueButton;
    String [] uNames=null;
    String [] uNames2=null;
    String [] uNames3=null;
    ArrayList<String> county=new ArrayList<>();
    ArrayList<String> con=new ArrayList<>();
    ArrayList<String> ward=new ArrayList<>();
    Dialog dialog;
      String c=null;
      String co=null;
      String wa=null;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference RootRef;
    String email;
    private ProgressDialog progressDialog;
    ArrayList<Place> places=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_residence);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        genderContinueButton = findViewById(R.id.ageContinueButton);
        textview=findViewById(R.id.textView);

        textview2=findViewById(R.id.textView2);
        getStartedTextView2=findViewById(R.id.getStartedTextView2);

        textview3=findViewById(R.id.textView3);
        getStartedTextView3=findViewById(R.id.getStartedTextView3);

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        RootRef= FirebaseDatabase.getInstance().getReference().child("Places");

      /*  RootRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.setTitle("Loading.... ");
                progressDialog.setMessage("Loading please wait");
                // progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    places.clear();


                    for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {


                        Place upload = teacherSnapshot.getValue(Place.class);

                        if (upload == null) {

                        } else {


                            places.add(upload);


                        }


                    }
                    Helper.setPlaces(places);
                    int SPLASH_DISPLAY_LENGTH;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(RegisterResidenceActivity.this, "Places=\t" + places.size(), Toast.LENGTH_LONG).show();

                        }
                    }, 10000);




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegisterResidenceActivity.this, "Places not loaded because \n" + databaseError.getMessage(), Toast.LENGTH_LONG).show();



            }});

       */
        RootRef= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        email=firebaseAuth.getCurrentUser().getUid();
        places=Helper.getPlaces();
        //Toast.makeText(RegisterResidenceActivity.this, "Places=\t" + places.size(), Toast.LENGTH_LONG).show();
        if(places.size() <=0)
        {

        }
        else
        {
            county.clear();
            for(int i=0;i<places.size();i++)
            {
                if(county.contains(places.get(i).getCounty()))
                {

                }
                else
                {
                    county.add(places.get(i).getCounty());
                }
            }
            //Toast.makeText(RegisterResidenceActivity.this, "COUNTY=\t" + county.size(), Toast.LENGTH_LONG).show();
            uNames=new String[county.size()];
            for(int i=0;i<county.size();i++)
            {
                uNames[i]=county.get(i);
            }
            java.util.Arrays.sort(uNames);

            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getStartedTextView2.setVisibility(View.INVISIBLE);
                    textview2.setVisibility(View.INVISIBLE);
                    getStartedTextView3.setVisibility(View.INVISIBLE);
                    textview3.setVisibility(View.INVISIBLE);
                    // Initialize dialog
                    dialog=new Dialog(RegisterResidenceActivity.this);

                    // set custom dialog
                    dialog.setContentView(R.layout.dialog_searchable_spinner);

                    // set custom height and width
                    dialog.getWindow().setLayout(650,800);

                    // set transparent background
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    // show dialog
                    dialog.show();

                    // Initialize and assign variable
                    TextView tv=dialog.findViewById(R.id.tv);
                    tv.setText("Select your County");
                    EditText editText=dialog.findViewById(R.id.edit_text);

                    ListView listView=dialog.findViewById(R.id.list_view);

                    // Initialize array adapter
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(RegisterResidenceActivity.this, android.R.layout.simple_list_item_1,uNames);

                    // set adapter
                    listView.setAdapter(adapter);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            textview.setText(adapter.getItem(position));
                           int n=county.indexOf(adapter.getItem(position));
                            c=adapter.getItem(position);
         // c=position + 1;
                            String rcounty=county.get(n);

                           /* for(int i=0;i<places.size();i++)
                            {
                           if(places.get(i).getCounty()==rcounty)
                           {
                               c=i;
                           }

                            }*/
                                con.clear();
                            for(int i=0;i<places.size();i++)
                            {

                                if (places.get(i).getCounty() == rcounty) {
                                       if(con.contains(places.get(i).getCons()))
                                       {

                                       }
                                       else
                                       {
                                           con.add(places.get(i).getCons());
                                       }
                                }
                            }
                           // Toast.makeText(RegisterResidenceActivity.this, "CONST=\t" + con.size(), Toast.LENGTH_LONG).show();
                            uNames2=new String[con.size()];
                            for(int i=0;i<con.size();i++)
                            {
                                uNames2[i]=con.get(i);
                            }
                            java.util.Arrays.sort(uNames2);
                            //Toast.makeText(RegisterResidenceActivity.this, "Places=\t" + places.size(), Toast.LENGTH_LONG).show();
                            getStartedTextView2.setVisibility(View.VISIBLE);
                           textview2.setVisibility(View.VISIBLE);
                           textview2.setText(uNames2[0]);
                            dialog.dismiss();
                        }
                    });
                }
            });


            textview2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog=new Dialog(RegisterResidenceActivity.this);

                    // set custom dialog
                    dialog.setContentView(R.layout.dialog_searchable_spinner);

                    // set custom height and width
                    dialog.getWindow().setLayout(650,800);

                    // set transparent background
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    // show dialog
                    dialog.show();

                    // Initialize and assign variable
                    TextView tv=dialog.findViewById(R.id.tv);
                    tv.setText("Select your Constituency");
                    EditText editText=dialog.findViewById(R.id.edit_text);

                    ListView listView=dialog.findViewById(R.id.list_view);

                    // Initialize array adapter
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(RegisterResidenceActivity.this, android.R.layout.simple_list_item_1,uNames2);

                    // set adapter
                    listView.setAdapter(adapter);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            textview2.setText(adapter.getItem(position));
                            co=adapter.getItem(position);
                            int n=con.indexOf(adapter.getItem(position));
                            String rcons=con.get(n);

                           ward.clear();

                            for(int i=0;i<places.size();i++)
                            {

                                if (places.get(i).getCons() == rcons)
                                {
                          if(ward.contains(places.get(i).getWard()))
                          {

                          }
                          else
                          {
                                    ward.add(places.get(i).getWard());
                          }
                            }}
                            //Toast.makeText(RegisterResidenceActivity.this, "WARDS=\t" + ward.size(), Toast.LENGTH_LONG).show();
                            uNames3=new String[ward.size()];
                            for(int i=0;i<ward.size();i++)
                            {
                                uNames3[i]=ward.get(i);
                            }
                            java.util.Arrays.sort(uNames3);
                            getStartedTextView3.setVisibility(View.VISIBLE);
                            textview3.setVisibility(View.VISIBLE);
                         textview3.setText(uNames3[0]);
                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }
            });

            textview3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog=new Dialog(RegisterResidenceActivity.this);

                    // set custom dialog
                    dialog.setContentView(R.layout.dialog_searchable_spinner);

                    // set custom height and width
                    dialog.getWindow().setLayout(650,800);

                    // set transparent background
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    // show dialog
                    dialog.show();

                    // Initialize and assign variable
                    TextView tv=dialog.findViewById(R.id.tv);
                    tv.setText("Select your Ward");
                    EditText editText=dialog.findViewById(R.id.edit_text);

                    ListView listView=dialog.findViewById(R.id.list_view);

                    // Initialize array adapter
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(RegisterResidenceActivity.this, android.R.layout.simple_list_item_1,uNames3);

                    // set adapter
                    listView.setAdapter(adapter);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            textview3.setText(adapter.getItem(position));
                            wa=adapter.getItem(position);



                            dialog.dismiss();
                        }
                    });
                }
            });


            genderContinueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                            if (c == null) {
                                Snackbar.make(RegisterResidenceActivity.this, v, "Kindly select your your County", 5000).show();
                            } else if (co == null) {
                                Snackbar.make(RegisterResidenceActivity.this, v, "Kindly select your Constituency", 5000).show();
                            } else if (wa == null) {
                                Snackbar.make(RegisterResidenceActivity.this, v, "Kindly select your Ward", 5000).show();
                            } else {
                                RootRef.child("Users").child(email).child(NodeNames.COUNTY).setValue(c)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                                RootRef.child("Users").child(email).child(NodeNames.CONSTITUENCY).setValue(co)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                RootRef.child("Users").child(email).child(NodeNames.WARD).setValue(wa)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                                Intent mainIntent = new Intent(RegisterResidenceActivity.this, RegisterBioActivity.class);
                                                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                mainIntent.putExtra("email", email);
                                                                                startActivity(mainIntent);
                                                                                finish();
                                                                            }

                                                                        });
                                                            }

                                                        });

                                            }

                                        });

                            }
                        }
                        catch(Exception ex)
                        {
                            Snackbar.make(RegisterResidenceActivity.this, v, "Kindly select your Level Of County,Constituency and ward", 5000).show();
                        }
                }});



        }


    }
}