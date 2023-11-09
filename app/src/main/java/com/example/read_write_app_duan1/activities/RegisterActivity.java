package com.example.read_write_app_duan1.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.read_write_app_duan1.MainActivity;
import com.example.read_write_app_duan1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edtRegisterEmail, edtRegisterUsername, edtRegisterPass, edtDateOfBirth;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

        //Ánh xạ
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterUsername = findViewById(R.id.edtRegisterUsername);
        edtRegisterPass = findViewById(R.id.edtRegisterPass);
        edtDateOfBirth = findViewById(R.id.edtDateOfBirth);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtRegisterEmail.getText().toString();
                String password = edtRegisterPass.getText().toString();
                createAccount(email, password);
            }
        });
    }
    private void createAccount(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Đăng ký thành công");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } else {
                            Log.w(TAG, "Đăng ký thất bại", task.getException());
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}