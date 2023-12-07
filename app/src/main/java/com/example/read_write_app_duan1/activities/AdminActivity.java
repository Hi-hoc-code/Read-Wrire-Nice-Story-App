package com.example.read_write_app_duan1.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.ListTypeAdapter;
import com.example.read_write_app_duan1.fragment.HomeFragment;
import com.example.read_write_app_duan1.fragment.LibraryFragment;
import com.example.read_write_app_duan1.models.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    ArrayList<Book> list;
    ListTypeAdapter listTypeAdapter;
    RecyclerView recyclerView;
    TextView back, backHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        intUi();
    }

    private void intUi() {
        recyclerView = findViewById(R.id.recyView);
        back = findViewById(R.id.back);
        backHome = findViewById(R.id.BackHome);

        //set layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //connect Realtime database
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        //connect Storage to get photos
        storage = FirebaseStorage.getInstance();
        // to dump data into the list
        list = new ArrayList<>();
        //get the adapter of the data item and add it to the list
        listTypeAdapter = new ListTypeAdapter(AdminActivity.this,list);
        recyclerView.setAdapter(listTypeAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCheckBookToZero();
            }
        });

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, HomeFragment.class));
            }
        });


        getdataFromFireBase();

    }

    private void updateCheckBookToZero() {
        // Xác định vị trí cần cập nhật checkBook, ví dụ: ở vị trí đầu tiên trong danh sách list
        int positionToUpdate = 0; // Đây là ví dụ, bạn cần xác định vị trí cụ thể

        // Kiểm tra xem vị trí có hợp lệ không và nếu có, cập nhật checkBook thành 0 và đẩy lên Firebase
        if (positionToUpdate >= 0 && positionToUpdate < list.size()) {
            String bookIdToUpdate = list.get(positionToUpdate).getId();
            DatabaseReference bookRefToUpdate = FirebaseDatabase.getInstance().getReference().child("Book").child(bookIdToUpdate);

            // Cập nhật giá trị checkBook thành 0
            bookRefToUpdate.child("checkBook").setValue(0)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Cập nhật thành công
                            // Refresh RecyclerView nếu cần thiết
                            listTypeAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xử lý khi cập nhật thất bại
                        }
                    });
        }
    }
    //used to update RecyclerView
    private void getdataFromFireBase(){
        databaseReference.orderByChild("checkBook").equalTo("2").addChildEventListener(new ChildEventListener() {
            //con
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//               //delete old data in list
//                list.clear();

                //object that stores data of snapshot
                Book book = new Book();
                //return object
                Object data = snapshot.getValue();
                if (data != null){
                    if (data instanceof Map){
                        //use Map because  in firebase each child node usually has a key-value structure
                        Map<String, Object> mapData = (Map<String, Object>) data;
                        book.setId(String.valueOf(mapData.get("id")));
                        book.setName(String.valueOf(mapData.get("name")));
                        book.setDiscription(String.valueOf(mapData.get("description")));
                        book.setImage(String.valueOf(mapData.get("image")));
                    }
                }
                list.add(book);
                listTypeAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}