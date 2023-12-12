package com.example.read_write_app_duan1.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

                openDialogEmail();


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
    private void openDialogEmail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_forgot, null);
        builder.setView(view);
        //show len man hinh
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        findViewById(R.id.layoutLogin).setVisibility(View.GONE);

        // Set a callback when the dialog is dismissed
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Set the visibility of the login screen back to VISIBLE
                findViewById(R.id.layoutLogin).setVisibility(View.VISIBLE);
            }
        });
        //hien thi bo goc mat di lhoang trang
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //anh xa
        EditText edtEmail = view.findViewById(R.id.edtNhapGmail);
        Button btnSend = view.findViewById(R.id.btnForgotPasswordEmail);
        Button btnCancel = view.findViewById(R.id.btnNoThank);

        //
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //ut gui
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = edtEmail.getText().toString();
                if (!emailAddress.matches("^[a-zA-Z][a-zA-Z0-9._]+@gmail\\.com$")) {
                    edtEmail.setError("Invalid email format");
                    return;
                }
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    alertDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Đã Gửi về Gmail, Vui Lòng Check Gmail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    }