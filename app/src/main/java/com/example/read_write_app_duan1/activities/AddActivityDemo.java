package com.example.read_write_app_duan1.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.fragment.WriteLibraryFragment;
import com.example.read_write_app_duan1.models.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AddActivityDemo extends AppCompatActivity {
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
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_PDF_REQUEST = 2;
    private Uri imageUri;
    private Uri pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demo);
        unitUi();
        categoryList = new ArrayList<>();

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
                // Lấy dữ liệu từ EditText
                String name = edtTitle.getText().toString().trim();
                String content = edtDescrice.getText().toString().trim();

                // Kiểm tra xem người dùng đã chọn ảnh và PDF hay chưa
                if (imageUri != null && pdfUri != null) {
                    // Kiểm tra xem người dùng đã chọn loại sách hay chưa
                    if (!category.isEmpty()) {
                        // Tải ảnh lên Firebase Storage
                        uploadImageToFirebase(name, content);

                        Intent intent = new Intent(AddActivityDemo.this, AdminActivity.class);
                        intent.putExtra("name", name); // Passing "name" data to DuyetActivity
                        startActivity(intent);
                    } else {
                        // Thông báo cho người dùng chọn loại sách trước khi tải lên
                        Toast.makeText(AddActivityDemo.this, "Please select a category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi người dùng chưa chọn ảnh hoặc PDF
                }


            }

        });



        //handle click, attach pdf
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);

            }
        });

        //handle click, pick type
        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickDiaLog();
                loadPdfCaregories();
            }
        });


        imgAddCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }

        });
    }

    // Create an intent to select a PDF file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Ở đây bạn có thể hiển thị ảnh đã chọn nếu cần
            imgAddCover.setImageURI(imageUri);
        }
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();

            // Ở đây bạn có thể làm gì đó với tệp PDF đã chọn, ví dụ: hiển thị tên tệp, tải lên Firebase, v.v.
        }
    }

    private void uploadImageToFirebase(String name, String content) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference booksRef = storageRef.child("Book");

        String timeStamp = String.valueOf(System.currentTimeMillis());
        StorageReference imageRef = booksRef.child("image_" + timeStamp + ".jpg");
        StorageReference pdfRef = booksRef.child("pdf_" + timeStamp + ".pdf");


        // Tải ảnh lên Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Tải PDF lên Firebase Storage
                        pdfRef.putFile(pdfUri)
                                .addOnSuccessListener(pdfTaskSnapshot -> {
                                    pdfRef.getDownloadUrl().addOnSuccessListener(pdfUri -> {
                                        String pdfUrl = pdfUri.toString();

                                        // Lưu dữ liệu vào Realtime Database
                                        uploadDataToFirebase(imageUrl, name, content, pdfUrl, category);
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý khi tải PDF lên Firebase thất bại
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi tải ảnh lên Firebase thất bại
                });
    }


    private void uploadDataToFirebase(String imageUrl, String name, String content, String pdfUrl, String category) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Book");
        String bookId = databaseRef.push().getKey(); // Tạo ID mới cho mỗi sách
        HashMap<String, Object> bookData = new HashMap<>();
        bookData.put("image", imageUrl);
        bookData.put("name", name);
        bookData.put("description", content);
        bookData.put("type", category);
        bookData.put("content", pdfUrl);
        bookData.put("checkBook", 0);
        bookData.put("status", 0);
        bookData.put("write", 0);

        databaseRef.child(bookId).setValue(bookData)
                .addOnSuccessListener(aVoid -> {
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi lưu dữ liệu thất bại
                });
    }

    //TYPE
    private String category = "";

    private void categoryPickDiaLog() {

        //get string array of categories form arrayList
        String[] categoriesArray = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            categoriesArray[i] = categoryList.get(i).getType();
        }

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item, click
                        //get clicked  item from list
                        category = categoryList.get(which).getType();
                        //set to category textview
                        categoryTv.setText(category);


                    }
                })
                .show();
    }

    private void loadPdfCaregories() {

        categoryList = new ArrayList<>();

        //db reference to load categories ... db > Book
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BookType");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get data
                    Book model = ds.getValue(Book.class);
                    //add to arrayList
                    categoryList.add(model);

                }

                categoryPickDiaLog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Trả về phần mở rộng của tệp từ Uri
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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