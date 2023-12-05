package com.example.read_write_app_duan1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.Constants;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReadPdfActivity extends AppCompatActivity {

    ImageButton backBtn;
    TextView toolbarTitleTv, toolbarSubtitleTv;
    ProgressBar progressBar;
    private String bookName;
    PDFView pdfView;
    private static final String TAG = "PDF_VIEW_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pdf);
        pdfView = findViewById(R.id.pdfView);
        backBtn = findViewById(R.id.backBtn);
        progressBar = findViewById(R.id.progressBar);
        toolbarSubtitleTv = findViewById(R.id.toolbarSubtitleTv);

        //get nameBook from intent that we passed in intent
        Intent intent = getIntent();
        if (intent != null) {
             bookName = intent.getStringExtra("name");
            Log.d(TAG, "onCreate: BookName: " + bookName);
        }


        loadBookDetails();


        //handle click, go back
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
finish();            }
        });


    }

    void loadBookDetails() {
        Log.d(TAG, "loadBookDetails: Get Pdf Url ");

        // Check if bookName is not null or empty before proceeding
//        if (bookName != null && !bookName.isEmpty()) {
        // Database Reference to get book details using book name
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Book");
        ref.orderByChild("name").equalTo(bookName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                        String pdfUrl = bookSnapshot.child("content").getValue(String.class);

                        if (pdfUrl != null && !pdfUrl.isEmpty()) {
                            // Nếu có URL của tệp PDF, sử dụng nó để hiển thị PDF
                            loadBookFromUrl(pdfUrl);
                        } else {
                            // Xử lý khi không tìm thấy URL của tệp PDF
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

        private void loadBookFromUrl(String pdfUrl) {
        Log.d(TAG,"loadBookFromUrl: Get PDF from storage");
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        reference.getBytes(Constants.MAX_BYTES_PDF)
        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //LOAD PDF USING BYTES
                pdfView.fromBytes(bytes)
                        .swipeHorizontal(false) //set false to scroll vertical, set true to swipe horizontal
                        .onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                //et current and total pages in toolbar subtitle
                                int currentPage = (page + 1);// do + 1  because page starts from 0
                                toolbarSubtitleTv.setText(currentPage + "/"+pageCount);
                                Log.d(TAG, "onPageChanged: "+currentPage + "/" +pageCount);

                            }
                        })
                        .onError(new OnErrorListener() {
                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(ReadPdfActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onPageError(new OnPageErrorListener() {
                            @Override
                            public void onPageError(int page, Throwable t) {
                                Log.d(TAG, "onPageError: "+t.getMessage());
                                Toast.makeText(ReadPdfActivity.this, "Error n page "+page+ " "+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .load();

                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                        //failed to load book
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

}