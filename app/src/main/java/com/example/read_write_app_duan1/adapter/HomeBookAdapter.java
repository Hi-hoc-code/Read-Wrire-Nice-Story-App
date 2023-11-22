package com.example.read_write_app_duan1.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.OnItemClickListener;
import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.fragment.CommentFragment;
import com.example.read_write_app_duan1.models.Book;
import com.example.read_write_app_duan1.models.Comment;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.MyViewHoler> {
    Context context;
    ArrayList<Book> list;


    public HomeBookAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recommend_story, parent, false);
        return new MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {
        Book book1 = list.get(position);
        holder.tvNameStory.setText(book1.getName());
        Picasso.get().load(book1.getImage()).into(holder.imgStory);

        holder.tvNameStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentFragment(book1.getId());
            }
        });
        holder.imgStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentFragment(book1.getId());
            }
        });
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
    private void showCommentFragment(String bookId) {
        Fragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bookId", bookId);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

}
