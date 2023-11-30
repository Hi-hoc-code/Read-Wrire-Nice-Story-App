package com.example.read_write_app_duan1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.fragment.HomeFragment;
import com.example.read_write_app_duan1.fragment.LibraryFragment;
import com.example.read_write_app_duan1.fragment.SearchFragment;
import com.example.read_write_app_duan1.fragment.NotiveFragment;
import com.example.read_write_app_duan1.fragment.WriteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity{
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Khái báo, khởi tạo, ánh xạ tại addControls()
        addControls();
        //Xử lí xự kiến tại addEvents()
        addEvents();
    }


    private void addEvents() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new HomeFragment();
                //Set HomeFragment làm mặt định
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, fragment).commit();
                //Select đến fragment tương ứng được click
                if(item.getItemId()==R.id.home_fragment){
                    fragment = new HomeFragment();
                }
                if(item.getItemId()==R.id.library_fragment){
                    fragment= new LibraryFragment();
                }
                if(item.getItemId()==R.id.search_fragment){
                    fragment= new SearchFragment();
                }
                if(item.getItemId()==R.id.write_fragment){
                    fragment= new WriteFragment();
                }
                if(item.getItemId()==R.id.notice_fragment){
                    fragment= new NotiveFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout,fragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
        });
    }

    private void addControls() {
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }
}