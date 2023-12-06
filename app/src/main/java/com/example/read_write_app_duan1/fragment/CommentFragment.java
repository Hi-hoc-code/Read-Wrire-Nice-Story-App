package com.example.read_write_app_duan1.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.CommentAdapter;
import com.example.read_write_app_duan1.models.Comment;
import com.example.read_write_app_duan1.models.Users;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentFragment extends Fragment {
    private RecyclerView rcvComment;
    private ImageView btnComment;
    private EditText edtComment;
    private CommentAdapter commentAdapter;
    private DatabaseReference mAuthor;
    private DatabaseReference reference;
    String author;
    private ArrayList<Comment> listComment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        Bundle bundle = getArguments();
        String idBook = bundle.getString("idBook");
        reference = FirebaseDatabase.getInstance().getReference("Book").child(idBook).child("comments");
        rcvComment = view.findViewById(R.id.rcvComment);
        btnComment = view.findViewById(R.id.btnComment);
        edtComment = view.findViewById(R.id.edtComment);
        rcvComment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listComment = new ArrayList<>();
        commentAdapter = new CommentAdapter(listComment, getContext(), idBook);
        rcvComment.setAdapter(commentAdapter);
        String uid = "Hf2LyJAymEN3HclrCgzneMuSyoa2";
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edtComment.getText().toString();
                String author = "ABC";
                writeNewComment(author, text, uid);
                commentAdapter.notifyDataSetChanged();
                edtComment.getText().clear();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment.clear();
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
    private void writeNewComment(String author, String text, String uid) {
        String key = reference.push().getKey();
        Comment comment = new Comment(author, text, uid);
        Map<String, Object> commentValues = comment.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, commentValues);
        reference.updateChildren(childUpdates);
    }
}

