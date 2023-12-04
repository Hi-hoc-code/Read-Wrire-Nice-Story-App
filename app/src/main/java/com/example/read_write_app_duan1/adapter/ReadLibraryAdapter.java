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

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.Book;
import com.example.read_write_app_duan1.models.LibraryRead;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ReadLibraryAdapter extends RecyclerView.Adapter<ReadLibraryAdapter.ReadLibraryViewHolder> {

    Context context;
    List<Book> mListLibraryRead;

    public ReadLibraryAdapter(Context context, List<Book> mListLibraryRead) {
        this.context = context;
        this.mListLibraryRead = mListLibraryRead;
    }

    @NonNull
    @Override
    public ReadLibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_library_read, parent, false);
        return new ReadLibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadLibraryViewHolder holder, int position) {
        Book libraryRead = mListLibraryRead.get(position);
        if (libraryRead == null) {
            return;
        }


        holder.tvNameReadLibrary.setText(libraryRead.getName());
        holder.tvTypeReadLibrary.setText(libraryRead.getType());
        holder.description.setText(libraryRead.getDiscription());
        Picasso.get().load(libraryRead.getImage()).into(holder.imgStoryReadLibrary);




    }

    @Override
    public int getItemCount() {

        if (mListLibraryRead != null) {
            return mListLibraryRead.size();
        }
        return 0;
    }
    // Trong ReadLibraryAdapter




    public class ReadLibraryViewHolder extends RecyclerView.ViewHolder {


        private ImageView imgStoryReadLibrary, imgDeleteRead;
        private TextView tvNameReadLibrary, tvTypeReadLibrary, description;

        public ReadLibraryViewHolder(@NonNull View itemView) {
            super(itemView);

            imgStoryReadLibrary = itemView.findViewById(R.id.imgStoryReadLibrary);
            imgDeleteRead = itemView.findViewById(R.id.imgDeleteRead);
            tvNameReadLibrary = itemView.findViewById(R.id.tvNameReadLibrary);
            tvTypeReadLibrary = itemView.findViewById(R.id.tvTypeReadLibrary);
            description = itemView.findViewById(R.id.decriptionReadLibrary);

            imgDeleteRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Xử lý sự kiện khi nút Delete được nhấn
                    showDeleteDialog(getAdapterPosition());
                }
            });

            imgStoryReadLibrary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                mListLibraryRead.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mListLibraryRead.size());
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
