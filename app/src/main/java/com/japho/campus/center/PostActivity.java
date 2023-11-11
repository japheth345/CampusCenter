package com.japho.campus.center;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hendraanggrian.appcompat.socialview.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;
import android.widget.AdapterView;
public class PostActivity extends AppCompatActivity {

    private Uri imageUri;
    private String imageUrl;

    private ImageView close;
    private ImageView imageAdded,attach;
    private TextView post;
    SocialAutoCompleteTextView description;
    private TextView textview;
    String univ="";
    int gender = -1;
    String gen=null;
    private Button genderContinueButton;
    private RadioGroup mRadioGroup;
    private RadioButton r1, r2, r3;
    String[] tribes;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
       attach = findViewById(R.id.attach);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);
        textview = findViewById(R.id.textView);

        tribes = Helper.getUniversities2();
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        r1 = findViewById(R.id.rb1);
        r2 = findViewById(R.id.rb2);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this , SMainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(v);
            }
        });
       attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(PostActivity.this);
            }
        });

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(PostActivity.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner5);

                // set custom height and width
                dialog.getWindow().setLayout(650, 800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();
                TextView tv = dialog.findViewById(R.id.tv);
                tv.setText("Select University");
                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(PostActivity.this, android.R.layout.simple_list_item_1, tribes);

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
                       univ = adapter.getItem(position);

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void upload(View v) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");

        if (r1.isChecked()) {
            gender = 1;
        } else if (r2.isChecked()) {
            gender = 2;
        }

        if (gender > 0) {

            Snackbar.make(PostActivity.this, v, "Kindly fill  all fields", 5000).show();

            if (univ == "" || univ.equals(null)) {
                Snackbar.make(PostActivity.this, v, "Please select your audience", 5000).show();
            }
            else
            {


                if (imageUri != null) {

                    final StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    pd.show();
                    filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageUrl = uri.toString();

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                            String postId = ref.push().getKey();
                                            String gen = null;
                                            if (gender == 1) {
                                                gen = "No";
                                            }
                                            if (gender == 2) {
                                                gen = "Yes";
                                            }
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("postid", postId);
                                            map.put("imageurl", imageUrl);
                                            map.put("description", description.getText().toString());
                                            map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            map.put("audience", univ);
                                            map.put("visible", gen);
                                            ref.child(postId).setValue(map);

                                            DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference().child("HashTags");
                                            List<String> hashTags = description.getHashtags();
                                            if (!hashTags.isEmpty()) {
                                                for (String tag : hashTags) {
                                                    map.clear();

                                                    map.put("tag", tag.toLowerCase());
                                                    map.put("postid", postId);

                                                    mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                                                }
                                            }

                                            pd.dismiss();
                                            Toast.makeText(PostActivity.this, "Post uploaded succsessfully", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(PostActivity.this, SMainActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Failed an error occurred \n try again ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress1 = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                                    double progress = Math.round((progress1) * 10) / 10;

                                    pd.setMessage("uploading...\t" + progress);
                                }
                            });
                    ;
                } else {
                    //Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = ref.push().getKey();
                    String gen = null;
                    if (gender == 1) {
                        gen = "No";
                    }
                    if (gender == 2) {
                        gen = "Yes";
                    }
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postid", postId);
                    map.put("imageurl", "default");
                    map.put("description", description.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put("audience", univ);
                    map.put("visible", gen);
                    ref.child(postId).setValue(map);

                    DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference().child("HashTags");
                    List<String> hashTags = description.getHashtags();
                    if (!hashTags.isEmpty()) {
                        for (String tag : hashTags) {
                            map.clear();

                            map.put("tag", tag.toLowerCase());
                            map.put("postid", postId);

                            mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                        }
                    }

                    pd.dismiss();
                    startActivity(new Intent(PostActivity.this, SMainActivity.class));
                    finish();
                }

            }
        } else {


            Snackbar.make(PostActivity.this, v, "Kindly fill  all fields", 5000).show();

        }
    }

    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageAdded.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this , SMainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ArrayAdapter<Hashtag> hashtagAdapter = new HashtagArrayAdapter<>(getApplicationContext());

        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hashtagAdapter.add(new Hashtag(snapshot.getKey() , (int) snapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        description.setHashtagAdapter(hashtagAdapter);
    }
}
