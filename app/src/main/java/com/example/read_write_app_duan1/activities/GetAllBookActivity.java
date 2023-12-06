package com.example.read_write_app_duan1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.GetAllAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetAllBookActivity extends AppCompatActivity {
    RecyclerView rcvGetAllBook;
    GetAllAdapter getAllAdapter;
    ArrayList<Book> listGetAll;
    DatabaseReference bookRef;
    EditText edtSearch;
    ImageView imgBack;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_book);
        addControls();
        addEvents();
    }

    private void addEvents() {
        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    listGetAll.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String image = dataSnapshot.child("image").getValue(String.class);
                        listGetAll.add(new Book(name, image));
                    }
                    getAllAdapter.notifyDataSetChanged();

                } catch (Exception ex) {
                    Log.d("TAG", "onDataChange: ", ex);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHomeActivity();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for the search functionality
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase();
                ArrayList<Book> filteredList = new ArrayList<>();

                for (Book data : listGetAll) {
                    if (data.getName().toLowerCase().contains(searchText)) {
                        filteredList.add(data);
                    }
                }

                getAllAdapter.setFilter(filteredList);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for the search functionality
            }
        });
    }

    private void backHomeActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void addControls() {
        rcvGetAllBook = findViewById(R.id.rcvGetAllBook);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcvGetAllBook.setLayoutManager(gridLayoutManager);
        listGetAll = new ArrayList<>();
        getAllAdapter = new GetAllAdapter(this, listGetAll,fragmentManager);
        bookRef = FirebaseDatabase.getInstance().getReference("Book");

        edtSearch = findViewById(R.id.edtSearch);
        imgBack = findViewById(R.id.btnBack);

        rcvGetAllBook.setAdapter(getAllAdapter); // Set adapter to RecyclerView
    }
}
