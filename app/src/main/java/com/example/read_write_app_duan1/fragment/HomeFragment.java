package com.example.read_write_app_duan1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.IsReadingBookAdapter;
import com.example.read_write_app_duan1.adapter.RecommendStoryAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {
    DatabaseReference databaseReference;
    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;
    ArrayList<Book> list;
    RecyclerView rcvRecommendStory, rcvReadingBook;
    RecommendStoryAdapter recommendStoryAdapter;
    IsReadingBookAdapter readingBookAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);
        addEvents(view);
        return view;
    }

    private void addEvents(View view) {
        mDatabase=FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        mStorage = FirebaseStorage.getInstance();
        rcvReadingBook = view.findViewById(R.id.rcvReading);
        rcvRecommendStory = view.findViewById(R.id.rcvRecommendStory);
        rcvRecommendStory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rcvReadingBook.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        list = new ArrayList<>();
        readingBookAdapter = new IsReadingBookAdapter(getContext(),list);
        rcvReadingBook.setAdapter(readingBookAdapter);
        recommendStoryAdapter = new RecommendStoryAdapter(getContext(),list);
        rcvRecommendStory.setAdapter(recommendStoryAdapter);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Book book1 = new Book();
                Object data = snapshot.getValue();
                if (data != null) {
                    if (data instanceof Map) {
                        Map<String, Object> mapData = (Map<String, Object>) data;
                        book1.setName(String.valueOf(mapData.get("name")));
                        book1.setImage(String.valueOf(mapData.get("image")));
                        String chapterContent= "";
                        if(snapshot.child("chapter").child("chapter1").exists()){
                            chapterContent = snapshot.child("chapter").child("chapter1").child("content").getValue(String.class);
                        }
                        book1.setChapter(chapterContent);
                        // Set other book properties accordingly
                    }
                }
                list.add(book1);
                recommendStoryAdapter.notifyDataSetChanged();
                readingBookAdapter.notifyDataSetChanged();
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
