package com.example.read_write_app_duan1.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.activities.MainActivity;
import com.example.read_write_app_duan1.adapter.TypeAdapter;
import com.example.read_write_app_duan1.models.Book;
import com.example.read_write_app_duan1.models.Type;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class SearchFragment extends Fragment {
    RecyclerView rvcType;
    EditText edtSearch;
    DatabaseReference databaseReference;
    int numberOfColumns = 2;
    FirebaseStorage mStorage;
    ArrayList<Type> list;
    TypeAdapter adapterType;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment,container,false);
        addEvents(view);
        return view;
    }

    private void addEvents(View view) {
        edtSearch = view.findViewById(R.id.edtSeachType);
        rvcType  = view.findViewById(R.id.rvcType);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        rvcType.setLayoutManager(gridLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference("BookType");
        mStorage = FirebaseStorage.getInstance();
        list = new ArrayList<>();
        adapterType = new TypeAdapter(getContext(),list);
        rvcType.setAdapter(adapterType);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Type type = new Type();
                Object data = snapshot.getValue();
                if (data != null) {
                    if (data instanceof Map) {
                        Map<String, Object> mapData = (Map<String, Object>) data;
                        type.setType(String.valueOf(mapData.get("type")));
                        type.setImage(String.valueOf(mapData.get("image")));
                    }
                }
                list.add(type);
                adapterType.notifyDataSetChanged();
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

        //edit text change listen,search
     edtSearch.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            //called as and when user type each letter
             try {
                 adapterType.getFilter().filter(s);
             }
             catch (Exception e){

             }

         }

         @Override
         public void afterTextChanged(Editable s) {

         }
     });
    }

}
