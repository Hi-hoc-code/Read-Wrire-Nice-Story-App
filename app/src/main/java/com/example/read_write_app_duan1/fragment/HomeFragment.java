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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.adapter.HuongDanHocTapAdapter;
import com.example.read_write_app_duan1.adapter.LichSuAdapter;
import com.example.read_write_app_duan1.adapter.TieuThuyetAdapter;
import com.example.read_write_app_duan1.adapter.TinhYeuAdapte;
import com.example.read_write_app_duan1.adapter.TruyenCoTichAdapter;
import com.example.read_write_app_duan1.adapter.TruyenThuyetAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    DatabaseReference databaseReference;
    FirebaseStorage mStorage;
    RecyclerView rcvTieuThuyet, rcvTruyenThuyet, rcvCoTich, rcvTinhYeu, rcvHuongDanHocTap, rcvLichSuChinhTri;
    DrawerLayout drawerLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        addEvents(view);
        return view;
    }

    private void addEvents(View view) {
        drawerLayout = view.findViewById(R.id.drawerLayout);

        // Khởi tạo danh sách sách cho từng loại
        ArrayList<Book> tieuThuyetBooks = new ArrayList<>();
        ArrayList<Book> truyenThuyetBooks = new ArrayList<>();
        ArrayList<Book> truyenCoTichBooks = new ArrayList<>();
        ArrayList<Book> tinhYeuBooks = new ArrayList<>();
        ArrayList<Book> lichSuChinhTri = new ArrayList<>();
        ArrayList<Book> huongDanHocTap = new ArrayList<>();
        // Tham chiếu đến Firebase
        DatabaseReference bookReference = FirebaseDatabase.getInstance().getReference("Book");
        DatabaseReference bookTypeReference = FirebaseDatabase.getInstance().getReference("BookType");

        // Sử dụng ValueEventListener để lấy danh sách sách dựa trên loại từ BookType
        bookTypeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String bookType = snapshot.child("type").getValue(String.class);

                    // Lọc dữ liệu sách dựa trên loại sách từ bookType
                    Query query = bookReference.orderByChild("type").equalTo(bookType);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Book> books = new ArrayList<>();
                            for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                                Book book = bookSnapshot.getValue(Book.class);
                                if (book != null) {
                                    books.add(book);
                                }
                            }
                            // Gán dữ liệu vào RecyclerView tương ứng với loại sách
                            switch (bookType) {
                                case "Tiểu thuyết":
                                    tieuThuyetBooks.addAll(books);
                                    TieuThuyetAdapter tieuThuyetAdapter = new TieuThuyetAdapter(getContext(), tieuThuyetBooks);
                                    rcvTieuThuyet.setAdapter(tieuThuyetAdapter);
                                    break;
                                case "Truyền thuyết":
                                    truyenThuyetBooks.addAll(books);
                                    TruyenThuyetAdapter truyenThuyetAdapter = new TruyenThuyetAdapter(getContext(), truyenThuyetBooks);
                                    rcvTruyenThuyet.setAdapter(truyenThuyetAdapter);
                                    break;
                                case "Tình yêu":
                                    tinhYeuBooks.addAll(books);
                                    TinhYeuAdapte tinhYeuAdapte = new TinhYeuAdapte(getContext(),tinhYeuBooks);
                                    rcvTinhYeu.setAdapter(tinhYeuAdapte);
                                case "Truyện cổ tích":
                                    truyenCoTichBooks.addAll(books);
                                    TruyenCoTichAdapter coTichAdapter = new TruyenCoTichAdapter(getContext(),truyenCoTichBooks);
                                    rcvCoTich.setAdapter(coTichAdapter);
                                case "Lịch sử và chính trị":
                                    lichSuChinhTri.addAll(books);
                                    LichSuAdapter lichSuAdapter = new LichSuAdapter(getContext(),lichSuChinhTri);
                                    rcvLichSuChinhTri.setAdapter(lichSuAdapter);
                                case "Hướng dẫn học tập":
                                    huongDanHocTap.addAll(books);
                                    HuongDanHocTapAdapter huongDanHocTapAdapter = new HuongDanHocTapAdapter(getContext(),huongDanHocTap);
                                    rcvHuongDanHocTap.setAdapter(huongDanHocTapAdapter);
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý lỗi nếu có
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });

        // Gán RecyclerView từ layout
        rcvTieuThuyet = view.findViewById(R.id.rcvTieuThuyet);
        rcvTieuThuyet.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvTruyenThuyet = view.findViewById(R.id.rcvTruyenThuyet);
        rcvTruyenThuyet.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvCoTich= view.findViewById(R.id.rcvTruyenCoTich);
        rcvCoTich.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvTinhYeu = view.findViewById(R.id.rcvTinhYeu);
        rcvTinhYeu.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvLichSuChinhTri = view.findViewById(R.id.rcvChinhTri);
        rcvLichSuChinhTri.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rcvHuongDanHocTap = view.findViewById(R.id.rcvHuongDanHocTap);
        rcvHuongDanHocTap.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}
