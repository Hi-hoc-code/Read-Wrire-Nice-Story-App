package com.example.read_write_app_duan1.fragment;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.read_write_app_duan1.R;

public class WriteLibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView tvYes, tvHuy;
    public WriteLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_library, container, false);
        imageView = view.findViewById(R.id.imgDeletedWrite);



        return view;

    }
}