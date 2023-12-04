package com.example.read_write_app_duan1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.activities.ListStoryActivity;
import com.example.read_write_app_duan1.fragment.SearchFragment;
import com.example.read_write_app_duan1.models.Book;
import com.example.read_write_app_duan1.models.FilterType;
import com.example.read_write_app_duan1.models.Type;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.MyViewHoler> implements Filterable {
    Context context;
   public ArrayList<Type> list, filterList;
   //instance of our filter class
    private FilterType filterType;

    public TypeAdapter(Context context, ArrayList<Type> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;
    }

    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_type, parent, false);
        return new TypeAdapter.MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {
        Type type = list.get(position);
        holder.tvType.setText(type.getType());
        Picasso.get().load(type.getImage()).into(holder.imgType);

        holder.itemListType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin của item được chọn
                Type selectedType = list.get(holder.getAdapterPosition());

                // Tạo Intent để chuyển sang ListStoryActivity và gửi thông tin theo Intent
                Intent intent = new Intent(context, ListStoryActivity.class);
                intent.putExtra("type", selectedType.getType()); // Gửi thông tin khác nếu cần
                // Khởi chạy ListStoryActivity
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    @Override
    public Filter getFilter() {
        if (filterType == null){
            filterType = new FilterType(filterList,this);
        }
        return filterType;
    }

    static class MyViewHoler extends RecyclerView.ViewHolder{
        TextView tvType;
        ImageView imgType;
        LinearLayout itemListType;
        public MyViewHoler(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            imgType = itemView.findViewById(R.id.imgType);
            itemListType = itemView.findViewById(R.id.itemListType);
        }
    }

}
