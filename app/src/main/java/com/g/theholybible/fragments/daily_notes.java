package com.g.theholybible.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.g.theholybible.R;
import com.g.theholybible.activities.write_pop;

import java.util.ArrayList;
import java.util.HashSet;

public class daily_notes extends Fragment {

    View v ;

    SwipeMenuListView listt;
    static ArrayList<String> notes = new ArrayList<String>();

    static ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_daily_notes, container, false);

        listt = v.findViewById(R.id.listt);

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.g.theholybible", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null){
            notes.add("Example");
        }

        else {
            notes = new ArrayList(set);
        }

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,notes);

        listt.setAdapter(arrayAdapter);

        listt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), write_pop.class);
                intent.putExtra("NoteId",position);
                startActivity(intent);
            }
        });

        return v;
    }


}
