package com.example.read_write_app_duan1.adapter;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.example.read_write_app_duan1.models.Book;

import java.util.ArrayList;

public class GetAllAdapter extends BookAdapter{
    private ArrayList<Book> filteredList;

    public GetAllAdapter(Context context, ArrayList<Book> list, FragmentManager fragmentManager) {
        super(context, list);
    }

    public void setFilter(ArrayList<Book> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }
}