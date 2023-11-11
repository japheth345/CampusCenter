package com.japho.campus.center;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.japho.campus.center.Common.NodeNames;

import java.util.ArrayList;
public class RegisterUniversityActivity extends AppCompatActivity {
    private TextView textview;
    private Button genderContinueButton;
    String [] uNames=null;
    ArrayList<String> runivs=new ArrayList<>();
    Dialog dialog;
  String uni;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference RootRef;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hobby);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        genderContinueButton = findViewById(R.id.ageContinueButton);
        textview=findViewById(R.id.textView);
        firebaseAuth=FirebaseAuth.getInstance();
        email=firebaseAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();
        uNames= Helper.getUniversities();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        for(int i=0;i<uNames.length;i++)
        {
            runivs.add(uNames[i]);
        }
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog=new Dialog(RegisterUniversityActivity.this);

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
                tv.setText("Select your University");
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter=new ArrayAdapter<>(RegisterUniversityActivity.this, android.R.layout.simple_list_item_1,uNames);

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
                       uni=adapter.getItem(position);

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
                   if (uni == null) {
                       Snackbar.make(RegisterUniversityActivity.this, v, "Select your University", 5000).show();
                   } else {
                       RootRef.child("Users").child(email).child(NodeNames.UNIV).setValue(uni)
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {


                                       Intent mainIntent = new Intent(RegisterUniversityActivity.this, RegisterResidenceActivity.class);
                                       mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       mainIntent.putExtra("email", email);
                                       startActivity(mainIntent);
                                       finish();
                                   }

                               });

                   }
               }
               catch(Exception ex)
               {
                   Snackbar.make(RegisterUniversityActivity.this, v, "Select your University", 5000).show();
               }
            }});

    }
}