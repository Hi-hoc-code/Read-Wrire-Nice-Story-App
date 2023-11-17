package com.example.read_write_app_duan1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.read_write_app_duan1.R;
import com.example.read_write_app_duan1.activities.AddStoryActivity;
import com.example.read_write_app_duan1.activities.EditStoryActivity;
import com.example.read_write_app_duan1.activities.LoginActivity;

public class WriteFragment extends Fragment {
    private TextView tvEdit, tvWriteNew;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_fragment,container,false);

        tvEdit = view.findViewById(R.id.tvEditStory);
        tvWriteNew = view.findViewById(R.id.tvWriteNewStory);

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditStoryActivity.class);
                startActivity(intent);
            }
        });

        tvWriteNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddStoryActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
