package com.g.theholybible.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.g.theholybible.R;
import com.g.theholybible.fragments.daily_notes;

import java.util.HashSet;

import static com.g.theholybible.fragments.daily_notes.arrayAdapter;
import static com.g.theholybible.fragments.daily_notes.notes;

public class write_pop extends Activity {

    EditText dNote;
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_pop);
        final int[] flag = {1};

        dNote = findViewById(R.id.dailynote);

        ImageView close = findViewById(R.id.close);
        final Button save = findViewById(R.id.save_note);


        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        int width = d.widthPixels;
        int height = d.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        Intent intent = getIntent();
        noteId = intent.getIntExtra("NoteId", -1);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (notes.get(noteId) == ""){
                    notes.remove(noteId);
                    arrayAdapter.notifyDataSetChanged();
                }

                write_pop.super.onBackPressed();  // optional depending on your needs

            }
        });

        if (noteId != -1) {
            dNote.setText(notes.get(noteId));
        } else {
            notes.add("");
            noteId = notes.size() - 1;
            arrayAdapter.notifyDataSetChanged();

        }

        dNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                if (!(s.equals(""))) {

                    notes.set(noteId, String.valueOf(s));
                    arrayAdapter.notifyDataSetChanged();

                } else {
                    save.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.g.theholybible", Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet(notes);

                sharedPreferences.edit().putStringSet("notes", set).apply();


                write_pop.super.onBackPressed();

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (notes.get(noteId) == ""){
            notes.remove(noteId);
            arrayAdapter.notifyDataSetChanged();
        }
        super.onBackPressed();

    }
}
