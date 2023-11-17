package com.example.read_write_app_duan1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.LibraryRead;

import java.util.ArrayList;
import java.util.List;


public class ReadLibraryAdapter extends RecyclerView.Adapter<ReadLibraryAdapter.ReadLibraryViewHolder> {


    private List<LibraryRead> mListLibraryRead;

    public ReadLibraryAdapter(List<LibraryRead> mListLibraryRead) {
        this.mListLibraryRead = mListLibraryRead;
    }

    public ReadLibraryAdapter(Context context, List<LibraryRead> list) {
    }

    @NonNull
    @Override
    public ReadLibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_library_read, parent, false);
        return new ReadLibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadLibraryViewHolder holder, int position) {
        LibraryRead libraryRead = mListLibraryRead.get(position);
        if (libraryRead == null) {
            return;
        }
//        holder.imgStoryReadLibrary.setImageResource(libraryRead.getImgStoryReadLibrary());

        holder.tvNameReadLibrary.setText(libraryRead.getName());
        holder.tvTypeReadLibrary.setText(libraryRead.getType());
        holder.tvChapterReadLibrary.setText(libraryRead.getChapter());
//        holder.imgDeleteRead.setImageResource(libraryRead.getImgDeleteRead());


    }

    @Override
    public int getItemCount() {
        if (mListLibraryRead != null) {
            return mListLibraryRead.size();
        }
        return 0;
    }

    public class ReadLibraryViewHolder extends RecyclerView.ViewHolder {


        private ImageView imgStoryReadLibrary, imgDeleteRead;
        private TextView tvNameReadLibrary, tvTypeReadLibrary, tvChapterReadLibrary;

        public ReadLibraryViewHolder(@NonNull View itemView) {
            super(itemView);

            imgStoryReadLibrary = itemView.findViewById(R.id.imgStoryReadLibrary);
            imgDeleteRead = itemView.findViewById(R.id.imgDeleteRead);
            tvNameReadLibrary = itemView.findViewById(R.id.tvNameReadLibrary);
            tvTypeReadLibrary = itemView.findViewById(R.id.tvTypeReadLibrary);
            tvChapterReadLibrary = itemView.findViewById(R.id.tvChapterReadLibrary);




        }
    }


}
