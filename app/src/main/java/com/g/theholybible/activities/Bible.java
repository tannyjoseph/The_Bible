package com.g.theholybible.activities;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuAdapter;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.g.theholybible.adapters.BookAdapter;
import com.g.theholybible.data.Book;
import com.g.theholybible.R;
import com.g.theholybible.data.Bookmarks;
import com.g.theholybible.data.CSVWriter;
import com.g.theholybible.data.Verse;
import com.g.theholybible.fragments.daily_notes;
import com.g.theholybible.fragments.favourites;
import com.g.theholybible.providers.BibleDatabaseHelper;
import com.g.theholybible.providers.BibleLibrary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


public class Bible extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    List<Book> books = null;

    SwipeMenuListView listView;

    NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holy__bible);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listView = findViewById(R.id.listview);
        listView.setFastScrollEnabled(true);
        listView.setTextFilterEnabled(true);

        setTitle("Books of the Bible");

        books = BibleLibrary.getBooks(getContentResolver());
        listView.setAdapter(new BookAdapter(Bible.this, books));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

            }
        };
        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        BookAdapter adapter = (BookAdapter)((SwipeMenuAdapter) listView.getAdapter()).getWrappedAdapter();

                        final Book book = (adapter.getItem(position));
                        //int count = BibleLibrary.getChapterCount(getContentResolver(), book);

                        //if (count == 1) {
                        gotoChapter(book, 1);
                        break;
                    case 1:
                        // delete
//					delete(item);
                        break;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookAdapter adapter = (BookAdapter) ((SwipeMenuAdapter)listView.getAdapter()).getWrappedAdapter();

                final Book book = (adapter.getItem(position));
                //int count = BibleLibrary.getChapterCount(getContentResolver(), book);

                //if (count == 1) {
                gotoChapter(book, 1);
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void selectChapter(final Book book) {
        int count = BibleLibrary.getChapterCount(getContentResolver(), book);

        if (count == 1) {
            gotoChapter(book, 1);
        }
        else {
            final String[] chapterNames = new String[count];
            for (int i=0; i<count; i++) {
                chapterNames[i] = "Chapter " + (i+1);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(book.name);
            builder.setSingleChoiceItems(chapterNames, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    gotoChapter(book, which+1);

                    dialog.cancel();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }

    private void gotoChapter(final Book book, final int chapter) {
        Intent intent = new Intent(Bible.this, ChapterActivity.class);
        intent.putExtra(ChapterActivity.TITLE, book.name);
        intent.putExtra(ChapterActivity.BOOK_ID, book.id);
        intent.putExtra(ChapterActivity.CHAPTER, chapter);
        startActivity(intent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            //showSearchOptions();
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.books_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.load_bookmarks_menu_item:
                loadBookmark();
                break;
            case R.id.delete_bookmarks_menu_item:
                removeBookmarks();
                break;
            case R.id.search_menu_item:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBookmark() {
        Bookmarks bookmarks = new Bookmarks(this);
        bookmarks.loadBookmarks();
        final List<Integer> bookmarkListing = bookmarks.bookmarks;

        if (bookmarkListing.size() == 0) {
            Toast.makeText(this, "No bookmarks have been saved", Toast.LENGTH_LONG).show();
        }
        else {
            final String[] bookmarkStrings = new String[bookmarkListing.size()];
            for (int i=0; i<bookmarkListing.size(); i++) {
                Verse bookmarkedVerse = BibleLibrary.getVerse(getContentResolver(), bookmarkListing.get(i));
                Book book = findBook(bookmarkedVerse.bookId);
                bookmarkStrings[i] = book.name + " Chapter " + bookmarkedVerse.chapter + " Verse " + bookmarkedVerse.number;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Load Bookmark");
            builder.setSingleChoiceItems(bookmarkStrings, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Verse selectedBookmark = BibleLibrary.getVerse(getContentResolver(), bookmarkListing.get(which));
                    Book book = findBook(selectedBookmark.bookId);

                    Intent intent = new Intent(Bible.this, ChapterActivity.class);
                    intent.putExtra(ChapterActivity.TITLE, book.name);
                    intent.putExtra(ChapterActivity.BOOK_ID, book.id);
                    intent.putExtra(ChapterActivity.CHAPTER, selectedBookmark.chapter);
                    intent.putExtra(ChapterActivity.VERSE, selectedBookmark.number);
                    startActivity(intent);

                    dialog.cancel();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }




    private void removeBookmarks() {
        final Bookmarks bookmarks = new Bookmarks(this);
        bookmarks.loadBookmarks();
        final List<Integer> bookmarkListing = bookmarks.bookmarks;

        final String[] bookmarkStrings = new String[bookmarkListing.size()];
        if (bookmarkListing.size() == 0) {
            Toast.makeText(this, "No bookmarks have been saved", Toast.LENGTH_LONG).show();
        }
        else {
            for (int i=0; i<bookmarkListing.size(); i++) {
                Verse bookmarkedVerse = BibleLibrary.getVerse(getContentResolver(), bookmarkListing.get(i));
                Book book = findBook(bookmarkedVerse.bookId);
                bookmarkStrings[i] = book.name + " Chapter " + bookmarkedVerse.chapter + " Verse " + bookmarkedVerse.number;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Remove Bookmarks");
            final HashSet<Integer> bookmarksToDelete = new HashSet<Integer>();
            builder.setMultiChoiceItems(bookmarkStrings, null, new DialogInterface.OnMultiChoiceClickListener() {

                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if (isChecked)
                        bookmarksToDelete.add(bookmarkListing.get(which));
                    else
                        bookmarksToDelete.remove(bookmarkListing.get(which));
                }
            });

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Delete the bookmarks
                    for (final Integer bookmark : bookmarksToDelete) {
                        bookmarks.removeBookmark(bookmark);
                    }

                    // Dismiss the dialog
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }

    private Book findBook(final int bookId) {
        for (int i=0; i<books.size(); i++) {
            Book book = books.get(i);
            if (book.id.equals(bookId))
                return book;
        }
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        Fragment frag = null;

        if (id == R.id.nav_home){
            Intent intent = new Intent(Bible.this, Bible.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        if (id == R.id.favourites)
            frag = new favourites();

        if (id == R.id.notes)
            frag = new daily_notes();

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, frag);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}