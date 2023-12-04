package com.example.read_write_app_duan1.adapter;

import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.models.SlideShow;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewholder>{
    private final List<SlideShow> mListSlide;

    public SlideAdapter(List<SlideShow> mListSlide) {
        this.mListSlide = mListSlide;
    }

    @NonNull
    @Override
    public SlideViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideshow,parent,false);
        return new SlideViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewholder holder, int position) {
       SlideShow slide = mListSlide.get(position);
        if(slide == null){
            return;
        }
        holder.imgSlide.setImageResource(slide.getResourceId());

    }

    @Override
    public int getItemCount() {
        if (mListSlide != null){
            return  mListSlide.size();
        }
        return mListSlide.size();
    }

    public static class SlideViewholder extends RecyclerView.ViewHolder{
         private final ImageView imgSlide;
        public SlideViewholder(@NonNull View itemView) {
            super(itemView);
           imgSlide = itemView.findViewById(R.id.imgSlide);

        }
    }
}
