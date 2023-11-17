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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View view2 = inflater.inflate(R.layout.dialog_delete, null);
                tvHuy = view.findViewById(R.id.tvHuy);
                tvYes = view.findViewById(R.id.tvYes);
                builder.setView(view);

                //show
                AlertDialog alertDialog = builder.create();
                // sửa bo góc tran
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                //khi nào nhấn nút mới out dialog
                alertDialog.setCancelable(false);
                alertDialog.show();

                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });

                //nút hủy
                tvHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });



        return view;

    }
}