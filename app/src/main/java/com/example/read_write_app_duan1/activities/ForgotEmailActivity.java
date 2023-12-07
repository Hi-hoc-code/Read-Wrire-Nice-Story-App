package com.example.read_write_app_duan1.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.read_write_app_duan1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotEmailActivity extends AppCompatActivity {
    EditText edtNhapEmail;
    Button btnForgotEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_email);
        edtNhapEmail = findViewById(R.id.edtNhapGmail);
        btnForgotEmail = findViewById(R.id.btnForgotPasswordEmail);

        btnForgotEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = edtNhapEmail.getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Intent intent = new Intent(ForgotEmailActivity.this, LoginActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("emailAddress", emailAddress);
//                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                });
            }
        });
    }
}