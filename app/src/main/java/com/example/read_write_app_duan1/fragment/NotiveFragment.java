package com.example.read_write_app_duan1.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.SectionPagerLibraryAdapter;
import com.google.android.material.tabs.TabLayout;

public class NotiveFragment extends Fragment {

    View view;
    ViewPager viewPager;
    TabLayout tabLayout;
    FrameLayout frameLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment, container,false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewPager);

        // Set màu cho văn bản ở TabLayout
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FF9901"));
        // Set màu cho thanh chuyển khi được chọn
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF9901"));
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerLibraryAdapter adapter = new SectionPagerLibraryAdapter(getChildFragmentManager());

        adapter.addFragment(new NotiFragment(), "THÔNG BÁO");
        adapter.addFragment(new MesegeFragment(), "TIN NHẮN");


        viewPager.setAdapter(adapter);

    }
    }

