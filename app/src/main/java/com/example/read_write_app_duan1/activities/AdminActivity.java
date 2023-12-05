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

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.ListTypeAdapter;
import com.example.read_write_app_duan1.fragment.LibraryFragment;
import com.example.read_write_app_duan1.models.Book;
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
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");


        }
        intUi();
    }

    private void intUi() {
        recyclerView = findViewById(R.id.recyView);
        back = findViewById(R.id.back);

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
                startActivity(new Intent(AdminActivity.this, LibraryFragment.class));
            }
        });

        getdataFromFireBase();

    }


    //used to update RecyclerView
    private void getdataFromFireBase(){
        databaseReference.orderByChild("checkBook").equalTo(0).addChildEventListener(new ChildEventListener() {
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