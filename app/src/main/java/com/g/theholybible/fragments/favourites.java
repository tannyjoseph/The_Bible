package com.g.theholybible.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.g.theholybible.R;
import com.g.theholybible.activities.ChapterActivity;
import com.g.theholybible.data.Book;
import com.g.theholybible.data.Bookmarks;
import com.g.theholybible.data.Verse;
import com.g.theholybible.providers.BibleLibrary;

import java.util.List;


public class favourites extends Fragment {

    View view;
    SwipeMenuListView list;
    List<Book> books ;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favourites, container, false);

        list = view.findViewById(R.id.list1);
        list.setFastScrollEnabled(true);
        list.setTextFilterEnabled(true);

        books = BibleLibrary.getBooks(getActivity().getContentResolver());


        Bookmarks bookmarks = new Bookmarks(getContext());
        bookmarks.loadBookmarks();
        final List<Integer> bookmarkListing = bookmarks.bookmarks;

        final String[] bookmarkStrings = new String[bookmarkListing.size()];
        if (bookmarkListing.size() == 0) {
            Toast.makeText(getActivity(), "No bookmarks have been saved", Toast.LENGTH_LONG).show();
        }
        else {
            for (int i=0; i<bookmarkListing.size(); i++) {
                Verse bookmarkedVerse = BibleLibrary.getVerse(getActivity().getContentResolver(), bookmarkListing.get(i));
                final Book book = BibleLibrary.getBook(getActivity().getContentResolver(), bookmarkedVerse.bookId);
                bookmarkStrings[i] = book.name + " Chapter " + bookmarkedVerse.chapter + " Verse " + bookmarkedVerse.number;
            }

            list.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,bookmarkStrings));

        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Verse selectedBookmark = BibleLibrary.getVerse(getActivity().getContentResolver(), bookmarkListing.get(position));
                final Book book = BibleLibrary.getBook(getActivity().getContentResolver(), selectedBookmark.bookId);

                Intent intent = new Intent(getContext(), ChapterActivity.class);
                intent.putExtra(ChapterActivity.TITLE, book.name);
                intent.putExtra(ChapterActivity.BOOK_ID, book.id);
                intent.putExtra(ChapterActivity.CHAPTER, selectedBookmark.chapter);
                intent.putExtra(ChapterActivity.VERSE, selectedBookmark.number);
                startActivity(intent);

            }

        });

        return view;

    }



}
