package com.example.read_write_app_duan1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.Comment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CommentAdapter extends FirebaseRecyclerAdapter<Comment, CommentAdapter.ViewHolder> {
    public CommentAdapter(@NonNull FirebaseRecyclerOptions<Comment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment model) {
        holder.setName(model.getName());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvReply, tvUsernameComment;

        public ViewHolder(View view) {
            super(view);
            tvReply = view.findViewById(R.id.tvReply);
            tvUsernameComment = view.findViewById(R.id.tvNameUserComment);
        }

        public void setName(String name) {
            tvUsernameComment.setText(name);
        }
    }
}
