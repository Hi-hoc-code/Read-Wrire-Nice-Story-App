package com.example.read_write_app_duan1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.CommentAdapter;
import com.example.read_write_app_duan1.models.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentFragment extends Fragment {
    private RecyclerView rcvComment;
    private CommentAdapter commentAdapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<Comment> listComment;
    EditText edtComment;
    ImageButton btnComment;

    //    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_comment, container, false);
//
//        Bundle bundle = getArguments();
//        String idBook = bundle.getString("idBook");
////        database = FirebaseDatabase.getInstance();
//        reference = FirebaseDatabase.getInstance().getReference("Book").child(idBook).child("comments");
//        rcvComment = view.findViewById(R.id.rcvComment);
//        rcvComment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        listComment = new ArrayList<>();
//        commentAdapter = new CommentAdapter(listComment, getContext(), idBook);
//        rcvComment.setAdapter(commentAdapter);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    Comment comment = dataSnapshot.getValue(Comment.class);
//                    listComment.add(comment);
//                }
//                commentAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "Get list comment failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return view;
//    }
//}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String idBook = bundle.getString("idBook");
            if (idBook != null && !idBook.isEmpty()) {
                reference = FirebaseDatabase.getInstance().getReference("Book").child(idBook).child("comments");
                rcvComment = view.findViewById(R.id.rcvComment);
                edtComment = view.findViewById(R.id.edtComment);
                btnComment = view.findViewById(R.id.btnComment);

                btnComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = edtComment.getText().toString();

                    }
                });


                rcvComment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                listComment = new ArrayList<>();
                commentAdapter = new CommentAdapter(listComment, getContext(), idBook);
                rcvComment.setAdapter(commentAdapter);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listComment.clear(); // Clear the list before adding new data
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Comment comment = dataSnapshot.getValue(Comment.class);
                            if (comment != null) {
                                listComment.add(comment);
                            }
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Get list comment failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Handle the case where idBook is null or empty
                Toast.makeText(getContext(), "Invalid book ID", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the bundle is null
            Toast.makeText(getContext(), "Bundle is null", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}