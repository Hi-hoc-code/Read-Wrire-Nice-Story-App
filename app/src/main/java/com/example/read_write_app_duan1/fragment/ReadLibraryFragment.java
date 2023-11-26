package com.example.read_write_app_duan1.fragment;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.activities.MainActivity;
import com.example.read_write_app_duan1.adapter.ReadLibraryAdapter;
import com.example.read_write_app_duan1.adapter.TypeAdapter;
import com.example.read_write_app_duan1.models.LibraryRead;
import com.example.read_write_app_duan1.models.Type;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadLibraryFragment extends Fragment {

    RecyclerView rvcType;
    DatabaseReference databaseReference;
    FirebaseStorage mStorage;
    ArrayList<LibraryRead> list;
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
        addEvents(view);

        return view;
    }

    private void addEvents(View view) {
        tvNumber = view.findViewById(R.id.tvNumberStory);
        rvcType = view.findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvcType.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        mStorage = FirebaseStorage.getInstance();

        list = new ArrayList<>();
        readLibraryAdapter = new ReadLibraryAdapter(getContext(), list);
        rvcType.setAdapter(readLibraryAdapter);
        // Tạo đối tượng DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvcType.getContext(), layoutManager.getOrientation());
        // Gán đối tượng DividerItemDecoration vào RecyclerView
        rvcType.addItemDecoration(dividerItemDecoration);
        databaseReference.orderByChild("checkBook").equalTo("2").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LibraryRead libraryRead = new LibraryRead();
                Object data = snapshot.getValue();
                if (data != null) {
                    if (data instanceof Map) {
                        Map<String, Object> mapData = (Map<String, Object>) data;
                        libraryRead.setName(String.valueOf(mapData.get("name")));
                        libraryRead.setType(String.valueOf(mapData.get("type")));
                        libraryRead.setImage(String.valueOf(mapData.get("image")));
                    }
                }

                list.add(libraryRead);
                readLibraryAdapter.notifyDataSetChanged();
                tvNumber.setText(String.valueOf(readLibraryAdapter.getItemCount()));


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