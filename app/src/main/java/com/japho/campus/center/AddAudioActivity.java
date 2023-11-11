package com.japho.campus.center;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;




import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
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
import com.japho.campus.center.Model.ConnectivityUtils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.widget.AdapterView;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
public class AddAudioActivity extends AppCompatActivity implements  MediaPlayer.OnCompletionListener {
    private ImageView close;

    private Button post;
    SocialAutoCompleteTextView description;
    private TextView textview,textview5,textview6;
    String univ="";
    String  userTribe="";
    String     userCounty="";
    int gender = -1;
    String gen=null;
    private Button genderContinueButton;
    private RadioGroup mRadioGroup;
    private RadioButton r1, r2, r3;
    String[] univs,tribes,counties;
    Dialog dialog;
    ArrayList<Place> places = new ArrayList<>();
    ArrayList<String> county = new ArrayList<>();
    private ImageView attach;
    private TextView imageAdded;


    private Uri imageUri;
    private String imageUrl;
    MediaPlayer mediaPlayer;
    private static final int REQUEST_CODE_PICK_VIDEO = 103;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audio);

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        close = findViewById(R.id.close);
        mediaPlayer=new MediaPlayer();
        attach = findViewById(R.id.attach);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);
        textview = findViewById(R.id.textView);
        textview5 = findViewById(R.id.textView5);
        textview6 = findViewById(R.id.textView6);
        imageAdded = findViewById(R.id.image_added);

        univs = Helper.getUniversities2();
        tribes = Helper.getTribes2();
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        r1 = findViewById(R.id.rb1);
        r2 = findViewById(R.id.rb2);
        places = Helper.getPlaces();
        if (places.size() <= 0)
        {

        } else
        {
            county.clear();
            county.add("All Counties");
            for (int i = 0; i < places.size(); i++) {
                if (county.contains(places.get(i).getCounty())) {

                } else {
                    county.add(places.get(i).getCounty());
                }
            }
            counties = new String[county.size()];
            for (int i = 0; i < county.size(); i++) {
                counties[i] = county.get(i);
            }
            java.util.Arrays.sort(counties);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAudioActivity.this , SMainActivity.class));
                finish();
            }
        });
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAdded.setVisibility(View.INVISIBLE);
                Intent intentVideo = new Intent();

                intentVideo.setType("audio/*");
                intentVideo.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentVideo, REQUEST_CODE_PICK_VIDEO);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(v);
            }
        });

        textview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog = new Dialog(AddAudioActivity.this  );

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout(650, 800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                TextView tv = dialog.findViewById(R.id.tv);
                tv.setText("Select  County");
                EditText editText = dialog.findViewById(R.id.edit_text);

                ListView listView = dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddAudioActivity.this, android.R.layout.simple_list_item_1, counties);

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

                        textview6.setText(adapter.getItem(position));
                        userCounty = adapter.getItem(position);


                        dialog.dismiss();
                    }
                });
            }
        });

        textview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(AddAudioActivity.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner5);

                // set custom height and width
                dialog.getWindow().setLayout(650, 800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();
                TextView tv = dialog.findViewById(R.id.tv);
                tv.setText("Select Tribe");
                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddAudioActivity.this , android.R.layout.simple_list_item_1, tribes);

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

                        textview5.setText(adapter.getItem(position));
                        userTribe = adapter.getItem(position);

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(AddAudioActivity.this );

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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddAudioActivity.this , android.R.layout.simple_list_item_1, univs);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_VIDEO && resultCode == RESULT_OK){
            imageUri = data.getData();
            try {
            imageAdded.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String displayName = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = this.getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            imageAdded.setText(displayName);
                        }
                    } finally {
                        cursor.close();
                    }
                }
                else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                    imageAdded.setText(displayName);
                }


            else
            {
                Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddAudioActivity.this, SMainActivity.class));
                finish();
            }

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mediaPlayer.setDataSource(getApplicationContext(),imageUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Toast.makeText(this, "Can,t play audio \n Kindly choose another audio!", Toast.LENGTH_LONG).show();
            }


        }
        else {
            Toast.makeText(this, "An Error occurred Kindly Try again!", Toast.LENGTH_LONG).show();
            // startActivity(new Intent(AddVideoActivity.this  , SMainActivity.class));
            //finish();
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
    private void upload(View v) {
        boolean isConnected = ConnectivityUtils.isInternetConnected(getApplicationContext());
        if (isConnected) {

            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading");
            pd.setCancelable(false);
            if (r1.isChecked()) {
                gender = 1;
            } else if (r2.isChecked()) {
                gender = 2;
            }

            if (gender > 0) {

                if (imageUri == null) {
                    Snackbar.make(AddAudioActivity.this, v, "Please select an audio", 5000).show();
                } else if (univ == "") {
                    Snackbar.make(AddAudioActivity.this, v, "Please select university", 5000).show();
                } else if (userTribe == "") {
                    Snackbar.make(AddAudioActivity.this, v, "Please select tribe", 5000).show();
                } else if (userCounty == "") {
                    Snackbar.make(AddAudioActivity.this, v, "Kindly select  County", 5000).show();
                } else if (description.getText().toString().equals("")) {
                    Snackbar.make(AddAudioActivity.this, v, "Kindly type the text", 5000).show();
                } else {

                    final StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    pd.show();
                    filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();

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
                                    Date dt = new Date();
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(dt);
                                    c.add(Calendar.DATE, 10);
                                    dt = c.getTime();
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("postid", postId);
                                    map.put("imageurl", imageUrl);
                                    map.put("description", description.getText().toString());
                                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    map.put("audience", univ);
                                    map.put("visible", gen);
                                    map.put("tribe", userTribe);
                                    map.put("county", userCounty);
                                    map.put("type", "audio");
                                    map.put("exp", dt);
                                    ref.child(postId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference().child("HashTags");
                                                List<String> hashTags = description.getHashtags();
                                                if (!hashTags.isEmpty()) {
                                                    for (String tag : hashTags) {
                                                        map.clear();

                                                        map.put("tag", tag.toLowerCase());
                                                        map.put("postid", postId);

                                                        mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                } else {
                                                                    Toast.makeText(AddAudioActivity.this, "An error has occurred \n Try again", Toast.LENGTH_LONG).show();
                                                                }

                                                            }
                                                        });
                                                    }
                                                }
                                                mediaPlayer.stop();
                                                pd.dismiss();
                                                startActivity(new Intent(AddAudioActivity.this, SMainActivity.class));
                                                Toast.makeText(AddAudioActivity.this, "post uploaded successfully", Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                mediaPlayer.stop();
                                                Toast.makeText(AddAudioActivity.this, "An error has occurred \n Try again", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mediaPlayer.stop();
                                    Toast.makeText(AddAudioActivity.this, "An error has occurred \n Try again", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddAudioActivity.this, "An error has occurred \n Try again", Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress1 = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                            double progress = Math.round((progress1) * 10) / 10;

                            pd.setMessage("uploading...\t" + progress + "%");
                        }
                    });
                }
            } 
            else {


                Snackbar.make(AddAudioActivity.this, v, "Kindly fill  all fields", 5000).show();

            }
        }
        else
        {
            Snackbar.make(AddAudioActivity.this, v, "You are not connected to internet", 5000).show();
        }
    }
    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }

    public void onCompletion(MediaPlayer mediapalyer)
    {
        mediaPlayer.seekTo(0);
       mediaPlayer.start();
      /*  AlertDialog.Builder obj = new AlertDialog.Builder(this);
        obj.setTitle("Playback Finished!");
        obj.setIcon(R.mipmap.ic_launcher);
        MyListener m = new MyListener();
        obj.setPositiveButton("Replay", m);
        obj.setNegativeButton("Cancel", m);
        obj.setMessage("Replay the video?");
        obj.show();

       */
    }

    class MyListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which)
        {
            if (which == -1) {
              //  imageAdded.seekTo(0);
                //imageAdded.start();
            }
            else {
                dialog.dismiss();
            }
        }
    }


}