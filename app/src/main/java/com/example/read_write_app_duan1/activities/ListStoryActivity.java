package com.example.read_write_app_duan1.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.ListTypeAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class ListStoryActivity extends AppCompatActivity {

    RecyclerView rcvView;
    TextView titleTypeList, numberStory, categoryTv;
    ImageButton prevBack;
    //specific location
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    ArrayList<Book> list;
    ListTypeAdapter listTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_story);

        intUi();
        TypeListName();
        buttonBack();

        listTypeAdapter.setOnItemClickListener(new ListTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Book clickedBook = list.get(position); // Lấy ra sách đã click
                Intent intent = new Intent(ListStoryActivity.this, ReadBookActivity.class);
                // Truyền dữ liệu của sách đã click tới ReadBookActivity
                intent.putExtra("name", clickedBook.getName());
                intent.putExtra("image", clickedBook.getImage());
                intent.putExtra("description", clickedBook.getDiscription());
                startActivity(intent);
            }
        });

    }
    private void buttonBack() {
        prevBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private void TypeListName() {
        Intent intent = getIntent();
        String typeName = null;
        if (intent != null) {
            typeName = intent.getStringExtra("type");
        }
        if (typeName != null && !typeName.isEmpty()) {
            titleTypeList.setText(typeName);
            getdataFromFireBase(typeName); // Call getdataFromFireBase with the category name
        }
    }

    private void intUi() {
        titleTypeList = findViewById(R.id.titleTypeList);
        prevBack = findViewById(R.id.prevList);
        numberStory = findViewById(R.id.numberStory);
        categoryTv = findViewById(R.id.categoryTv);
        rcvView = findViewById(R.id.recycleList);

        //set layout
        rcvView.setLayoutManager(new LinearLayoutManager(this));

        //connect Realtime database
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        //connect Storage to get photos
        storage = FirebaseStorage.getInstance();
        // to dump data into the list
        list = new ArrayList<>();
        //get the adapter of the data item and add it to the list
        listTypeAdapter = new ListTypeAdapter(ListStoryActivity.this,list);
        rcvView.setAdapter(listTypeAdapter);



    }


    //used to update RecyclerView
    private void getdataFromFireBase(final String selectedCategory){
        databaseReference.orderByChild("type").equalTo(selectedCategory).addChildEventListener(new ChildEventListener() {
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

                // update quantity
                updateNumberOfItems();
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
    private void updateNumberOfItems() {
        int numberOfItems = list.size();
        numberStory.setText(String.valueOf(numberOfItems));
    }
}