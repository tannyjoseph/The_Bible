package com.g.theholybible.activities;

import java.util.HashSet;
import java.util.List;

import com.g.theholybible.adapters.BookAdapter;
import com.g.theholybible.data.Book;
import com.g.theholybible.R;
import com.g.theholybible.data.Bookmarks;
import com.g.theholybible.data.Verse;
import com.g.theholybible.fragments.daily_notes;
import com.g.theholybible.fragments.favourites;
import com.g.theholybible.providers.BibleLibrary;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class Bible extends AppCompatActivity implements OnItemClickListener, NavigationView.OnNavigationItemSelectedListener
{
    List<Book> books = null;

    ListView listView;

    NavigationView navigationView;
    HamButton.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holy__bible);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


//        ActionBar mActionBar = getSupportActionBar();
//        mActionBar.setDisplayShowHomeEnabled(false);
//        mActionBar.setDisplayShowTitleEnabled(false);
//        LayoutInflater mInflater = LayoutInflater.from(this);
//
//
//        View actionBar = mInflater.inflate(R.layout.custom_actionbar, null);
//        TextView mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
//        mTitleTextView.setText("ActionBar");
//        mActionBar.setCustomView(actionBar);
//        mActionBar.setDisplayShowCustomEnabled(true);
//        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);
//
//        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
//
//        bmb.setButtonEnum(ButtonEnum.Ham);
//
//        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
//             builder = (HamButton.Builder) new HamButton.Builder()
//                    .normalImageRes(R.drawable.forward)
//                    .normalText("Butter Doesn't fly!")
//                    .subNormalText("Little butter Doesn't fly, either!");
//            bmb.addBuilder(builder);
//        }
//
//        BoomMenuButton leftBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_left_bmb);
//        BoomMenuButton rightBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_right_bmb);
//
//        leftBmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
//        leftBmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_1);
//        leftBmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
//        for (int i = 0; i < leftBmb.getPiecePlaceEnum().pieceNumber(); i++){
//            builder = (HamButton.Builder) new HamButton.Builder()
//                    .normalImageRes(R.drawable.forward)
//                    .normalText("Butter Doesn't fly!")
//                    .subNormalText("Little butter Doesn't fly, either!");
//            leftBmb.addBuilder(builder);
//
//        }

//        rightBmb.setButtonEnum(ButtonEnum.Ham);
//        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
//        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);
//        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++)
//            rightBmb.addBuilder(BuilderManager.getHamButtonBuilderWithDifferentPieceColor());
//
        listView = findViewById(R.id.listview);
        listView.setFastScrollEnabled(true);
        listView.setTextFilterEnabled(true);

        setTitle("Books of the Bible");

        books = BibleLibrary.getBooks(getContentResolver());
        listView.setAdapter(new BookAdapter(this, books));
        listView.setOnItemClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //final Book book = books.get(position);
        BookAdapter adapter = (BookAdapter) listView.getAdapter();
        final Book book = (adapter.getItem(position));
        //int count = BibleLibrary.getChapterCount(getContentResolver(), book);

        //if (count == 1) {
        gotoChapter(book, 1);
	/*}
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
	}*/
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.books_menu, menu);
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