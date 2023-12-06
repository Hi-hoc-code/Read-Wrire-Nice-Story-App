package com.example.read_write_app_duan1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.activities.GetAllBookActivity;
import com.example.read_write_app_duan1.adapter.HuongDanHocTapAdapter;
import com.example.read_write_app_duan1.adapter.LichSuAdapter;
import com.example.read_write_app_duan1.adapter.RecommendStoryAdapter;
import com.example.read_write_app_duan1.adapter.TieuThuyetAdapter;
import com.example.read_write_app_duan1.adapter.TinhYeuAdapter;
import com.example.read_write_app_duan1.adapter.TruyenCoTichAdapter;
import com.example.read_write_app_duan1.adapter.TruyenThuyetAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class HomeFragment extends Fragment {
    DatabaseReference bookRef;
    FirebaseStorage mStorage;
    RecyclerView rcvTieuThuyet, rcvTruyenThuyet, rcvCoTich, rcvTinhYeu, rcvHuongDanHocTap, rcvLichSuChinhTri, rcvRecomment;
    DrawerLayout drawerLayout;
    ShapeableImageView imgAvatar;
    ArrayList<Book> listTieuThuyet, listCoTich, listTinhYeu, listTruyenThuyet, listHuongDanHocTap, listLichSuVaChinhTri, listRecomment, randomBookList;
    TieuThuyetAdapter tieuThuyetAdapter;
    HuongDanHocTapAdapter huongDanHocTapAdapter;
    TruyenCoTichAdapter truyenCoTichAdapter;
    TinhYeuAdapter tinhYeuAdapter;
    LichSuAdapter lichSuAdapter;
    TruyenThuyetAdapter truyenThuyetAdapter;
    RecommendStoryAdapter recommendStoryAdapter;
    Handler handler;
    Random random;
    Integer interval = 30*1000;
    Button btnGetAllBook;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        addEvents(view);
        return view;
    }

    private void addEvents(View view) {
        fragmentManager = getParentFragmentManager();
        btnGetAllBook = view.findViewById(R.id.btnGetAllBook);
        loadDataForCategory("Hướng dẫn học tập", bookRef);
        loadDataForCategory("Tiểu thuyết", bookRef);
        loadDataForCategory("Truyền thuyết", bookRef);
        loadDataForCategory("Tình yêu", bookRef);
        loadDataForCategory("Lịch sử và chính trị", bookRef);
        loadDataForCategory("Truyện cổ tích", bookRef);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        fragmentManager = getParentFragmentManager();

        btnGetAllBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), GetAllBookActivity.class));
            }
        });

        bookRef = FirebaseDatabase.getInstance().getReference("Book");
        // Click imgAvatar open/ close drawer navigation
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        // RecycleView book-> type
        //init rcv
        rcvRecomment = view.findViewById(R.id.rcvRecommendStory);
        rcvRecomment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvTieuThuyet = view.findViewById(R.id.rcvTieuThuyet);
        rcvTieuThuyet.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvTruyenThuyet = view.findViewById(R.id.rcvTruyenThuyet);
        rcvTruyenThuyet.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvCoTich = view.findViewById(R.id.rcvTruyenCoTich);
        rcvCoTich.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvTinhYeu = view.findViewById(R.id.rcvTinhYeu);
        rcvTinhYeu.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvHuongDanHocTap = view.findViewById(R.id.rcvHuongDanHocTap);
        rcvHuongDanHocTap.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvLichSuChinhTri = view.findViewById(R.id.rcvChinhTri);
        rcvLichSuChinhTri.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Create Array for every rcv
        listTieuThuyet = new ArrayList<>();
        listTruyenThuyet = new ArrayList<>();
        listCoTich = new ArrayList<>();
        listTinhYeu = new ArrayList<>();
        listHuongDanHocTap = new ArrayList<>();
        listLichSuVaChinhTri = new ArrayList<>();
        listRecomment = new ArrayList<>();

        //Adapter

        tieuThuyetAdapter = new TieuThuyetAdapter(getContext(), listTieuThuyet, fragmentManager);
        rcvTieuThuyet.setAdapter(tieuThuyetAdapter);

        truyenThuyetAdapter = new TruyenThuyetAdapter(getContext(), listTruyenThuyet, fragmentManager);
        rcvTruyenThuyet.setAdapter(truyenThuyetAdapter);

        truyenCoTichAdapter = new TruyenCoTichAdapter(getContext(), listCoTich, fragmentManager);
        rcvCoTich.setAdapter(truyenCoTichAdapter);

        tinhYeuAdapter = new TinhYeuAdapter(getContext(), listTinhYeu, fragmentManager);
        rcvTinhYeu.setAdapter(tinhYeuAdapter);

        huongDanHocTapAdapter = new HuongDanHocTapAdapter(getContext(), listHuongDanHocTap, fragmentManager);
        rcvHuongDanHocTap.setAdapter(huongDanHocTapAdapter);

        lichSuAdapter = new LichSuAdapter(getContext(), listLichSuVaChinhTri,fragmentManager);
        rcvLichSuChinhTri.setAdapter(lichSuAdapter);

        recommendStoryAdapter = new RecommendStoryAdapter(getContext(), listRecomment,fragmentManager);
        rcvRecomment.setAdapter(recommendStoryAdapter);

//            Data recomment book random 5 book every 60s

        loadDataForRecommentBook(bookRef, listRecomment, recommendStoryAdapter);
    }

    private void loadDataForRecommentBook(DatabaseReference bookRef, ArrayList<Book> listRecomment, RecommendStoryAdapter recommendStoryAdapter) {
        bookRef = FirebaseDatabase.getInstance().getReference("Book");
        bookRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Book book = new Book();
                Object data = snapshot.getValue();
                if (data != null) {
                    if (data instanceof Map) {
                        Map<String, Object> mapData = (Map<String, Object>) data;
                        book.setName(String.valueOf(mapData.get("name")));
                        book.setImage(String.valueOf(mapData.get("image")));
                        book.setId(String.valueOf(mapData.get("idBook")));
                        // Set other book properties accordingly
                    }
                }
                listRecomment.add(book);
                recommendStoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        bookRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listRecomment.clear();
//                try {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        String name = dataSnapshot.child("name").getValue(String.class);
//                        String image = dataSnapshot.child("image").getValue(String.class);
//                        String IdBook = dataSnapshot.child("id").getValue(String.class);
//
//                        listRecomment.add(new Book(name, image, IdBook));
//
//
//                        randomBookList= new ArrayList<>();
//                        random = new Random();
//                        handler=new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                updateRandomBook();
//                                handler.postDelayed(this,interval);
//                            }
//                        }, interval);
//                        recommendStoryAdapter.notifyDataSetChanged();
//
//                    }
//                } catch (Exception ex) {
//                    Log.d("TAG", "onDataChange: ", ex);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        randomBookList= new ArrayList<>();
        random = new Random();
        handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateRandomBook();
                handler.postDelayed(this,interval);
            }
        }, interval);
    }
    private void updateRandomBook() {
        if (listRecomment.size() > 0) {
            randomBookList.clear();
            int maxIndex = listRecomment.size();
            if (maxIndex > 5) {
                ArrayList<Integer> index = new ArrayList<>();
                for (int i = 0; i < maxIndex; i++) {
                    index.add(i);
                }
                Collections.shuffle(index);
                for (int i = 0; i < 5; i++) {
                    int randomIndex = index.get(i);
                    randomBookList.add(listRecomment.get(randomIndex));
                }
            } else {
                randomBookList.addAll(listRecomment);
            }

            // Update listRecomment with randomBookList
            listRecomment.clear();
            listRecomment.addAll(randomBookList);
            // Notify the adapter that data has changed
            recommendStoryAdapter.notifyDataSetChanged();
        }
    }

    private void loadDataForCategory(String category, DatabaseReference bookRef) {
        bookRef = FirebaseDatabase.getInstance().getReference("Book");
        bookRef.orderByChild("type").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch (category) {
                    case "Hướng dẫn học tập": {
                        handleDataForRecyclerView(dataSnapshot, listHuongDanHocTap, huongDanHocTapAdapter);
                        break;
                    }
                    case "Tiểu thuyết": {
                        handleDataForRecyclerView(dataSnapshot, listTieuThuyet, tieuThuyetAdapter);
                        break;
                    }
                    case "Truyền thuyết": {
                        handleDataForRecyclerView(dataSnapshot, listTruyenThuyet, truyenThuyetAdapter);
                        break;
                    }
                    case "Tình yêu": {
                        handleDataForRecyclerView(dataSnapshot, listTinhYeu, tinhYeuAdapter);
                        break;
                    }
                    case "Lịch sử và chính trị": {
                        handleDataForRecyclerView(dataSnapshot, listLichSuVaChinhTri, lichSuAdapter);
                        break;
                    }
                    case "Truyện cổ tích": {
                        handleDataForRecyclerView(dataSnapshot, listCoTich, truyenCoTichAdapter);
                        break;
                    }
                    // Thêm các case khác tương ứng với từng thể loại sách và RecyclerView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi rồi ba ơi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleDataForRecyclerView(DataSnapshot dataSnapshot, ArrayList<Book> bookList, RecyclerView.Adapter adapter) {
        try {
            bookList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String name = snapshot.child("name").getValue(String.class);
                String image = snapshot.child("image").getValue(String.class);
                if (name != null && image != null) {
                    Book book = new Book(name, image);
                    bookList.add(book);
                }
            }
            adapter.notifyDataSetChanged(); // Cập nhật RecyclerView sau khi thêm dữ liệu
        } catch (Exception ex) {
            Log.d("lỗi", "Quá lỗi", ex);
        }
    }
}

