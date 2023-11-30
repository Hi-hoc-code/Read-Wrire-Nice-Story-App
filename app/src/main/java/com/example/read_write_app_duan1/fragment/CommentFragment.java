package com.example.read_write_app_duan1.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.CommentAdapter;
import com.example.read_write_app_duan1.models.Comment;
import com.google.firebase.database.ChildEventListener;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        Bundle bundle = getArguments();
        String idBook = bundle.getString("idBook");
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Book").child(idBook).child("comments");
        rcvComment = view.findViewById(R.id.rcvComment);
        rcvComment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listComment = new ArrayList<>();
        commentAdapter = new CommentAdapter(listComment, getContext(), idBook);
        rcvComment.setAdapter(commentAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    listComment.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list comment failed", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}

