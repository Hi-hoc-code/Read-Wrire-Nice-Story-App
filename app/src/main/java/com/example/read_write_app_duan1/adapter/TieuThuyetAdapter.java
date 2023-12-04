package com.example.read_write_app_duan1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.Book;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TieuThuyetAdapter extends RecyclerView.Adapter<TieuThuyetAdapter.MyViewHoler> {
    Context context;
    ArrayList<Book> list;

    public TieuThuyetAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TieuThuyetAdapter.MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_view_book, parent, false);
        return new TieuThuyetAdapter.MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TieuThuyetAdapter.MyViewHoler holder, int position) {
        Book book = list.get(position);
        holder.tvNameStory.setText(book.getName());
        Picasso.get().load(book.getImage()).into(holder.imgStory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHoler extends RecyclerView.ViewHolder{
        TextView tvNameStory;
        ShapeableImageView imgStory;
        public MyViewHoler(View view){
            super(view);
            tvNameStory = view.findViewById(R.id.tvNameStory);
            imgStory = view.findViewById(R.id.imgRecommendStory);
        }
    }
}