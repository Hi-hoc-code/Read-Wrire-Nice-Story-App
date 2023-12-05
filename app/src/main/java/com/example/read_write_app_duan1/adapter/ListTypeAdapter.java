package com.example.read_write_app_duan1.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListTypeAdapter extends RecyclerView.Adapter<ListTypeAdapter.MyViewHolder> {

    Context context;
    public ArrayList<Book> list;

    public ListTypeAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_story, parent, false);
        return new ListTypeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //data progressing
        //Display data on the interface
        Book book = list.get(position);
        holder.order.setText(book.getId());
        Picasso.get().load(book.getImage()).into(holder.imgList);
        holder.tvName.setText(book.getName());
        holder.descriptionList.setText(book.getDiscription());

        holder.itemliststory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
    TextView order, tvName, descriptionList;
    ImageView imgList;
    LinearLayout itemliststory;



    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        order = itemView.findViewById(R.id.oder);
        tvName = itemView.findViewById(R.id.tvName);
        descriptionList = itemView.findViewById(R.id.decriptionList);
        imgList = itemView.findViewById(R.id.imgList);
        itemliststory = itemView.findViewById(R.id.itemliststory);
    }
}
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
