package com.japho.campus.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.snackbar.Snackbar;
import com.japho.campus.center.Common.NodeNames;

public class RegisterGenderActivity extends AppCompatActivity {
    String email;

    String gender=null;

    private Button genderContinueButton;
    private RadioGroup mRadioGroup;
    private RadioButton r1,r2,r3;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference RootRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gender);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        firebaseAuth=FirebaseAuth.getInstance();
        email=firebaseAuth.getCurrentUser().getUid();

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        r1=findViewById(R.id.rb1);
        r2=findViewById(R.id.rb2);
        r3=findViewById(R.id.rb3);
        genderContinueButton = findViewById(R.id.genderContinueButton);

        //By default male has to be selected so below code is added


        firebaseAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();


        genderContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // gender = mRadioGroup.getCheckedRadioButtonId();
                if(r1.isChecked())
                {
                    gender="MALE";

                }
                if(r2.isChecked())
                {
                    gender="FEMALE";

                }
                if(r3.isChecked())
                {
                    gender="OTHERS";
                }
                if (gender == null)
                {
                    Snackbar.make(RegisterGenderActivity.this, v, "Kindly select your gender", 5000).show();
                }
                else
                {
                    String email=firebaseAuth.getCurrentUser().getUid();

                    RootRef.child("Users").child(email).child(NodeNames.GENDER).setValue(gender)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    Intent mainIntent = new Intent(RegisterGenderActivity.this, RegisterAgeActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.putExtra("email", email);
                                    startActivity(mainIntent);
                                    finish();
                                }

                            });

                }
            }
        });
    }
}


