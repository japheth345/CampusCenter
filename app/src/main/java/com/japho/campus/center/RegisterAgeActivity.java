package com.japho.campus.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.snackbar.Snackbar;
import com.japho.campus.center.Common.NodeNames;

public class RegisterAgeActivity extends AppCompatActivity {
    String email;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
    private DatePicker ageSelectionPicker;
    private Button ageContinueButton;
    // age limit attribute
    private int ageLimit = 16;
    private DatabaseReference RootRef;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_age);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        firebaseAuth=FirebaseAuth.getInstance();
       email=firebaseAuth.getCurrentUser().getUid();




        ageSelectionPicker = findViewById(R.id.ageSelectionPicker);


        ageContinueButton = findViewById(R.id.ageContinueButton);
        RootRef= FirebaseDatabase.getInstance().getReference();
        ageContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opeTribeEntryPage(v);
            }
        });


    }

    public void opeTribeEntryPage(View v) {
     String age = String.valueOf(getAge(ageSelectionPicker.getYear(), ageSelectionPicker.getMonth(), ageSelectionPicker.getDayOfMonth()));

        // if user is above 13 years old then only he/she will be allowed to register to the system.
        if (Integer.parseInt(age ) > ageLimit) {
            // code for converting date to string
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, ageSelectionPicker.getYear());
            cal.set(Calendar.MONTH, ageSelectionPicker.getMonth());
            cal.set(Calendar.DAY_OF_MONTH, ageSelectionPicker.getDayOfMonth());
            Date dateOfBirth = cal.getTime();
            String strDateOfBirth = dateFormatter.format(dateOfBirth);
           // Snackbar.make(RegisterAgeActivity.this,v,"Age=\t"+age +"\n Dob= \t"+ageSelectionPicker.getDayOfMonth() +"\n Mob=\t"+ageSelectionPicker.getMonth()+"\n Yob=\t"+ ageSelectionPicker.getYear() +"\n String =\t"+strDateOfBirth,5000).show();
            RootRef.child("Users").child(email).child(NodeNames.AGE).setValue(String.valueOf(age))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            RootRef.child("Users").child(email).child(NodeNames.DATEOB).setValue(strDateOfBirth)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            RootRef.child("Users").child(email).child(NodeNames.DOB).setValue(String.valueOf(ageSelectionPicker.getDayOfMonth()))
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            RootRef.child("Users").child(email).child(NodeNames.MOB).setValue( String.valueOf(ageSelectionPicker.getMonth() + 1))
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {


                                                                            RootRef.child("Users").child(email).child(NodeNames.YOB).setValue(String.valueOf(ageSelectionPicker.getYear()))
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {


                                                                                            Intent mainIntent = new Intent(RegisterAgeActivity.this, RegisterTribeActivity.class);
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


                                    });
                        }

                    });

        }
        else
        {
            Snackbar.make(RegisterAgeActivity.this,v,"Error \t Age of the user should be greater than " + ageLimit + " !!!",5000).show();
           // Toast.makeText(getApplicationContext(), "Age of the user should be greater than " + ageLimit + " !!!", Toast.LENGTH_SHORT).show();
        }

    }

    // method to get the current age of the user.
    private int getAge(int year, int month, int day) {
        Calendar dateOfBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dateOfBirth.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }
}
