package com.japho.campus.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.japho.campus.center.Common.NodeNames;

import java.util.ArrayList;

public class RegisterTribeActivity extends AppCompatActivity {
    private TextView textview;
    private Button genderContinueButton;
    String [] uNames=null;
    ArrayList<String> runivs=new ArrayList<>();
    Dialog dialog;
   String tribe=null;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference RootRef;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tribe);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        genderContinueButton = findViewById(R.id.ageContinueButton);
        textview=findViewById(R.id.textView);
        firebaseAuth=FirebaseAuth.getInstance();
        RootRef= FirebaseDatabase.getInstance().getReference();
        uNames= Helper.getTribes();
        java.util.Arrays.sort(uNames);
        firebaseAuth=FirebaseAuth.getInstance();
        email=firebaseAuth.getCurrentUser().getUid();
        for(int i=0;i<uNames.length;i++)
        {
            runivs.add(uNames[i]);
        }
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog=new Dialog(RegisterTribeActivity.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout(650,800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter=new ArrayAdapter<>(RegisterTribeActivity.this, android.R.layout.simple_list_item_1,uNames);

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
                       tribe=adapter.getItem(position);

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });

        genderContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (tribe == null) {
                        Snackbar.make(RegisterTribeActivity.this, v, "Kindly select your Tribe", 5000).show();
                    } else {
                        RootRef.child("Users").child(email).child(NodeNames.TRIBE).setValue(tribe)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        Intent mainIntent = new Intent(RegisterTribeActivity.this, RegisterUniversityActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.putExtra("email", email);
                                        startActivity(mainIntent);
                                        finish();
                                    }

                                });

                    }
                }
                catch (Exception ex)
                {
                    Snackbar.make(RegisterTribeActivity.this, v, "Kindly select your Tribe", 5000).show();
                }

            }});

    }
}