package com.g.theholybible.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.g.theholybible.R;
import com.g.theholybible.activities.write_pop;

import java.util.ArrayList;
import java.util.HashSet;

public class daily_notes extends Fragment {

    View v ;

    SwipeMenuListView listt;

    public static ArrayList<String> notes = new ArrayList<String>();

    public static ArrayAdapter arrayAdapter;

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

        Button add = v.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), write_pop.class));
            }
        });

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

        listt.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet(notes);

                                sharedPreferences.edit().putStringSet("notes",set).apply();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        return v;
    }


}
