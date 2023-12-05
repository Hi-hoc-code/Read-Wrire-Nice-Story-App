package com.example.read_write_app_duan1.fragment;

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

public class LibraryFragment extends Fragment {

    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;

    FrameLayout frameLayout;

    public LibraryFragment(){}

    public static LibraryFragment getInstance(){
        return new LibraryFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.library_fragment,container,false);
        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
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

        adapter.addFragment(new ReadLibraryFragment(), "TRUYỆN ĐANG ĐỌC");
        adapter.addFragment(new WriteLibraryFragment(), "DANH SÁCH VIẾT");


        viewPager.setAdapter(adapter);

    }

}
