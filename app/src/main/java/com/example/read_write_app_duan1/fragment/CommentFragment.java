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
import com.example.read_write_app_duan1.adapter.CommentAdapter;
import com.example.read_write_app_duan1.models.Comment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommentFragment extends Fragment {
    RecyclerView rcvComment;
    CommentAdapter commentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        String bookId = getArguments().getString("bookId");
        if (bookId != null) {
            rcvComment = view.findViewById(R.id.rcvComment);
            rcvComment.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvComment.setAdapter(commentAdapter);
            // Use bookId to construct Firebase database reference
            DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference()
                    .child("Book")
                    .child(bookId)  // Ensure bookId is initialized
                    .child("description");

            // Create FirebaseRecyclerOptions and pass it to the adapter
            FirebaseRecyclerOptions<Comment> options = new FirebaseRecyclerOptions.Builder<Comment>()
                    .setQuery(commentsRef, Comment.class)
                    .build();
            // Use the book ID to load comments from Firebase
            commentAdapter = new CommentAdapter(options);
            rcvComment.setAdapter(commentAdapter);
        }
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
