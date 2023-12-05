package com.example.read_write_app_duan1.adapter;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.fragment.CommentFragment;
import com.example.read_write_app_duan1.models.Comment;
import com.example.read_write_app_duan1.models.Users;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> listComment;
    private Context context;
    private DatabaseReference mAuthor;
    private DatabaseReference mComment;
    private String idBook;
    private String author;

    public CommentAdapter(ArrayList<Comment> listComment, Context context, String idBook) {
        this.listComment = listComment;
        this.context = context;
        this.idBook = idBook;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = listComment.get(position);
        holder.tvUsernameComment.setText(comment.getAuthor());
        holder.tvComment.setText(comment.getText());

        holder.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.edtComment.setFocusableInTouchMode(true);
                holder.edtComment.requestFocus();
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(holder.edtComment, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }
    private void writeNewComment(String author, String text, String uid) {
        String key = mComment.push().getKey();
        Comment comment = new Comment(author, text, uid);
        Map<String, Object> commentValues = comment.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, commentValues);
        mComment.updateChildren(childUpdates);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsernameComment, tvComment, tvReply;
        private EditText edtComment;
        private ShapeableImageView shapeableImageView;
        private ImageButton btnComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsernameComment = itemView.findViewById(R.id.tvNameUserComment);
            tvComment = itemView.findViewById(R.id.tvComment);
            shapeableImageView = itemView.findViewById(R.id.imgAvatarUser);
            tvReply = itemView.findViewById(R.id.tvReply);
            edtComment = itemView.findViewById(R.id.edtComment);
            btnComment = itemView.findViewById(R.id.btnComment);
        }
    }
}
