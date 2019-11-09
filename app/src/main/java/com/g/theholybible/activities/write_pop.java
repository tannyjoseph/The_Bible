package com.g.theholybible.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.g.theholybible.R;

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

        dNote = findViewById(R.id.dailynote);

        ImageView close = findViewById(R.id.close);



        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        int width = d.widthPixels;
        int height = d.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height*0.6));

        Intent intent = getIntent();
        noteId = intent.getIntExtra("NoteId", -1);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if (noteId != -1){
            dNote.setText(notes.get(noteId));
        }

        //        else {
//
////            notes.add("");
//            noteId = notes.size() - 1;
//            arrayAdapter.notifyDataSetChanged();
//
//        }

        dNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!(s.equals(""))) {

                    notes.set(noteId, String.valueOf(s));
                    arrayAdapter.notifyDataSetChanged();

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.g.theholybible", Context.MODE_PRIVATE);

                    HashSet<String> set = new HashSet(notes);

                    sharedPreferences.edit().putStringSet("notes", set).apply();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });

    }
}
