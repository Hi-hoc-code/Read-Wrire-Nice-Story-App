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
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("name")) {
           String  bookName = bundle.getString("name");
            Log.d("ReadLibraryFragment", "Received book name: " + bookName);

            // Sử dụng bookName ở đây để thực hiện các thao tác cần thiết
            // Ví dụ: Gọi hàm lấy dữ liệu từ Firebase
            getDataFromFireBare(bookName);
        }



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

    private void getDataFromFireBare(String bookName){
        databaseReference.orderByChild("name").equalTo(bookName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Lấy dữ liệu từ dataSnapshot và thêm vào list hoặc adapter của RecyclerView
                        Book book = snapshot.getValue(Book.class);
                        if (book != null) {
                            // Thêm dữ liệu vào list hoặc adapter của RecyclerView
                            bookList.add(book);
                        }
                    }
                    // Sau khi có dữ liệu, cập nhật RecyclerView
                    readLibraryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); {

        }
        }

}
