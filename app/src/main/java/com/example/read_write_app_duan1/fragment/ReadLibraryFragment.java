package com.example.read_write_app_duan1.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.ReadLibraryAdapter;
import com.example.read_write_app_duan1.adapter.TypeAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.example.read_write_app_duan1.models.Type;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadLibraryFragment extends Fragment {

    RecyclerView rvcType;
    DatabaseReference databaseReference;
    FirebaseStorage mStorage;
    ArrayList<Book> bookList;
    ReadLibraryAdapter readLibraryAdapter;
    TextView tvNumber;

    public ReadLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_library, container, false);
        rvcType = view.findViewById(R.id.recycleView);
        if (getArguments() != null) {
            String bookName = getArguments().getString("name");

            // Lấy dữ liệu từ Firebase dựa trên tên sách
         addEvents(view);
        }
        return view;
    }

    private void addEvents(View view) {

        rvcType  = view.findViewById(R.id.rvcType);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvcType.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        mStorage = FirebaseStorage.getInstance();
        bookList = new ArrayList<>();
        readLibraryAdapter = new ReadLibraryAdapter(getContext(),bookList);
        rvcType.setAdapter(readLibraryAdapter);

        databaseReference.orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Book book = new Book();
                Object data = snapshot.getValue();
                if (data != null) {
                    if (data instanceof Map) {
                        Map<String, Object> mapData = (Map<String, Object>) data;
                        book.setType(String.valueOf(mapData.get("type")));
                        book.setImage(String.valueOf(mapData.get("image")));
                    }
                }
                bookList.add(book);
                //thông báo
                readLibraryAdapter.notifyDataSetChanged();
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

