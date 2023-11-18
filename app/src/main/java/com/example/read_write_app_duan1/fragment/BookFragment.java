package com.example.read_write_app_duan1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.read_write_app_duan1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BookFragment extends Fragment {
    BottomNavigationView bookMenuNavigate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        bookMenuNavigate = view.findViewById(R.id.bookBottomNavigation);
        bookMenuNavigate.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                if(item.getItemId() == R.id.menuBinhChon) {
                    fragment = new Fragment(R.layout.fragment_book);
                }
                if (item.getItemId() == R.id.menuView) {

                }
                if (item.getItemId() == R.id.menuChiSe) {

                }
                return false;
            }
        });
        return view;
    }
}
