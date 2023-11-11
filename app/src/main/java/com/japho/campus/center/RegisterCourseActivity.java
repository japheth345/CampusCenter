package com.japho.campus.center;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RegisterCourseActivity extends AppCompatActivity {
    ListView listView;

    ArrayAdapter<String> arrayAdapter;
    EditText etSearch;
    TextView tv1;
    private Button genderContinueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_course);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        tv1= findViewById(R.id.getStartedTextView2);
        genderContinueButton = findViewById(R.id.ageContinueButton);
        listView = findViewById(R.id.listView);
        etSearch = findViewById(R.id.etSearch);
        ArrayList<String> months = Helper.getCourses();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, months);
        listView.setAdapter(arrayAdapter);
        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tv1.setText(arrayAdapter.getItem(position));

                String rcons = arrayAdapter.getItem(position);
            }  });
    }

}