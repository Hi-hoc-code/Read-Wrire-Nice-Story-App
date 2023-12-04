    package com.example.read_write_app_duan1.activities;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;

    import android.app.ProgressDialog;
    import android.content.ContentResolver;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.database.Cursor;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.OpenableColumns;
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
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;

    import java.util.ArrayList;

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
        private static final int IMAGE_REQUEST = 1;
        private static final int PDF_REQUEST = 2;
        private Uri pdfUri;
        private Uri imageUri;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_demo);
            unitUi();

            //handle click, upload pdf
            tvSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadDataToFirebase();
                }
            });

            //handle click, attach pdf
            imgUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choosePdf();
                }
            });



            imgAddCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImageChooser();
                }
            });
        }
        private void uploadDataToFirebase() {
            // Check if the required fields are not empty
            String name = edtTitle.getText().toString().trim();
            String description = edtDescrice.getText().toString().trim();
            String category = categoryTv.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(category)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if both image and PDF have been selected
            if (imageUri == null || pdfUri == null) {
                Toast.makeText(this, "Please select both image and PDF", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("PDF_URI_UPLOAD", pdfUri.toString());
            // Upload image and PDF to Firebase
            uploadImageAndPdfToFirebase(imageUri, pdfUri);
        }
        private void openImageChooser() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_REQUEST);
        }

        private void choosePdf() {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PDF_REQUEST);
        }

        private void uploadImageAndPdfToFirebase(Uri imageUri, Uri pdfUri) {
            // Create separate references for image and PDF
            StorageReference imageReference = reference.child("image/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
            StorageReference pdfReference = reference.child("content/" + System.currentTimeMillis() + "." + getFileExtension(pdfUri));

            // Upload image file
            imageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageReference.getDownloadUrl().addOnSuccessListener(downloadUriImage -> {
                            String imageUrl = downloadUriImage.toString();

                            // Upload PDF file
                            pdfReference.putFile(pdfUri)
                                    .addOnSuccessListener(taskSnapshotPdf -> {
                                        pdfReference.getDownloadUrl().addOnSuccessListener(downloadUriPdf -> {
                                            String pdfUrl = downloadUriPdf.toString();
                                            Toast.makeText(AddActivityDemo.this, "Upload PDF succfully", Toast.LENGTH_SHORT).show();
                                            // Save both URLs to the database
                                            saveUrlsToDatabase(imageUrl, pdfUrl);
                                        });
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle PDF upload failure
                                        Toast.makeText(AddActivityDemo.this, "Upload PDF failed", Toast.LENGTH_SHORT).show();
                                    });

                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle image upload failure
                        Toast.makeText(AddActivityDemo.this, "Upload image failed", Toast.LENGTH_SHORT).show();
                    });
        }

        private void saveUrlsToDatabase(String imageUrl, String pdfUrl) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Book");

            String name = edtTitle.getText().toString().trim();
            String description = edtDescrice.getText().toString().trim();
            String category = categoryTv.getText().toString();

            Book newBook = new Book();
            newBook.setImage(imageUrl);

            if (pdfUrl != null && !pdfUrl.isEmpty()) {
                newBook.setContent(pdfUrl);
            } else {
                // Xử lý khi pdfUrl không hợp lệ
            }
            newBook.setName(name);
            newBook.setDiscription(description);
            newBook.setType(category);

            DatabaseReference newBookRef = database.push();
            newBookRef.setValue(newBook)
                    .addOnSuccessListener(aVoid -> {
                        String bookId = newBookRef.getKey();
                        // Xử lý khi lưu thông tin sách thành công
                        Toast.makeText(AddActivityDemo.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý khi lưu thông tin sách thất bại
                        Toast.makeText(AddActivityDemo.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                    });
        }
// ...

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                // Hiển thị hình ảnh đã chọn lên ImageView
                imgAddCover.setImageURI(imageUri);
            } else if (requestCode == PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                pdfUri = data.getData();
                String pdfFileName = getFileName(pdfUri);
                Log.d("PDF_URI", pdfUri.toString());
                // Hiển thị tên tệp PDF đã chọn (nếu cần)
                // Ví dụ: Hiển thị tên tệp PDF trong một TextView
                // pdfFileNameTextView.setText(pdfFileName);
            }
        }
        private String getFileName(Uri uri) {
            String result = null;
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex >= 0) {
                        result = cursor.getString(displayNameIndex);
                    } else {
                        // Nếu cột không tồn tại, xử lý tương ứng (ở đây bạn có thể gán một giá trị mặc định hoặc thực hiện hành động khác)
                        Log.e("getFileName", "Column " + OpenableColumns.DISPLAY_NAME + " does not exist");
                    }
                }
            }
            if (result == null) {
                result = uri.getLastPathSegment();
            }
            return result;
        }


// ...

// ...
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