package com.example.read_write_app_duan1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.read_write_app_duan1.R;

public class AddStoryActivity extends AppCompatActivity {
private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        tvSkip = findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddStoryActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });
    }
}