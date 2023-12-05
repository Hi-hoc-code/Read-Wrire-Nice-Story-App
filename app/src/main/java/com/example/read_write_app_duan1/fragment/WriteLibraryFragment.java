package com.example.read_write_app_duan1.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.activities.ListStoryActivity;
import com.example.read_write_app_duan1.adapter.ListTypeAdapter;
import com.example.read_write_app_duan1.adapter.TypeAdapter;
import com.example.read_write_app_duan1.adapter.WriteLibraryAdapter;
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

public class WriteLibraryFragment extends Fragment {
    RecyclerView rvcType;
    DatabaseReference databaseReference;
    FirebaseStorage mStorage;
    ArrayList<Book> list;
    TextView Number;

    WriteLibraryAdapter adapter;
    public WriteLibraryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_library, container, false);
        addEvents(view);
        return view;

    }

    private void addEvents(View view) {
        rvcType = view.findViewById(R.id.recycleViewWrite);
        Number = view.findViewById(R.id.tvNumberStory);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        rvcType.setLayoutManager(gridLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        mStorage = FirebaseStorage.getInstance();
        list = new ArrayList<>();
        adapter = new WriteLibraryAdapter(getContext(), list);
        rvcType.setAdapter(adapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Book type = new Book();
                Object data = snapshot.getValue();
                if (data != null) {
                    if (data instanceof Map) {
                        Map<String, Object> mapData = (Map<String, Object>) data;
                        type.setName(String.valueOf(mapData.get("name")));
                        type.setStatus(String.valueOf(mapData.get("status")));
                        type.setImage(String.valueOf(mapData.get("image")));
                    }
                }
                list.add(type);
                //thông báo
                adapter.notifyDataSetChanged();
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
        Number.setText(String.valueOf(numberOfItems));
    }
}



