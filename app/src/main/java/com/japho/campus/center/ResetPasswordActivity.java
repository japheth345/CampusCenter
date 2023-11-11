package com.japho.campus.center;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText fn, ln, em, pw1, pw2;
    private FirebaseAuth mAuth;
    private Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        em = (EditText) findViewById(R.id.em);
        b1 = (Button) findViewById(R.id.su);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendLink();
            }

            ;
        });

    }
    public void GoToLogin(View view)
    {
        Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void SendLink()
    {
        String email = em.getText().toString().trim();


        if (email.isEmpty()) {
            em.setError("First Name is required");
            em.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            em.setError("Please enter a valid email");
            em.requestFocus();
            return;
        }
        else
        {
            sendPasswordResetLink(email);
        }
    }
    private void sendPasswordResetLink(String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        // if (user != null) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    showDialog("SUCCESS", "We have sent a password reset link to your email",0);
                   // FirebaseAuth.getInstance().signOut();

                    em.setText("");


                } else {
                    showDialog2("ERROR", ""+task.getException().getMessage(),1);
                }

            }



        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showDialog2("ERROR", "FAILED BECAUSE \n"+e.getMessage(),1);
            }
        });
        //}
     /*  else
        {
            showDialog2("ERROR", "USER NOT FOUND \n"+email,1);
        }

      */
    }
    private void showDialog(String title, String message,int code){
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
                        dialog.dismiss();
                        finish();
                        Intent mainIntent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                        ResetPasswordActivity.this.startActivity(mainIntent);

                    }
                })
                .build();
        android.view.View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        TextView tv = (TextView)view.findViewById(R.id.tv);
        //ImageView imageView = (ImageView)view.findViewById(R.id.success);

        messageText.setText(message);
        tv.setText("");
        dialog.show();
    }

    private void showDialog2(String title, String message,int code){
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
                        dialog.dismiss();
                        //  fn.setText(null);

                    }
                })
                .build();
        android.view.View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        TextView tv = (TextView)view.findViewById(R.id.tv);

        messageText.setText(message);
        tv.setText("");
        dialog.show();
    }
    public void onLoginClicked(View view)
    {
        Intent mainIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(mainIntent);
        finish();
    }
}