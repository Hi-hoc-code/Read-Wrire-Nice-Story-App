    package com.example.read_write_app_duan1.activities;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;

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

                }
            });

            //handle click, pick type
            categoryTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   loadPdfCaregories();
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

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                // Gọi hàm để tải hình ảnh lên Firebase Realtime Database
                uploadImageToFirebase(imageUri);

                // Hiển thị hình ảnh đã chọn lên ImageView
                imgAddCover.setImageURI(imageUri);
            }
        }

        private void uploadImageToFirebase(Uri imageUri) {
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

        private void saveImageUrlToDatabase(String imageUrl) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Book"); // Thay "Book" thành tên node bạn muốn lưu trữ sách

            // Lấy thông tin từ EditText
            String name = edtTitle.getText().toString().trim();
            String description = edtDescrice.getText().toString().trim();
            String category = "";


            // Tạo một đối tượng Book với thông tin và URL của hình ảnh
            Book newBook = new Book();
            newBook.setImage(imageUrl); // Lưu URL của hình ảnh
            newBook.setName(name);
            newBook.setDiscription(description);
            newBook.setType(category);


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

        private String getFileExtension(Uri uri) {
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            // Trả về phần mở rộng của tệp từ Uri
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }

        //các dữ liệu để chon categoty
        private void showDialogToSelectCategory() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Category");

            // Chuẩn bị danh sách loại từ ArrayList hoặc từ Firebase

            String[] categoryArray = getCategoryNames(); // Hàm này trả về một mảng các tên loại

            builder.setItems(categoryArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String selectedCategory = categoryArray[which];
                    // Làm điều gì đó với loại đã chọn
                    // Ví dụ: hiển thị loại đã chọn lên TextView hoặc lưu vào biến để sử dụng sau này
                    categoryTv.setText(selectedCategory);
                }
            });

            builder.show();
        }

        // Hàm để lấy danh sách tên loại từ ArrayList (hoặc từ Firebase)
        private String[] getCategoryNames() {
            // Chuyển đổi ArrayList categoryList thành mảng String tên loại
            String[] categoryArray = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                categoryArray[i] = categoryList.get(i).getName(); // Giả sử 'getName()' trả về tên loại
            }
            return categoryArray;
        }

        private void loadPdfCaregories() {
            categoryList = new ArrayList<>();

            //db reference to load categories ... db > Book
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BookType");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    categoryList.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        //get data
                        Book model = ds.getValue(Book.class);
                        //add to arrayList
                        categoryList.add(model);
                    }
                    // Sau khi lấy danh sách từ Firebase, hiển thị dialog để chọn thể loại
                    showDialogToSelectCategory();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý khi có lỗi
                }
            });
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