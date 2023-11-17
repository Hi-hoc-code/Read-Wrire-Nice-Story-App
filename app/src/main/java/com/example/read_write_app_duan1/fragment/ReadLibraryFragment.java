package com.example.read_write_app_duan1.fragment;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.activities.MainActivity;
import com.example.read_write_app_duan1.adapter.ReadLibraryAdapter;
import com.example.read_write_app_duan1.models.LibraryRead;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadLibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReadLibraryAdapter readLibraryAdapter;
    private List<LibraryRead> list;
    private ImageView imageView;
    private TextView tvYes, tvHuy;

    public ReadLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_library, container, false);


        imageView = view.findViewById(R.id.imgDeleteRead);
//        recyclerView = view.findViewById(R.id.recycleView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//
//
//        //phân cách
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(dividerItemDecoration);
//
//        getListFromRealtimeDatabase();

        return view;
    }

    private void getListFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Book");
        list = new ArrayList<>();
        readLibraryAdapter = new ReadLibraryAdapter(getContext(), list);
        recyclerView.setAdapter(readLibraryAdapter);

        //cách 1
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LibraryRead libraryRead = dataSnapshot.getValue(LibraryRead.class);
                    list.add(libraryRead);
                }

                readLibraryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //lỗi thông báo
                String message = "This is a message";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View view2 = inflater.inflate(R.layout.dialog_delete, null);
                tvHuy = view.findViewById(R.id.tvHuy);
                tvYes = view.findViewById(R.id.tvYes);
                builder.setView(view);

                //show
                AlertDialog alertDialog = builder.create();
                // sửa bo góc tran
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                //khi nào nhấn nút mới out dialog
                alertDialog.setCancelable(false);
                alertDialog.show();

                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });

                //nút hủy
                tvHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

//        //Cách 2
//        mRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                LibraryRead libraryRead = snapshot.getValue(LibraryRead.class);
//                if (libraryRead != null){
//                    list.add(libraryRead);
//                    readLibraryAdapter.notifyDataSetChanged();
//                }
//
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//  }

    }
}