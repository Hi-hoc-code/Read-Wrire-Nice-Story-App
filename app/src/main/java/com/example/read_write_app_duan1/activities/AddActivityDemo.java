    package com.example.read_write_app_duan1.activities;

    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;

    import android.app.ProgressDialog;
    import android.content.ContentResolver;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.net.Uri;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.webkit.MimeTypeMap;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.ProgressBar;
    import android.widget.TextView;

    import com.example.read_write_app_duan1.R;
    import com.example.read_write_app_duan1.models.Book;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;
    import com.google.firebase.storage.UploadTask;

    import java.io.FileNotFoundException;
    import java.io.InputStream;
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
        private static final int IMAGE_REQUEST = 1;
        private static final int PDF_REQUEST = 2;
        private Uri pdfUri;
        private Uri imageUri;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_demo);
            unitUi();

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
                    categoryPickDiaLog();
                }
            });


            imgAddCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();
                }
            });
        }

        private void chooseImage() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_REQUEST);
        }

        private void uploadPdfToStorage(Uri uri) {
            StorageReference fileRef = reference.child("pdfs/" + System.currentTimeMillis() + "." + getFileExtension(uri));

            fileRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {

                        // Lấy đường dẫn của PDF sau khi tải lên thành công
                        fileRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            pdfUrl = downloadUri.toString();

                            // Gọi hàm uploadDataToDatabase sau khi tải lên PDF thành công
                            uploadDataToDatabase(pdfUrl, "");
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý khi tải lên thất bại
                    });
        }

        private void uploadImageToFirebase(Uri uri) {
            final StorageReference fileReference = reference.child("images/" + System.currentTimeMillis() + "." + getFileExtension(uri));
            UploadTask uploadTask = fileReference.putFile(uri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri != null) {
                        String imageUrl = downloadUri.toString();

                        // Gọi hàm uploadDataToDatabase sau khi tải lên ảnh thành công
                        uploadDataToDatabase(pdfUrl, imageUrl);
                    }
                } else {
                    // Xử lý khi không thể lấy được URL
                }
            });
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri pdfUri = data.getData();
                uploadPdfToStorage(pdfUri);
            } else if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                uploadImageToFirebase(imageUri);
            }

        }

        private void choosePdf() {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PDF_REQUEST);
        }

        private String getFileExtension(Uri uri){
            ContentResolver cr = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cr.getType(uri));
        }
        private String pdfUrl;
            private void uploadDataToDatabase (String pdfUrl, String imageUrl){
                // Tạo một unique ID cho dữ liệu mới
                String id = FirebaseDatabase.getInstance().getReference("Book1").push().getKey();

                // Lấy thông tin từ EditText hoặc các thành phần khác
                String description = edtDescrice.getText().toString().trim();
                String name = edtTitle.getText().toString().trim();
                String category = categoryTv.getText().toString().trim();
    //            String imageUrl = (imageUri != null) ? imageUri.toString() : ""; // Đường dẫn ảnh đã lấy từ ImageView

                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put("id", id);
                dataMap.put("type", category);
                dataMap.put("name", name);
                dataMap.put("description", description);
                dataMap.put("pdfUrl", pdfUrl);
                dataMap.put("imageUrl", imageUrl);

                FirebaseDatabase.getInstance().getReference("Book").child(id)
                        .setValue(dataMap)
                        .addOnSuccessListener(aVoid -> {
                            // Xử lý khi lưu thành công
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý khi lưu thất bại
                        });
            }
        private void categoryPickDiaLog(){
            if (categoryList !=null){
            //get string array of categories form arrayList
            String[] categoriesArray = new String[categoryList.size()];
            for (int i=0; i<categoryList.size(); i++){
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
                            String category = categoriesArray[which];
                            //set to category textview
                            categoryTv.setText(category);

                        }
                    })
                    .show();
        }
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