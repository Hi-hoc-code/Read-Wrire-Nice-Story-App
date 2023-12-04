package com.example.read_write_app_duan1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.ReadLibraryAdapter;
import com.example.read_write_app_duan1.fragment.ReadLibraryFragment;
import com.example.read_write_app_duan1.fragment.SearchFragment;
import com.example.read_write_app_duan1.models.Book;
import com.example.read_write_app_duan1.models.LibraryRead;
import com.example.read_write_app_duan1.models.Type;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ReadBookActivity extends AppCompatActivity {
    ImageButton backBtn;
    ImageView imgBook;
    TextView TitleBook, descriptionRead, NameUser, comment, readBook, addFavorite;
    Context context;
    public ArrayList<Book> list;
    String bookName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        initUi();
        dataTransfer();
        addEvent();
        takeData();

    }

    private void takeData() {
        Intent intent = getIntent();
        String bookName = null;
        String bookComment = null;
        if (intent != null) {
            bookName = intent.getStringExtra("name");
            bookComment = intent.getStringExtra("comments");

        }

        // Tạo tham chiếu đến Firebase và lấy dữ liệu từ đó dựa trên tên cuốn sách
        DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("Book");
        bookRef.orderByChild("name").equalTo(bookName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                        // Lấy dữ liệu từ mỗi cuốn sách
                        String author = bookSnapshot.child("author").getValue(String.class);
                        long commentCount = bookSnapshot.child("comments").getChildrenCount();

                        // Ghi log để kiểm tra dữ liệu bạn nhận được từ Firebase
                        Log.d("FirebaseData", "Author: " + author);
                        Log.d("FirebaseData", "Comment Count: " + commentCount);

                        // Hiển thị thông tin tác giả và số lượng comment trong TextViews
                        NameUser.setText(author);
                        comment.setText(String.valueOf(commentCount));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong việc đọc dữ liệu từ Firebase
            }
        });

    }

    private void initUi() {
        TitleBook = findViewById(R.id.TitleBook);
        backBtn = findViewById(R.id.backBtn);
        imgBook = findViewById(R.id.imgBook);
        descriptionRead = findViewById(R.id.descriptionRead);
        NameUser = findViewById(R.id.NameUser);
        comment = findViewById(R.id.comment);
        readBook = findViewById(R.id.readBook);
        addFavorite = findViewById(R.id.addFavorite);
    }

    private void dataTransfer() {
        Intent intent = getIntent();

        String bookImage = null;
        String bookDescription = null;
        if (intent != null) {
            bookName = intent.getStringExtra("name");
            bookImage = intent.getStringExtra("image");
            bookDescription = intent.getStringExtra("description");

        }
        TitleBook.setText(bookName);
        Picasso.get().load(bookImage).into(imgBook);
        descriptionRead.setText(bookDescription);

    }

    private void addEvent() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ReadBookActivity.this, SearchFragment.class));

            }
        });

        //Button ReadBook
        readBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến ReadPdfActivity

// Code trong ReadBookActivity khi muốn chuyển tên sách sang ReadPdfActivity

                Intent intent = new Intent(ReadBookActivity.this, ReadPdfActivity.class);
                intent.putExtra("name", bookName);
                startActivity(intent);




            }
        });

        //Button Add Favorite
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}



