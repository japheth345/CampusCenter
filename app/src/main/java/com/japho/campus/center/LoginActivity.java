package com.japho.campus.center;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.japho.campus.center.Common.NodeNames;


import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import android.util.Patterns;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.japho.campus.center.Model.ConnectivityUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private Context mContext;
    private EditText editTextEmail, editTextPassword;
    Button btnLogin;
    private ProgressDialog progressDialog;
    private static int locationRequestCode = 0;
    String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
    String upperCaseChars = "(.*[A-Z].*)";
    String lowerCaseChars = "(.*[a-z].*)";
    String numbers = "(.*[0-9].*)";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        FirebaseAuth authRef = FirebaseAuth.getInstance();
        if( authRef.getCurrentUser() == null)
        {
            Intent intent = new Intent(this, SignUpActivity.class);

            startActivity(intent);

        }

        mAuth=FirebaseAuth.getInstance();
       // String currentUserID=mAuth.getCurrentUser().getUid();
       // RootRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        btnLogin = findViewById(R.id.btn_login);
        editTextEmail = findViewById(R.id.input_email);
        editTextPassword = findViewById(R.id.input_password);
        mContext = LoginActivity.this;
        progressDialog=new ProgressDialog(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin(view);
            }

            ;
        });

        TextView linkSignUp = findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: navigating to register screen");
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        TextView createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                builder1.setCancelable(true);
                builder1.setTitle("Confirm");
                builder1.setMessage("Could you like to create a new account??");

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
    }

    private void userLogin(View v) {
        boolean isConnected = ConnectivityUtils.isInternetConnected(getApplicationContext());
        if (isConnected) {

            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (email.isEmpty())
            {
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
                return;
            }

            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Please enter a valid email");
                editTextEmail.requestFocus();
                return;
            }

           else if (password.isEmpty()) {
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
                return;
            }

           else if (password.length() < 6 || password.length() > 30) {
                editTextPassword.setError("Password should be of 6-30 characters");
                editTextPassword.requestFocus();
                return;
            }


            else if (!password.matches(upperCaseChars)) {
                //editTextPassword.setError("Password should contain atleast one upper case alphabet");
                editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
                editTextPassword.requestFocus();
                return;
            }


            else if (!password.matches(lowerCaseChars)) {
                editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
                editTextPassword.requestFocus();
                return;
            }


           else  if (!password.matches(numbers)) {
                editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
                editTextPassword.requestFocus();
                return;
            }


           else if (!password.matches(specialChars))
           {
                editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
                editTextPassword.requestFocus();
                return;
            }
else {
                //progressBar.setVisibility(View.VISIBLE);
  progressDialog.setTitle("Loading..");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);
        progressDialog.show();


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //  progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {


                     /*   Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    finish();



                      */

                            VerifyUserExistence();

                        }
                        else
                        {
                            progressDialog.cancel();
                            String message = task.getException().getMessage();
                            if (message.contains("The password is invalid"))
                            {
                                Snackbar.make(LoginActivity.this, v, "ERROR wrong email or password try again", 5000).show();
                            } else if (message.contains("There is no user record")) {
                                Snackbar.make(LoginActivity.this, v, "ERROR wrong email or password try again", 5000).show();
                            } else {
                                Snackbar.make(LoginActivity.this, v, "You are not connected to internet", 5000).show();
                            }
                            // Toast.makeText(mContext, "Error \n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            // Snackbar.make(LoginActivity.this, v, "You are not connected to internet",  5000).show();


                        }
                    }
                });
            }
        }
        else
        {
            Snackbar.make(LoginActivity.this, v, "You are not connected to internet",  5000).show();
        }
    }
    @Override
    public void onBackPressed() {

    }
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, ACCESS_COARSE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, ACCESS_FINE_LOCATION)) {

            return;
        }
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION,READ_EXTERNAL_STORAGE}, locationRequestCode);

    }
    private void VerifyUserExistence() {
       /* progressDialog.setTitle("Loading..");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        */
        DatabaseReference RootRef2 = FirebaseDatabase.getInstance().getReference();
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RootRef2.child("Users").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(NodeNames.NAME).exists() && dataSnapshot.child(NodeNames.GENDER).exists() && dataSnapshot.child(NodeNames.AGE).exists()  && dataSnapshot.child(NodeNames.TRIBE).exists()  &&  dataSnapshot.child(NodeNames.UNIV).exists() && dataSnapshot.child(NodeNames.COUNTY).exists() && dataSnapshot.child(NodeNames.CONSTITUENCY).exists() && dataSnapshot.child(NodeNames.WARD).exists() && dataSnapshot.child(NodeNames.BIO).exists() && dataSnapshot.child(NodeNames.PHOTO).exists()   &&  dataSnapshot.child(NodeNames.APPROVAL).exists() && dataSnapshot.child(NodeNames.EXPIRY).exists() && dataSnapshot.child(NodeNames.NATID).exists()) {
                    progressDialog.cancel();
                    String approval = dataSnapshot.child(NodeNames.APPROVAL).getValue(String.class);
                    Date d1 = dataSnapshot.child(NodeNames.EXPIRY).getValue(Date.class);
                    Date d2=new java.util.Date();
                    long difference_In_Time = d1.getTime() - d2.getTime();

                      double exp = (difference_In_Time / (1000 * 60 * 60 * 24));

                        if (approval.equals(NodeNames.APPROVAL_PENDING) && exp  > 0) {

                            /*DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(exp);
                            String date = formatter.format(calendar.getTime());

                             */
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(d2.getTime());
                            Toast.makeText(LoginActivity.this, "INFO ! \t Your free posting trial will expire on \n" + d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h", Toast.LENGTH_LONG).show();

                            Intent mainIntent = new Intent(LoginActivity.this, Dashboard.class);


                            startActivity(mainIntent);
                            finish();

                        }
                        else if (approval.equals(NodeNames.APPROVAL_PENDING) && exp  <= 0)
                        {
                            //showPendingDialog();
                           // Toast.makeText(LoginActivity.this, "DIFFERENCE \n"+exp, Toast.LENGTH_LONG).show();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(d2.getTime());
                            Toast.makeText(LoginActivity.this, "EXPIRY! \t Your free posting trial  expired on \n" + d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h", Toast.LENGTH_LONG).show();

                            Intent mainIntent = new Intent(LoginActivity.this, Dashboard.class);


                            startActivity(mainIntent);
                            finish();

                        }


                        else if (exp > 0 && exp <= 5 && approval.equals(NodeNames.APPROVAL_ACTIVE))
                        {

                            //showExpiryDialog();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(d2.getTime());
                            Toast.makeText(LoginActivity.this, "ALERT! \t Your  posting subscription will expire on \n" + d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h", Toast.LENGTH_LONG).show();

                            Intent mainIntent = new Intent(LoginActivity.this, Dashboard.class);


                            startActivity(mainIntent);
                            finish();
                        }
                        else if (exp <= 0 && approval.equals(NodeNames.APPROVAL_ACTIVE))
                        {

                            //showExpiryDialog();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(d2.getTime());
                            Toast.makeText(LoginActivity.this, "EXPIRY ! \t Your  posting subscription expired on \n" + d1.getDate() +"/"+(d1.getMonth()+1) +"/"+calendar.get(Calendar.YEAR)+"\t"+d1.getHours()+":"+d1.getMinutes()+":"+d1.getSeconds()+"h", Toast.LENGTH_LONG).show();

                            Intent mainIntent = new Intent(LoginActivity.this, Dashboard.class);


                            startActivity(mainIntent);
                            finish();
                        }
                      /*  else
                        {

                            Intent mainIntent = new Intent(LoginActivity.this, Dashboard.class);


                            startActivity(mainIntent);
                            finish();
                        }

                       */


                }
                else
                {
                    progressDialog.cancel();


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                    builder1.setCancelable(true);
                    builder1.setTitle("ALERT");
                    builder1.setMessage("Your Profile is incomplete. \n Complete your profile");

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                    Intent mainIntent = new Intent(LoginActivity.this, RegisterNameActivity.class);


                                    startActivity(mainIntent);
                                    finish();
                                }
                            }).setNegativeButton(
                            "CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(LoginActivity.this, "An Error has occured kindly Try Again", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void showPendingDialog(){
        String title="NOT APPROVED!";
        String message="Sorry,your account has not approved.Your account needs to be approved inorder to enjoy this service. We charge Ksh 300 only per month for this service. \n \n" +
                "HOW TO APPROVE YOUR ACCOUNT \n \n" +
                "1.Send subscription  fee of ksh 300 to MPesa number 0707807623 (Japheth Nyakundi Nyarandi). \n \n" +
                "2.Send us the screen shot of the MPesa payment message and your national id no/passport no. through our WhatsApp number +254707807623. \n \n" +
                " 3.Admin Japheth Nyakundi  will approve your account within 2 hrs. \n \n " +
                "For any queries kindly  WhatsApp admin +254707807623 ";
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Alert !")
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.success_dialog, true)
                .positiveText("OK")
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        /*Intent mainIntent = new Intent(SignUpActivity.this, LaunchingActivity.class);
                        SignUpActivity.this.startActivity(mainIntent);
                        SignUpActivity.this.finish();

                         */
                    } })
                .build();
        android.view.View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        TextView tv2 = (TextView)view.findViewById(R.id.tv);



            tv2.setText(title);


        messageText.setText(message);

        dialog.show();
    }

    private void showBlockDialog(){
        String title="BLOKED";
        String message="Sorry your account is blocked \n " +
                "  WhatsApp admin +254707807623 for assistance";
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.success_dialog, true)
                .positiveText("OK")
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        /*Intent mainIntent = new Intent(SignUpActivity.this, LaunchingActivity.class);
                        SignUpActivity.this.startActivity(mainIntent);
                        SignUpActivity.this.finish();

                         */
                    } })
                .build();
        android.view.View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        TextView tv2 = (TextView)view.findViewById(R.id.tv);



        tv2.setText(title);


        messageText.setText(message);

        dialog.show();
    }
    private void showExpiryDialog(){
        String title="ACTIVATE";
        String message="Sorry,your account subscription expired.Subscribe inorder to enjoy this service.We charge Ksh 300 only per month for this service. \n \n" +
                "HOW TO SUBSCRIBE \n\n" +
                "1.Send subscription  fee of ksh 300 to MPesa number 0707807623 (Japheth Nyakundi Nyarandi). \n " +
                "2.Send us the screen shot of the MPesa payment message and your national id no/passport no. through our WhatsApp number +254707807623. \n \n" +
                " 3.Admin Japheth Nyakundi  will activate your account within 2 hrs.\n \n " +
                "For any queries kindly  WhatsApp admin +254707807623 ";
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Alert !")
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.success_dialog, true)
                .positiveText("OK")
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        /*Intent mainIntent = new Intent(SignUpActivity.this, LaunchingActivity.class);
                        SignUpActivity.this.startActivity(mainIntent);
                        SignUpActivity.this.finish();

                         */
                    } })
                .build();
        android.view.View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        TextView tv2 = (TextView)view.findViewById(R.id.tv);



        tv2.setText(title);


        messageText.setText(message);

        dialog.show();
    }
}
