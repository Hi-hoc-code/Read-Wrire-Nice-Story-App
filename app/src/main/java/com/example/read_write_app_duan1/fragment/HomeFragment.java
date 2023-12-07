//        rcvCoTich = view.findViewById(R.id.rcvTruyenCoTich);
//        rcvTieuThuyet = view.findViewById(R.id.rcvTruyenThuyet);
//        rcvTruyenThuyet = view.findViewById(R.id.rcvTruyenThuyet);
//        rcvTinhYeu = view.findViewById(R.id.rcvTinhYeu);
//        rcvLichSuChinhTri = view.findViewById(R.id.rcvChinhTri);
//        rcvHuongDanHocTap = view.findViewById(R.id.rcvHuongDanHocTap);

//
//        rcvTieuThuyet.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        rcvTruyenThuyet.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        rcvCoTich.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        rcvTinhYeu.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        rcvLichSuChinhTri.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        rcvHuongDanHocTap.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

//        truyenCoTichAdapter = new LichSuAdapter(getContext(), list);
//        tieuThuyetAdapter = new TieuThuyetAdapter(getContext(), list);
//        truyenThuyetAadapter = new TruyenThuyetAdapter(getContext(), list);
//        tinhYeuAdapter = new TinhYeuAdapte(getContext(), list);
//        lichSuAdapter = new TinhYeuAdapte(getContext(), list);
//        huongDanHocTapAdapter = new HuongDanHocTapAdapter(getContext(), list);
//
//        rcvCoTich.setAdapter(truyenCoTichAdapter);
//        rcvTieuThuyet.setAdapter(tieuThuyetAdapter);
//        rcvTruyenThuyet.setAdapter(truyenThuyetAadapter);
//        rcvTinhYeu.setAdapter(tinhYeuAdapter);
//        rcvLichSuChinhTri.setAdapter(lichSuAdapter);
//        rcvHuongDanHocTap.setAdapter(huongDanHocTapAdapter);

//
//        rcvReadingBook = view.findViewById(R.id.rcvReading);
//        rcvRecommendStory = view.findViewById(R.id.rcvRecommendStory);
//        rcvRecommendStory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        rcvReadingBook.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        readingBookAdapter = new IsReadingBookAdapter(getContext(), list);
//        rcvReadingBook.setAdapter(readingBookAdapter);
//        recommendStoryAdapter = new RecommendStoryAdapter(getContext(), list);
//        rcvRecommendStory.setAdapter(recommendStoryAdapter);
package com.example.read_write_app_duan1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.activities.LoginActivity;
import com.example.read_write_app_duan1.adapter.HuongDanHocTapAdapter;
import com.example.read_write_app_duan1.adapter.LichSuAdapter;
import com.example.read_write_app_duan1.adapter.TieuThuyetAdapter;
import com.example.read_write_app_duan1.adapter.TinhYeuAdapter;
import com.example.read_write_app_duan1.adapter.TruyenCoTichAdapter;
import com.example.read_write_app_duan1.adapter.TruyenThuyetAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {
    DatabaseReference databaseReference;
    public static String AUTHOR;

    RecyclerView rcvTieuThuyet, rcvTruyenThuyet, rcvCoTich, rcvTinhYeu, rcvHuongDanHocTap, rcvLichSuChinhTri;
    DrawerLayout drawerLayout;
    ShapeableImageView imgAvatar;
    ArrayList<Book> listTieuThuyet,listCoTich, listTinhYeu,listTruyenThuyet,listHuongDanHocTap, listLichSuVaChinhTri ;
    TieuThuyetAdapter tieuThuyetAdapter;
    HuongDanHocTapAdapter huongDanHocTapAdapter;
    TruyenCoTichAdapter truyenCoTichAdapter;
    TinhYeuAdapter tinhYeuAdapter;
    LichSuAdapter lichSuAdapter;
    TruyenThuyetAdapter truyenThuyetAdapter;
    NavigationView navigationView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        addEvents(view);
        return view;
    }

    private void addEvents(View view) {
        loadDataForCategory("Hướng dẫn học tập");
        loadDataForCategory("Tiểu thuyết");
        loadDataForCategory("Truyền thuyết");
        loadDataForCategory("Tình yêu");
        loadDataForCategory("Lịch sử và chính trị");
        loadDataForCategory("Truyện cổ tích");
        imgAvatar  = view.findViewById(R.id.imgAvatar);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        navigationView = view.findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView tvUsername = headerView.findViewById(R.id.tvUsername);
        TextView tvEmail = headerView.findViewById(R.id.tvGmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AUTHOR = snapshot.child("username").getValue().toString();
                tvUsername.setText(AUTHOR);
                tvEmail.setText(snapshot.child("email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Click imgAvatar open/ close drawer navigation

        //Logout

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);

                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.draw_nav_home){
                    HomeFragment homeFragment = new HomeFragment();
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, homeFragment)
                            .commit();
                    drawerLayout.close();
                }
                if(item.getItemId() == R.id.draw_nav_search){
                    SearchFragment searchFragment = new SearchFragment();
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, searchFragment)
                            .commit();
                    drawerLayout.close();
                }
                if(item.getItemId() == R.id.draw_nav_notice){
                    NotiveFragment notiveFragment = new NotiveFragment();
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, notiveFragment)
                            .commit();
                    drawerLayout.close();
                }
                if(item.getItemId() == R.id.draw_nav_setting){
                    SearchFragment searchFragment = new SearchFragment();
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, searchFragment)
                            .commit();
                    drawerLayout.close();
                }
                if(item.getItemId() == R.id.draw_nav_logout){
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                return true;
            }
        });
        // RecycleView book-> type
        //init rcv
        rcvTieuThuyet = view.findViewById(R.id.rcvTieuThuyet);
        rcvTieuThuyet.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        rcvTruyenThuyet = view.findViewById(R.id.rcvTruyenThuyet);
        rcvTruyenThuyet.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        rcvCoTich= view.findViewById(R.id.rcvTruyenCoTich);
        rcvCoTich.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        rcvTinhYeu = view.findViewById(R.id.rcvTinhYeu);
        rcvTinhYeu.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        rcvHuongDanHocTap = view.findViewById(R.id.rcvHuongDanHocTap);
        rcvHuongDanHocTap.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        rcvLichSuChinhTri = view.findViewById(R.id.rcvChinhTri);
        rcvLichSuChinhTri.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //Create Array for every rcv
        listTieuThuyet = new ArrayList<>();
        listTruyenThuyet = new ArrayList<>();
        listCoTich = new ArrayList<>();
        listTinhYeu = new ArrayList<>();
        listHuongDanHocTap = new ArrayList<>();
        listLichSuVaChinhTri = new ArrayList<>();

        //Adapter

        tieuThuyetAdapter = new TieuThuyetAdapter(getContext(), listTieuThuyet);
        rcvTieuThuyet.setAdapter(tieuThuyetAdapter);

        truyenThuyetAdapter = new TruyenThuyetAdapter(getContext(), listTruyenThuyet);
        rcvTruyenThuyet.setAdapter(truyenThuyetAdapter);

        truyenCoTichAdapter = new TruyenCoTichAdapter(getContext(), listCoTich);
        rcvCoTich.setAdapter(truyenCoTichAdapter);

        tinhYeuAdapter = new TinhYeuAdapter(getContext(), listTinhYeu);
        rcvTinhYeu.setAdapter(tinhYeuAdapter);

        huongDanHocTapAdapter = new HuongDanHocTapAdapter(getContext(), listHuongDanHocTap);
        rcvHuongDanHocTap.setAdapter(huongDanHocTapAdapter);

        lichSuAdapter = new LichSuAdapter(getContext(),listLichSuVaChinhTri);
        rcvLichSuChinhTri.setAdapter(lichSuAdapter);

        // get data from firebase fill into rcv

    }
    private void loadDataForCategory(String category){
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference().child("Book");
        booksRef.orderByChild("type").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch (category) {
                    case "Hướng dẫn học tập" :{
                        handleDataForRecyclerView(dataSnapshot, listHuongDanHocTap, huongDanHocTapAdapter);
                        break;
                    }
                    case "Tiểu thuyết":{
                        handleDataForRecyclerView(dataSnapshot, listTieuThuyet, tieuThuyetAdapter);
                        break;
                    }
                    case "Truyền thuyết":{
                        handleDataForRecyclerView(dataSnapshot, listTruyenThuyet, truyenThuyetAdapter);
                        break;
                    }
                    case "Tình yêu":{
                        handleDataForRecyclerView(dataSnapshot, listTinhYeu, tinhYeuAdapter);
                        break;
                    }
                    case "Lịch sử và chính trị" :{
                        handleDataForRecyclerView(dataSnapshot,listLichSuVaChinhTri, lichSuAdapter);
                        break;
                    }
                    case "Truyện cổ tích":{
                        handleDataForRecyclerView(dataSnapshot, listCoTich,truyenCoTichAdapter);
                        break;
                    }
                    // Thêm các case khác tương ứng với từng thể loại sách và RecyclerView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Lỗi rồi ba ơi",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleDataForRecyclerView(DataSnapshot dataSnapshot, ArrayList<Book> bookList, RecyclerView.Adapter adapter) {
        bookList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Book book = snapshot.getValue(Book.class);
            if (book != null) {
                String name = book.getName();
                String image = book.getImage();
                bookList.add(new Book(name, image));
            }
        }
        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView sau khi thêm dữ liệu
    }
}

