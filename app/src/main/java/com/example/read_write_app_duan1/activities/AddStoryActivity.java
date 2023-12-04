package com.example.read_write_app_duan1.activities;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AddStoryActivity extends AppCompatActivity {
    private TextView tvSkip, categoryTv;
    private ImageView imgPrev, imgAddCover, imgUpload;
    private EditText edtTitle, edtDescrice;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    StorageReference reference = FirebaseStorage.getInstance().getReference();

    //progress dialog
    private ProgressDialog progressDialog;
    // arrayList to hold pdf categories
    private ArrayList<Book> categoryList;
    private static final int IMAGE_REQUEST = 1;
    private static final int PDF_REQUEST = 2;
    private Uri pdfUri;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        unitUi();
        unitListener();
        loadPdfCaregories();

    }

    private void unitListener() {
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //handle click, upload pdf
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        //handle click, attach pdf
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePdf();
            }
        });

        //handle click, pick type
        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });


        imgAddCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
    }


    // Hàm để mở Intent và chọn hình ảnh từ thiết bị
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private void uploadImageAndPdfToFirebase(Uri imageUri) {
        StorageReference fileReference = reference.child("images/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        String imageUrl = downloadUri.toString();
                        // Gọi hàm để lưu URL của hình ảnh vào Firebase Realtime Database
                        saveImageUrlToDatabase(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi tải lên thất bại
                });

    }
    private void saveImageUrlToBookImage(String bookId, String imageUrl) {
        DatabaseReference bookImageRef = FirebaseDatabase.getInstance().getReference("Book").child(bookId);
        bookImageRef.push().setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Xử lý khi lưu URL của hình ảnh vào mục "images" thành công
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi lưu URL của hình ảnh vào mục "images" thất bại
                });


    }

    //hàm chính xử dụng chung
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Gọi hàm để tải hình ảnh lên Firebase Realtime Database
            uploadImageAndPdfToFirebase(imageUri);

            // Hiển thị hình ảnh đã chọn lên ImageView
            imgAddCover.setImageURI(imageUri);
        }
    }
    private void saveImageUrlToDatabase(String imageUrl) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Book"); // Thay "Book" thành tên node bạn muốn lưu trữ sách

        // Lấy thông tin từ EditText
        String name = edtTitle.getText().toString().trim();
        String description = edtDescrice.getText().toString().trim();
         String selectedCategory = categoryTv.getText().toString();


        // Tạo một đối tượng Book với thông tin và URL của hình ảnh
        Book newBook = new Book();
        newBook.setImage(imageUrl); // Lưu URL của hình ảnh
        newBook.setName(name);
        newBook.setDiscription(description);
        newBook.setType(selectedCategory);




        // Lưu thông tin của sách vào Firebase Realtime Database
        DatabaseReference newBookRef = database.push();
        newBookRef.setValue(newBook)
                .addOnSuccessListener(aVoid -> {
                    String bookId = newBookRef.getKey(); // Lấy ID của sách vừa thêm
                    // Lưu URL của hình ảnh vào mục "images" trong sách tương ứng
                    saveImageUrlToBookImage(bookId, imageUrl);
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi lưu thông tin sách thất bại
                });
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Trả về phần mở rộng của tệp từ Uri
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    //TYPE
    private void loadPdfCaregories() {
        categoryList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BookType");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Book model = ds.getValue(Book.class);
                    categoryList.add(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
    private void showCategoryDialog() {
        String[] categoriesArray = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            categoriesArray[i] = categoryList.get(i).getType();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedCategory = categoriesArray[which];
                        categoryTv.setText(selectedCategory);
                    }
                })
                .show();
    }
//File Pdf
private void choosePdf() {
    Intent intent = new Intent();
    intent.setType("application/pdf");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(intent, PDF_REQUEST  );
}




    private void unitUi() {
        tvSkip = findViewById(R.id.tvSkip);
        imgPrev = findViewById(R.id.imgPrev);
        imgAddCover = findViewById(R.id.imgAddCover);
        imgUpload = findViewById(R.id.attackBtn);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescrice = findViewById(R.id.edtDescription);
        firebaseAuth = FirebaseAuth.getInstance();
        categoryTv = findViewById(R.id.categoryTv);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }
}