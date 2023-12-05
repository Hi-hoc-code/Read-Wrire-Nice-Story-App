package com.example.read_write_app_duan1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.HomeBookAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment{
    private DatabaseReference databaseReference;
    private ArrayList<Book> list;
    private RecyclerView rcvRecommendStory, rcvReadingBook;
    private HomeBookAdapter homeBookAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);
        addEvents(view);
        return view;
    }

    private void addEvents(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        rcvReadingBook = view.findViewById(R.id.rcvReading);
        rcvRecommendStory = view.findViewById(R.id.rcvRecommendStory);
        rcvRecommendStory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rcvReadingBook.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        list = new ArrayList<>();
        // Initialize FragmentManager
        FragmentManager fragmentManager = getParentFragmentManager();
        homeBookAdapter = new HomeBookAdapter(getContext(),list , fragmentManager);
        rcvReadingBook.setAdapter(homeBookAdapter);
        rcvRecommendStory.setAdapter(homeBookAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Book book = new Book();
                Object data = snapshot.getValue();
                if (data != null) {
                    if (data instanceof Map) {
                        Map<String, Object> mapData = (Map<String, Object>) data;
                        book.setName(String.valueOf(mapData.get("name")));
                        book.setImage(String.valueOf(mapData.get("image")));
                        book.setId(String.valueOf(mapData.get("idBook")));
                        // Set other book properties accordingly
                    }
                }
                list.add(book);
                homeBookAdapter.notifyDataSetChanged();
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
