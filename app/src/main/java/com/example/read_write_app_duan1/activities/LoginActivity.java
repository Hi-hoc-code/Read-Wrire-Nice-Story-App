package com.example.read_write_app_duan1.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.read_write_app_duan1.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button btnFacebookAuth, btnGoogleAuth, btnDangNhap;
    EditText edtLoginUsername, edtLoginPassword;
    FirebaseAuth mAuth;
    TextView tvquenmk,tvDangky;
    GoogleSignInOptions gos;
    GoogleSignInClient gsc;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(), "ACcount null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnFacebookAuth = findViewById(R.id.btnFacebookAuth);
        btnGoogleAuth = findViewById(R.id.btnGoogleAuth);
        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        tvquenmk = findViewById(R.id.tvquenmk);
        tvDangky = findViewById(R.id.tvDangKylg);

        gos = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gos);

        mAuth = FirebaseAuth.getInstance();

//        Bundle bundle = getIntent().getExtras();
//        String emailAddress = bundle.getString("emailAddress", "No value");
//        edtLoginUsername.setText(emailAddress);

        tvDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        tvquenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotEmailActivity.class);
                startActivity(intent);
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String email = edtLoginUsername.getText().toString();
                String password = edtLoginPassword.getText().toString();

                //bat loi
                if (!email.matches("^[a-zA-Z][a-zA-Z0-9._]+@gmail\\.com$")) {
                    edtLoginUsername.setError("Invalid email format");
                }
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Tài Khoản Hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gọi phương thức signInWithEmailAndPassword chỉ khi email và password không trống
                mAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user
                                    Toast.makeText(LoginActivity.this, "Bạn sống lỗi rồi", Toast.LENGTH_SHORT).show();
                                }
                            }
                            // Xử lý kết quả đăng nhập
                        });
            }
//
        });
        btnGoogleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   signIn();
            }
        });

        // sự kiện  đăng nhập bằng fb
        btnFacebookAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateTosecondActivity();
            }catch (ApiException e){
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateTosecondActivity() {
        finish();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
}