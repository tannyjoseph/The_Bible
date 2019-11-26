package com.g.theholybible.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.g.theholybible.R;
import com.g.theholybible.activities.ChapterActivity;
import com.g.theholybible.activities.write_pop;
import com.g.theholybible.data.Bookmarks;
import com.g.theholybible.data.Verse;

import java.util.ArrayList;
import java.util.HashSet;

public class daily_notes extends Fragment {

    View v;

    SwipeMenuListView listt;

    public static ArrayList<String> notes = new ArrayList<String>();

    public static ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_daily_notes, container, false);

        listt = v.findViewById(R.id.listt);

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.g.theholybible", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null) {
            notes.add("Example");
        } else {
            notes = new ArrayList(set);
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                openItem.setBackground(R.color.purple);
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Delete");
                openItem.setTitleSize(18);
                openItem.setTitleColor(R.color.WHITE);

                menu.addMenuItem(openItem);

            }
        };

        listt.setMenuCreator(creator);

        listt.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.g.theholybible", Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet(notes);

                                //sharedPreferences.edit().putStringSet("notes",set).apply();
                                sharedPreferences.edit().putStringSet("notes", set).commit();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        Button add = v.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), write_pop.class));
            }
        });

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, notes);

        listt.setAdapter(arrayAdapter);

        listt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), write_pop.class);
                intent.putExtra("NoteId", position);
                startActivity(intent);
            }
        });


        return v;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
