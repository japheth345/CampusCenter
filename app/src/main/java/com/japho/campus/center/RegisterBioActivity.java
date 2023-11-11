package com.japho.campus.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japho.campus.center.Common.NodeNames;

public class RegisterBioActivity extends AppCompatActivity {
    private EditText bio;
    private Button genderContinueButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference RootRef;
    String abt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bio);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        bio = findViewById(R.id.bioNameCreateAcc);
        genderContinueButton = findViewById(R.id.ageContinueButton);
        firebaseAuth=FirebaseAuth.getInstance();
        RootRef= FirebaseDatabase.getInstance().getReference();
        genderContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
              abt = bio.getText().toString();


                if (abt.isEmpty())
                {
                    Snackbar.make(RegisterBioActivity.this,v,"Kindly enter your Bio \n",5000).show();

                    //Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_LONG).show();
                }

                else {
                    String currentUserID = firebaseAuth.getCurrentUser().getUid();

                    RootRef.child("Users").child(currentUserID).child(NodeNames.BIO).setValue(abt)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {



                                        Intent mainIntent = new Intent(RegisterBioActivity.this, RegisterProfileActivity.class);
                                         mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(mainIntent);
                                        finish();
                                    } else {

                                        String errormessage = task.getException().toString();

                                        Snackbar.make(RegisterBioActivity.this, v, "Error :" + errormessage, 10000).show();
                                        // Toast.makeText(RegisterActivity.this,"Error :"+errormessage,Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                }

    }});
}}
