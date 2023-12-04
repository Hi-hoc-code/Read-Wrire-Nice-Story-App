package com.example.read_write_app_duan1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.SlideAdapter;
import com.example.read_write_app_duan1.models.SlideShow;
import com.google.firebase.storage.OnPausedListener;

import java.util.ArrayList;
import java.util.List;

public class SlideShowActivity extends AppCompatActivity {
     private ViewPager2 mViewPager2;
     Button btnDangNhapSl;
     TextView tvDangkySl;
     private List<SlideShow> mListSlideshow;
     private Handler handler = new Handler(Looper.getMainLooper());
     private Runnable runnable = new Runnable() {
         @Override
         public void run() {
             int currentPosition = mViewPager2.getCurrentItem();
             if (currentPosition == mListSlideshow.size() - 1){
                 mViewPager2.setCurrentItem(0);
             }else{
                 mViewPager2.setCurrentItem(currentPosition + 1);
             }
         }
     };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        mViewPager2 = findViewById(R.id.view_pager_2);
        btnDangNhapSl = findViewById(R.id.btnRegisterSlide);
        tvDangkySl = findViewById(R.id.tvDangnhapSl);

        btnDangNhapSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SlideShowActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        tvDangkySl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SlideShowActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //setting viewpager2
        mViewPager2.setOffscreenPageLimit(3);
        mViewPager2.setClipToPadding(false);
        mViewPager2.setClipChildren(false);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float  r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        mViewPager2.setPageTransformer(compositePageTransformer);

        mListSlideshow = getListSlideShow();
        SlideAdapter slideAdapter = new SlideAdapter(mListSlideshow);
        mViewPager2.setAdapter(slideAdapter);
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3000);

            }
        });
    }
    private List<SlideShow> getListSlideShow(){
        List<SlideShow> list =  new ArrayList<>();
        list.add(new SlideShow(R.drawable.image_1));
        list.add(new SlideShow(R.drawable.images_2));
        list.add(new SlideShow(R.drawable.images_3));
        list.add(new SlideShow(R.drawable.images_4));
        list.add(new SlideShow(R.drawable.images_5));
        list.add(new SlideShow(R.drawable.images_6));
        list.add(new SlideShow(R.drawable.images_7));
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }
}