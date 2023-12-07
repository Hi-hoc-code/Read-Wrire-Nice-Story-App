package com.example.read_write_app_duan1.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.ReadLibraryAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class ReadLibraryFragment extends Fragment {

    private static final int YOUR_REQUEST_CODE = 1;
    RecyclerView rvcType;
    DatabaseReference databaseReference;
    FirebaseStorage mStorage;
    ArrayList<Book> bookList;
    ReadLibraryAdapter readLibraryAdapter;
    TextView tvNumber;
    FrameLayout frameLayoutRead;
    LinearLayout layout;

    public ReadLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_library, container, false);
        unitUi(view);
        // Nhận dữ liệu từ Intent gửi từ Activity
            getDataFromFireBare();




        return view;
    }

    private void unitUi(View view){
        tvNumber = view.findViewById(R.id.tvNumberStory);
        rvcType = view.findViewById(R.id.recycleViewRead);

        rvcType.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference("Book");

        mStorage = FirebaseStorage.getInstance();

        bookList = new ArrayList<>();

        readLibraryAdapter = new ReadLibraryAdapter(getContext(),bookList);
        rvcType.setAdapter(readLibraryAdapter);
    }

    private void getDataFromFireBare() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        databaseReference.orderByChild("read").equalTo("0").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Lấy dữ liệu từ dataSnapshot và thêm vào list hoặc adapter của RecyclerView
                Book book = dataSnapshot.getValue(Book.class);
                if (book != null) {
                    // Thêm dữ liệu vào list hoặc adapter của RecyclerView
                    bookList.add(book);
                    // Cập nhật RecyclerView sau khi thêm dữ liệu mới
                    readLibraryAdapter.notifyDataSetChanged();
                }
            }

            // Implement other methods of ChildEventListener if necessary
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

}
