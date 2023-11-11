package com.japho.campus.center;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.firebase.storage.StorageReference;
import com.japho.campus.center.Model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.japho.campus.center.Common.NodeNames;
public class RegisterProfileActivity extends AppCompatActivity {
    private TextView textview, tv;

    ImageView iv;
    private Button genderContinueButton, btSelect;
    private String uploadURL;
    private static final int GALLERY_INTENT = 2;
    private Intent data2 = null;
    private StorageReference mStorage;
    private DatabaseReference RootRef;
    private FirebaseAuth firebaseAuth;
    String email;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        mStorage = FirebaseStorage.getInstance().getReference();
        tv = findViewById(R.id.tvPercentage);
        progressDialog = new ProgressDialog(this);
        textview = findViewById(R.id.textView);
        btSelect = findViewById(R.id.btSelect);
        iv = findViewById(R.id.iv);
        genderContinueButton = findViewById(R.id.ageContinueButton);
        firebaseAuth = FirebaseAuth.getInstance();
        email = firebaseAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // selectImageCameraLayout.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // selectImageCameraLayout.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        genderContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data2 == null) {
                    Snackbar.make(RegisterProfileActivity.this, v, "Kindly select your profile picture", 5000).show();
                } else {
                    uploadImageToFirebase(data2);

                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //selectImageCameraLayout.setVisibility(View.VISIBLE);
            Picasso.get().load(data.getData()).into(iv);
            // uploadImageToFirebase(data);
            data2 = data;

        }
    }

    public void uploadImageToFirebase(Intent data) {

        tv.setVisibility(View.VISIBLE);
        // rl2.setVisibility(View.GONE);
        textview.setVisibility(View.GONE);
        btSelect.setVisibility(View.GONE);
        iv.setVisibility(View.GONE);
        genderContinueButton.setVisibility(View.GONE);

        tv.setText("Uploading  Profile pic... \n  \t" + 0 + "%");

        Uri uri = data.getData();
        StorageReference filePath = mStorage.child("COMRADES").child(email);

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        tv.setText("Profile Image uploaded successfully");

                        progressDialog.setTitle("Finalizing...");
                        progressDialog.setMessage("please wait");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uploadURL = uri.toString();
                                int n = Helper.getEdit();
                                if (n == 0) {

                                    RootRef.child("Users").child(email).child(NodeNames.PHOTO).setValue(uploadURL)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    RootRef.child("Users").child(email).child(NodeNames.ONLINE).setValue("true")
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {


                                                                    RootRef.child("Users").child(email).child(NodeNames.PHOTO).setValue(uploadURL)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    Date dt = new Date();
                                                                                    Calendar c = Calendar.getInstance();
                                                                                    c.setTime(dt);
                                                                                    c.add(Calendar.DATE, 5);
                                                                                    dt = c.getTime();
                                                                                    RootRef.child("Users").child(email).child(NodeNames.EXPIRY).setValue(dt)
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    RootRef.child("Users").child(email).child(NodeNames.APPROVAL).setValue(NodeNames.APPROVAL_PENDING)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {



                                                                                                                                    progressDialog.cancel();
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        Toast.makeText(RegisterProfileActivity.this, "Your account has been created successfully \n You have been awarded 5 free posting trial days", Toast.LENGTH_LONG).show();
                                                                                                                                        //finish();

                                                                                                                                        Intent mainIntent = new Intent(RegisterProfileActivity.this, MainActivity.class);

                                                                                                                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                                                        RegisterProfileActivity.this.startActivity(mainIntent);
                                                                                                                                        finish();
                                                                                                                                    }

                                                                                                                                }

                                                                                                                            });
                                                                                                                }
                                                                                                            });

                                                                                                }

                                                                                            });
                                                                                }

                                                                            });



                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegisterProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                }
                                else if (n == 1)
                                {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {

                                            String photo = teacherSnapshot.child(NodeNames.PHOTO).getValue(String.class);
                                            StorageReference filePath = mStorage.child("COMRADES").child(email);
                                            filePath.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {





                                    RootRef.child("Users").child(email).child(NodeNames.PHOTO).setValue(uploadURL)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    RootRef.child("Users").child(email).child(NodeNames.ONLINE).setValue("true")
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {


                                                                    RootRef.child("Users").child(email).child(NodeNames.PHOTO).setValue(uploadURL)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {




                                                                                                                                    progressDialog.cancel();
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        Helper.setEdit(0);
                                                                                                                                        Toast.makeText(RegisterProfileActivity.this, "Your account has been edited successfully", Toast.LENGTH_LONG).show();
                                                                                                                                        //finish();

                                                                                                                                        Intent mainIntent = new Intent(RegisterProfileActivity.this, MainActivity.class);
                                                                                                                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                                                        RegisterProfileActivity.this.startActivity(mainIntent);
                                                                                                                                        finish();
                                                                                                                                    }

                                                                                                                                }

                                                                                                                            });






                                                                                }

                                                                            });



                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(RegisterProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                                                }
                                            });

                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.cancel();
                                                            Toast.makeText(RegisterProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                            }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError e)
                                        {
                                            progressDialog.cancel();
                                            Toast.makeText(RegisterProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }















                            }
                        });
                    }})


                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(RegisterProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress1 = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                        double progress = Math.round((progress1) * 10) / 10;

                        tv.setText("Uploading Your Profile pic...\n  \t \t \t" + progress + "%");
                    }
                });
    }




}