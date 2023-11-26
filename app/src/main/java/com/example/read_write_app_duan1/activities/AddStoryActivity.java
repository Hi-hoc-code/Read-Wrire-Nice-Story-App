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

import java.util.ArrayList;
import java.util.HashMap;

public class AddStoryActivity extends AppCompatActivity {
    private TextView tvSkip, categoryTv;
    private ImageView imgPrev, imgAddCover, imgUpload;
    private EditText edtTitle, edtDescrice;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("image");
    StorageReference reference = FirebaseStorage.getInstance().getReference();

    //progress dialog
    private ProgressDialog progressDialog;
    // arrayList to hold pdf categories
    private ArrayList<Book> categoryList;
    private Uri pdfUri;
    private static final int PDF_PICK_CODE = 1000;
    //TAG for debugging;
    private static final String TAG = "ADD_PDF_TAG";


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
                validateData();
                if (pdfUri != null){
                    uploadToFirebase(pdfUri);
                }else {
                    Toast.makeText(AddStoryActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //handle click, attach pdf
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfPickIntent();
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
                uploadImage();
            }
        });
    }

    private void uploadToFirebase(Uri uri){

        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Book book = new Book( uri.toString());
                        String bookId = root.push().getKey();
                        root.child(bookId).setValue(book);
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(AddStoryActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        imgAddCover.setImageResource(R.drawable.addcover);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AddStoryActivity.this, "Uploading Failed !", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    private String title = "", description = "", category = "";
    private void validateData(){
        //Step 1: Validate data
        Log.d(TAG, "validateDate: validating data...");

        //get data
        title = edtTitle.getText().toString().trim();
        description = edtDescrice.getText().toString().trim();
        category = categoryTv.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(title)){
            Toast.makeText(this, "Enter Title...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter Description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter Category...", Toast.LENGTH_SHORT).show();
        }
        else if (pdfUri ==null){
            Toast.makeText(this, "Pick Pdf...", Toast.LENGTH_SHORT).show();
        }
        else {
            //all data is valid, can upload now
            uploadPdfToStorage();
        }
    }

    private void uploadPdfToStorage() {
        //Step 2: Upload Pdf to firebase storage
        Log.d(TAG, "uploadPdfToStorage: uploading to storage...");

        //show progress
        progressDialog.setMessage("Uploading Pdf...");
        progressDialog.show();

        //timestamp
        long timestamp = System.currentTimeMillis();

        //path of pdf in firebase storage
        String filePathAndName = "Books/" +timestamp;
        //storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: PDF uploaded to storage... ");
                        Log.d(TAG, "onSuccess: getting pdf url");

                        //GET PDF URL
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadedPdfUrl = ""+uriTask.getResult();

                        //upload to firebase db
                        uploadPdfInfoToDb(uploadedPdfUrl, timestamp);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: PDF upload failed due to "+e.getMessage());
                        Toast.makeText(AddStoryActivity.this,"PDF upload failed due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPdfInfoToDb(String uploadedPdfUrl, long timestamp) {
        //Step 3: Upload Pdf info to firebase db
        Log.d(TAG, "uploadPdfToStorage: uploading Pdf info to firebase db...");

        progressDialog.setMessage("Uploading pdf info...");

        String uid = firebaseAuth.getUid();

        //setup data to upload
        HashMap<String, String > hashMap = new HashMap<>();
        hashMap.put("uid", ""+uid);
        hashMap.put("idBook", ""+timestamp);
        hashMap.put("title", ""+title);
        hashMap.put("description", ""+description);
        hashMap.put("type", ""+category);
        hashMap.put("url", ""+uploadedPdfUrl);
//        hashMap.put("timestamp", String.valueOf(timestamp));

        //db reference: BD > Books
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Book");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onSuccess: Successfully uploaded...");
                        Toast.makeText(AddStoryActivity.this,"Successfully upload...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: Failed to upload to db due to "+e.getMessage());
                        Toast.makeText(AddStoryActivity.this, "Failed to upload to db due to"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void pdfPickIntent(){
        Log.d(TAG, "pdfPickIntent: starting pdf pick intent");

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_PICK_CODE);
    }

    private void categoryPickDiaLog(){
        Log.d(TAG, "categoryPickDialog: showing category pick dialog");

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

                        Log.d(TAG, "onClick: Selected Category: "+category);
                    }
                })
                .show();
    }
    private void loadPdfCaregories() {
        Log.d(TAG, "loadPdtCategories: Loading pdf categories...");
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

                    Log.d(TAG, "onDataChange: "+model.getType());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==2 && resultCode == RESULT_OK && data != null){
            if (requestCode == PDF_PICK_CODE){
                Log.d(TAG, "onActivityResult: PDF Picked");

                pdfUri = data.getData();
                imgAddCover.setImageURI(pdfUri);

                Log.d(TAG,"onActivityResult: URI: "+pdfUri);
            }
        }else {
            Log.d(TAG, "onActivityResult: cancelled picking pdf");
            Toast.makeText(this, "cancelled picking pdf", Toast.LENGTH_SHORT).show();
        }

       /* //upload Image
        if (requestCode==2 && resultCode == RESULT_OK && data != null){

        }*/
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