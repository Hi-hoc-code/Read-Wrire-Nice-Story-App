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
import com.example.read_write_app_duan1.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    EditText edtRegisterEmail, edtRegisterUsername, edtRegisterPass, edtDateOfBirth;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                String username = edtRegisterUsername.getText().toString();
                String dateOfBirth = edtDateOfBirth.getText().toString();

                if (email.isEmpty()) {
                    edtRegisterEmail.setError("Email cannot be empty");
                }
                if (password.isEmpty()) {
                    edtRegisterPass.setError("Password cannot be empty");
                }
                if (username.isEmpty()) {
                    edtRegisterUsername.setError("Username cannot be empty");
                }
                if (dateOfBirth.isEmpty()) {
                    edtDateOfBirth.setError("Date of birth cannot be empty");
                } else {
                    createAccount(email, password, username, dateOfBirth);
                }
            }
        });
    }
    private void createAccount(String email, String pass, String username, String dateOfBirth) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Đăng ký thành công");
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid(),email, username, pass, dateOfBirth);
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
    public void writeNewUser(String userId, String email, String username, String password, String dateOfBirth) {
        Users user = new Users(userId, email, username, password, dateOfBirth);

        mDatabase.child("user").child(userId).setValue(user);
    }
}