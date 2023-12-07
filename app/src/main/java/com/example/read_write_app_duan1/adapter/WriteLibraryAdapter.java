package com.example.read_write_app_duan1.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WriteLibraryAdapter extends RecyclerView.Adapter<WriteLibraryAdapter.WriteLibraryViewHolder>{
    Context context;
    List<Book> mListLibraryWrite;

    public WriteLibraryAdapter(Context context, List<Book> mListLibraryWrite) {
        this.context = context;
        this.mListLibraryWrite = mListLibraryWrite;
    }

    @NonNull
    @Override
    public WriteLibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_library_write,parent, false);
        return new WriteLibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WriteLibraryViewHolder holder, int position) {
        Book book = mListLibraryWrite.get(position);

        // Hiển thị thông tin từ đối tượng Book lên ViewHolder
        holder.tvNameWriteLibrary.setText(book.getName());
        holder.tvProgressLibrary.setText(book.getStatus());
        // Load image using Glide, Picasso, or any other library
        Glide.with(context).load(book.getImage()).into(holder.imgStoryWriteLibrary);

    }

    @Override
    public int getItemCount() {

        if (mListLibraryWrite != null){
            return mListLibraryWrite.size();
        }
        return 0;
    }

    public class WriteLibraryViewHolder extends RecyclerView.ViewHolder{
        ImageView imgStoryWriteLibrary, deleteWrite;
        TextView tvNameWriteLibrary, tvProgressLibrary;

        public WriteLibraryViewHolder(@NonNull View itemView){
            super(itemView);

            imgStoryWriteLibrary = itemView.findViewById(R.id.imgStoryWriteLibrary);
            deleteWrite = itemView.findViewById(R.id.deleteWrite);
            tvNameWriteLibrary = itemView.findViewById(R.id.tvNameWriteLibrary);
            tvProgressLibrary = itemView.findViewById(R.id.tvProgressLibrary);

            deleteWrite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(getAdapterPosition());
                }
            });
        }


    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xóa phần tử tại vị trí position
                mListLibraryWrite.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mListLibraryWrite.size());
                // Thực hiện xóa dữ liệu khỏi Firebase hoặc cơ sở dữ liệu của bạn ở đây

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
