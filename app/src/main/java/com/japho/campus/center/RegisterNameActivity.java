package com.japho.campus.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.snackbar.Snackbar;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import com.japho.campus.center.Common.NodeNames;
public class RegisterNameActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static int locationRequestCode = 0;
    private Context mContext;
    public String username, username2;
    private EditText mUsername, mUsername2;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private String append = "";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference RootRef;
    private ProgressDialog progressDialog;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
    String upperCaseChars = "(.*[A-Z].*)";
    String numbers = "(.*[0-9].*)";
    String lowerCaseChars = "(.*[a-z].*)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));


        mUsername = findViewById(R.id.input_username);
        mUsername2 = findViewById(R.id.input_username2);
        btnRegister = findViewById(R.id.btn_register);

        mContext = RegisterNameActivity.this;


        firebaseAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = mUsername.getText().toString();
                username2 = mUsername2.getText().toString();
                if (username.isEmpty()) {
                    Snackbar.make(mContext, v, "Kindly fill all fields\n", 5000).show();
                    //Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_LONG).show();
                } else if (username2.isEmpty()) {
                    Snackbar.make(mContext, v, "Kindly fill all fields\n", 5000).show();
                    //Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_LONG).show();
                } else {


                    String currentUserID = firebaseAuth.getCurrentUser().getUid();
                    //String currentUserID=email;
                    //RootRef.child("Users").child(currentUserID).setValue("");
                    RootRef.child("Users").child(currentUserID).child(NodeNames.USERID).setValue(currentUserID)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    RootRef.child("Users").child(currentUserID).child(NodeNames.NAME).setValue(username)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    RootRef.child("Users").child(currentUserID).child(NodeNames.NATID).setValue(username2)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    String email = firebaseAuth.getCurrentUser().getUid();

                                                                    Intent mainIntent = new Intent(RegisterNameActivity.this, RegisterGenderActivity.class);
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
        });
    }
}

