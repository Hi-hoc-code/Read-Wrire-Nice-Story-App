package com.example.read_write_app_duan1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.CommentAdapter;
import com.example.read_write_app_duan1.models.Comment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CommentFragment extends Fragment {
    RecyclerView rcvComment;
    CommentAdapter commentAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        rcvComment = view.findViewById(R.id.rcvComment);
        FirebaseRecyclerOptions<Comment> options =
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("items"), Comment.class)
                        .build();

        commentAdapter = new CommentAdapter(options);

        rcvComment.setAdapter(commentAdapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        commentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        commentAdapter.stopListening();
    }
}
