package com.example.read_write_app_duan1.models;

import android.widget.Filter;

import com.example.read_write_app_duan1.adapter.TypeAdapter;

import java.util.ArrayList;

public class FilterType extends Filter {
    //arrayList in which we wuant to search
    ArrayList<Type> filterList;
    //adapter in which filter need to be implemented
    TypeAdapter adapter;

    public FilterType(ArrayList<Type> filterList, TypeAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //value should not be null and emty
        if(constraint != null && constraint.length() >0){
            //change to upper case, or lower case to avoid case sensitivity
            constraint = constraint.toString().toUpperCase();
            ArrayList<Type> list = new ArrayList<>();
            for (int i=0; i<filterList.size(); i++){
              //validate
                if (filterList.get(i).getType().toUpperCase().contains(constraint)){
                    //add to filter list
                    list.add(filterList.get(i));
                }
            }
            results.count = list.size();
            results.values = list;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results; //don't miss it
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        //apply filter changes
        adapter.list = (ArrayList<Type>)results.values;

        //notify changes
        adapter.notifyDataSetChanged();
    }
}
