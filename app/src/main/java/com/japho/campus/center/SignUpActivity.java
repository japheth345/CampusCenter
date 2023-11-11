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
import com.japho.campus.center.Model.ConnectivityUtils;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static int locationRequestCode = 0;
    private Context mContext;
    public String email, password,password2;
    private  com.google.android.material.textfield.TextInputEditText mEmail, mPassword,mPassword2;
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
        setContentView(R.layout.activity_sign_up);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        requestLocationPermission();
        mEmail = findViewById(R.id.input_email);

        btnRegister = findViewById(R.id.btn_register);
        mPassword = findViewById(R.id.input_password);
        mPassword2 = findViewById(R.id.input_password2);
        mContext = SignUpActivity.this;


        firebaseAuth=FirebaseAuth.getInstance();
        RootRef= FirebaseDatabase.getInstance().getReference();
        progressDialog=new ProgressDialog(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean isConnected = ConnectivityUtils.isInternetConnected(getApplicationContext());
                if (isConnected) {
                email = mEmail.getText().toString();

                password = mPassword.getText().toString();
                password2 = mPassword2.getText().toString();

                if (email.isEmpty())
                {
                    Snackbar.make(mContext,v,"Kindly enter your email \n",5000).show();

                    //Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_LONG).show();
                }

                else if (password.isEmpty())
                {
                    Snackbar.make(mContext,v,"Kindly enter your password \n",5000).show();

                    //Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_LONG).show();
                }
                else if (password2.isEmpty())
                {
                    Snackbar.make(mContext,v,"Kindly Re-enter your password \n",5000).show();

                    //Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_LONG).show();
                }
                else if (!email.matches(emailPattern))
                {

                    Snackbar.make(mContext,v,"Invalid email address, enter valid email id and click on Continue",5000).show();
                   // Toast.makeText(getApplicationContext(), "Invalid email address, enter valid email id and click on Continue", Toast.LENGTH_LONG).show();
                }
               else if (password.length() < 6 || password.length() > 30)
               {

                    Snackbar.make(mContext,v,"Password should be of 6-30 characters",5000).show();
                }


                else if (!password.matches(upperCaseChars))
                {
                    Snackbar.make(mContext,v,"Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.",5000).show();

                }


                else if (!password.matches(lowerCaseChars))
                {

                    Snackbar.make(mContext,v,"Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.",5000).show();

                }


                else if (!password.matches(numbers))
                {
                    Snackbar.make(mContext,v,"Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.",5000).show();

                }


               else if (!password.matches(specialChars))
               {

                    Snackbar.make(mContext,v,"Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.",5000).show();

                }

               else {
                    if (password.equals(password2)) {


                        progressDialog.setTitle("Creating New Account");
                        progressDialog.setMessage("please wait, While we are creating a new account for you...");
                        // progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.show();
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String currentUserID = firebaseAuth.getCurrentUser().getUid();

                                    RootRef.child("Users").child(currentUserID).child(NodeNames.EMAIL).setValue(email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();

                                                        // Toast.makeText(SignUpActivity.this, "Account created Successfully...", Toast.LENGTH_LONG).show();
                                                        Intent mainIntent = new Intent(SignUpActivity.this, RegisterNameActivity.class);
                                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        mainIntent.putExtra("email", email);
                                                        startActivity(mainIntent);
                                                        finish();
                                                    } else {
                                                        String errormessage = task.getException().toString();
                                                        progressDialog.dismiss();
                                                        Toast.makeText(mContext, "Error :" + errormessage, Toast.LENGTH_LONG).show();
                                                        // Toast.makeText(RegisterActivity.this,"Error :"+errormessage,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    boolean isConnected = ConnectivityUtils.isInternetConnected(getApplicationContext());
                                    if (isConnected) {
                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {


                                            String errormessage = "Error\t The Email Already Exists \n Kindly enter another email";
                                            progressDialog.dismiss();
                                            //Snackbar.make(mContext, v, "Error :" + errormessage, 10000).show();
                                            Toast.makeText(SignUpActivity.this, errormessage, Toast.LENGTH_SHORT).show();
                                        } else {
                                            String errormessage = task.getException().toString();
                                            progressDialog.dismiss();
                                            // Snackbar.make(mContext, v, "Error :" + errormessage, 10000).show();
                                            Toast.makeText(SignUpActivity.this, "Error :" + errormessage, Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Snackbar.make(SignUpActivity.this, v, "You are not connected to internet", 5000).show();
                                    }
                                }
                            } });

                    }
                    else {
                        Snackbar.make(mContext, v, "Password do not match", 5000).show();
                    }
                }
                }
                else
                {
                    Snackbar.make(SignUpActivity.this, v, "You are not connected to internet",  5000).show();
                }

                        }
                    });



    }
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this,READ_EXTERNAL_STORAGE) ) {

            return;
        }
        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{READ_EXTERNAL_STORAGE}, locationRequestCode);

    }
    public void onLoginClicked(View view)
    {
        Intent mainIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(mainIntent);
        finish();
    }
}