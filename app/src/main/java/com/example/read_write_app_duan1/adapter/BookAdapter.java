package com.example.read_write_app_duan1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.Book;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Book> list;

    public BookAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_view_book, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book1 = list.get(position);
        holder.tvNameStory.setText(book1.getName());
        Picasso.get().load(book1.getImage()).into(holder.imgStory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameStory;
        ShapeableImageView imgStory;

        public MyViewHolder(View view) {
            super(view);
            tvNameStory = view.findViewById(R.id.tvNameStory);
            imgStory = view.findViewById(R.id.imgRecommendStory);
        }
    }
}