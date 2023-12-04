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
import android.widget.TextView;
import android.widget.Toast;

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
    EditText edtRegisterEmail, edtRegisterUsername, edtRegisterPass, edtDateOfBirth,edtConfirmPassword;
    Button btnRegister;
    TextView tvDangnhaprs;
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
        edtConfirmPassword = findViewById(R.id.edtConfirmRegisterPass);
        btnRegister = findViewById(R.id.btnRegister);
        tvDangnhaprs = findViewById(R.id.tvDangnhaprs);

        tvDangnhaprs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtRegisterEmail.getText().toString();
                String password = edtRegisterPass.getText().toString();
                String username = edtRegisterUsername.getText().toString();
                String confirmpass = edtConfirmPassword.getText().toString();
                String dateOfBirth = edtDateOfBirth.getText().toString();

                if (email.isEmpty()) {
                    edtRegisterEmail.setError("Email cannot be empty");
                }
                if (!email.matches("^[a-zA-Z][a-zA-Z0-9._]+@gmail\\.com$")) {
                    edtRegisterEmail.setError("Invalid email format");
                }
                // Emmail phải có đuôi @gamil.com
                // bắt đầu Email không có ký tự đặc biệt hoặc số

                if (password.isEmpty()) {
                    edtRegisterPass.setError("Password cannot be empty");
                }
                if (password.length() < 8) {
                    edtRegisterPass.setError("Password must be at least 8 characters long");
                } else if (!password.matches(".*[a-z].*")) {
                    edtRegisterPass.setError("Password must contain at least one lowercase letter");
                } else if (!password.matches(".*[A-Z].*")) {
                    edtRegisterPass.setError("Password must contain at least one uppercase letter");
                } else if (!password.matches(".*\\d.*")) {
                    edtRegisterPass.setError("Password must contain at least one digit");
                } else if (!password.matches(".*[@$!%*?&].*")) {
                    edtRegisterPass.setError("Password must contain at least one special character");
                } else {
//                    Toast.makeText(RegisterActivity.this, "Password không hợp lệ", Toast.LENGTH_SHORT).show();
                }
                // password lớn hơn 8 ký tự
                // có số , thường , hoa , ktdb
                if (username.isEmpty()) {
                    edtRegisterUsername.setError("Username cannot be empty");
                }
                if (!username.matches("^[a-zA-Z0-9]{5,}$")) {
                    edtRegisterUsername.setError("Invalid username format");
                }
                // Lớn 5 ki tự , không có kí tự db.
                if (!confirmpass.equals(password)) {
                    edtConfirmPassword.setError("Passwords do not match");
                }
                //check lỗi confirm pass
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
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            Log.w(TAG, "Đăng ký thất bại", task.getException());
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void writeNewUser(String userId, String email, String username, String password, String dateOfBirth) {
        Users user = new Users(userId, email, username, password,  dateOfBirth);

        mDatabase.child("user").child(userId).setValue(user);
    }
}