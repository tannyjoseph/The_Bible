package com.g.theholybible.activities;

import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuAdapter;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.g.theholybible.R;
import com.g.theholybible.adapters.BookAdapter;
import com.g.theholybible.adapters.VerseAdapter;
import com.g.theholybible.data.Book;
import com.g.theholybible.data.Bookmarks;
import com.g.theholybible.data.Chapter;
import com.g.theholybible.data.Verse;
import com.g.theholybible.providers.BibleLibrary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ChapterActivity extends AppCompatActivity implements OnItemLongClickListener {

    private static final String TAG = "ChapterActivity";

    public static final String TITLE = "ChapterActivity.title";
    public static final String BOOK_ID = "ChapterActivity.book_id";
    public static final String CHAPTER = "ChapterActivity.chapter";
    public static final String VERSE = "ChapterActivity.verse";

    private String book;
    private int bookId;
    private int chapter;
    public int verse;
    private List<Verse> verses;
    private View navigationPanel;
    private Handler closeNavigationHandler;
    private Thread closeNavigationThread;

    SwipeMenuListView listView;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.chapter);

        Window window = ChapterActivity.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(ChapterActivity.this, R.color.gray));

        this.book = getIntent().getStringExtra(TITLE);
        this.bookId = getIntent().getIntExtra(BOOK_ID, 1);
        this.chapter = getIntent().getIntExtra(CHAPTER, 1);
        this.verse = getIntent().getIntExtra(VERSE, 0);
        listView = findViewById(R.id.list);

        closeNavigationHandler = new Handler();
        closeNavigationThread = new Thread(new Runnable() {

            public void run() {
                navigationPanel.startAnimation(AnimationUtils.loadAnimation(ChapterActivity.this,
                        R.anim.slide_out));
                navigationPanel.setVisibility(View.GONE);
            }
        });

        loadChapter();

        listView.setOnItemLongClickListener(this);



        listView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (navigationPanel == null) {
                        navigationPanel = ((ViewStub) findViewById(R.id.stub_navigation)).inflate();
                        navigationPanel.setVisibility(View.GONE);
                        ImageButton previous = (ImageButton)navigationPanel.findViewById(R.id.navigation_previous);
                        previous.setBackgroundDrawable(null);
                        previous.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                postHideNavigation();
                                backward();
                            }
                        });

                        ImageButton next = (ImageButton)navigationPanel.findViewById(R.id.navigation_next);
                        next.setBackgroundDrawable(null);
                        next.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                postHideNavigation();
                                forward();
                            }
                        });
                    }

                    if (navigationPanel.getVisibility() != View.VISIBLE) {
                        navigationPanel.startAnimation(AnimationUtils.loadAnimation(ChapterActivity.this,
                                R.anim.slide_in));
                        navigationPanel.setVisibility(View.VISIBLE);
                    }
                    postHideNavigation();
                }
                return false;
            }
        });
    }

    private void postHideNavigation() {
        // Start a time to hide the panel after 3 seconds
        closeNavigationHandler.removeCallbacks(closeNavigationThread);
        closeNavigationHandler.postDelayed(closeNavigationThread, 3000);
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Verse selectedVerse = verses.get(position);

    //        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Add Bookmark");
//        builder.setMessage("Add bookmark for " + this.book +
//                " Chapter " + selectedVerse.chapter +
//                " verse " + (position + 1) + "?");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                Bookmarks bookmarks = new Bookmarks(ChapterActivity.this);
//                bookmarks.addBookmark(selectedVerse.id);
//
//                dialog.cancel();
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // Canceled.
//                dialog.cancel();
//            }
//        });
//
//        builder.show();

        return true;
    }

    private void backward() {
        // If this is the first chapter of the first book, do nothing
        // If this is the first chapter, go to the previous book
        // Else go to the previous chapter
        if (this.bookId == 1 && this.chapter == 1) {
            Toast.makeText(this, "You are currently at the beginning of the Bible", Toast.LENGTH_SHORT).show();
            // Do nothing
        }
        else if (this.chapter > 1) {
            // Go to the previous chapter
            this.chapter--;
            loadChapter();
        }
        else {
            // Go to the previous book (last chapter in that book)
            this.bookId--;
            // Load the previous book to update the title
            final Book previousBook = BibleLibrary.getBook(getContentResolver(), this.bookId);
            if (previousBook != null) {
                this.book = previousBook.name;
            }

            // Set the current chapter to the last chapter in the previous book
            int chapterCount = BibleLibrary.getChapterCount(getContentResolver(), previousBook);
            this.chapter = chapterCount;

            // Load the data
            loadChapter();
        }
    }

    private void forward() {
        // If this is the last chapter in the last book, do nothing
        // If this is the last chapter in the book, go to the first chapter in the next book
        // Else go to the next chapter
        if (this.bookId == 66 && this.chapter == 22) {
            Toast.makeText(this, "You have reached the end of the Bible", Toast.LENGTH_SHORT).show();

            // Do nothing
            return;
        }

        // Get the chapter count
        int chapterCount = BibleLibrary.getChapterCount(getContentResolver(), this.bookId);

        if (this.chapter == chapterCount) {
            // Go to the next book
            this.bookId++;
            this.chapter = 1;

            // Load the next book to update the title
            final Book nextBook = BibleLibrary.getBook(getContentResolver(), this.bookId);
            if (nextBook != null) {
                this.book = nextBook.name;
            }

            loadChapter();
        }
        else {
            // Go to the next chapter
            this.chapter++;
            loadChapter();
        }
    }

    private void loadChapter() {
        if (this.book != null)
            this.setTitle(this.book);

        this.verse = getIntent().getIntExtra(VERSE, 0);


        ((TextView)findViewById(R.id.chapter_heading)).setText("Chapter " + this.chapter);

        this.verses = BibleLibrary.getVerses(getContentResolver(), this.bookId, this.chapter);
        Log.d(TAG, "Loaded " + this.verses.size() + " verses");
        VerseAdapter adapter = new VerseAdapter(this, this.verses);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(R.color.WHITE);
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setIcon(R.drawable.heart);

                menu.addMenuItem(openItem);

            }
        };
        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    final Verse selectedVerse = verses.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChapterActivity.this);
                    builder.setTitle("Add Bookmark");
                    builder.setMessage(ChapterActivity.this.book +
                            " Chapter " + selectedVerse.chapter +
                            " verse " + (position + 1)+ "\n"+ "Add to Favourites?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Bookmarks bookmarks = new Bookmarks(ChapterActivity.this);
                            bookmarks.addBookmark(selectedVerse.id);

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
                return false;
            }
        });

        String toast = (this.book != null) ? (this.book + " Chapter " + this.chapter) : ("Chapter " + this.chapter);
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.chapter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.load_bookmarks_menu_item:
                loadBookmark();
                break;
            case R.id.change_font_menu_item:
                changeFont();
                break;
            case R.id.select_chapter_menu_item:
                selectChapter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBookmark() {
        Bookmarks bookmarks = new Bookmarks(this);
        bookmarks.loadBookmarks();
        final List<Integer> bookmarkListing = bookmarks.bookmarks;

        final String[] bookmarkStrings = new String[bookmarkListing.size()];
        if (bookmarkListing.size() == 0) {
            Toast.makeText(this, "No bookmarks have been saved", Toast.LENGTH_LONG).show();
        }
        else {
            for (int i=0; i<bookmarkListing.size(); i++) {
                Verse bookmarkedVerse = BibleLibrary.getVerse(getContentResolver(), bookmarkListing.get(i));
                final Book book = BibleLibrary.getBook(getContentResolver(), bookmarkedVerse.bookId);
                bookmarkStrings[i] = book.name + " Chapter " + bookmarkedVerse.chapter + " Verse " + bookmarkedVerse.number;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Load Bookmark");
            builder.setSingleChoiceItems(bookmarkStrings, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Verse selectedBookmark = BibleLibrary.getVerse(getContentResolver(), bookmarkListing.get(which));
                    final Book book = BibleLibrary.getBook(getContentResolver(), selectedBookmark.bookId);

                    Intent intent = new Intent(ChapterActivity.this, ChapterActivity.class);
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
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void changeFont() {

        final float MIN_FONT_SIZE = 14;
        final float MAX_FONT_SIZE = 26;
        final VerseAdapter adapter = (VerseAdapter)((SwipeMenuAdapter)listView.getAdapter()).getWrappedAdapter();
        final float originalFontSize = adapter.getFontSize();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Font Size");
        View view = getLayoutInflater().inflate(R.layout.change_font, null);
        final SeekBar sizerBar = (SeekBar) view.findViewById(R.id.change_font_seekbar);
        sizerBar.setMax((int)(MAX_FONT_SIZE - MIN_FONT_SIZE));
        sizerBar.setProgress((int)(originalFontSize - MIN_FONT_SIZE));
        sizerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
                //  Auto-generated method stub

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                //  Auto-generated method stub

            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                adapter.setFontSize(progress + MIN_FONT_SIZE);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Save the new preference
                SharedPreferences prefs = ChapterActivity.this.getSharedPreferences("VerseAdapter", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat(VerseAdapter.FONT_PREFERENCE, sizerBar.getProgress() + MIN_FONT_SIZE);
                editor.commit();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                adapter.setFontSize(originalFontSize);
                adapter.notifyDataSetChanged();
            }
        });

        builder.show();
    }

    public void selectChapter() {
        final Book book = BibleLibrary.getBook(getContentResolver(), this.bookId);
        int count = BibleLibrary.getChapterCount(getContentResolver(), book);

        final String[] chapterNames = new String[count];
        for (int i=0; i<count; i++) {
            chapterNames[i] = "Chapter " + (i+1);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(book.name);
        builder.setSingleChoiceItems(chapterNames, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                ChapterActivity.this.chapter = which+1;
                loadChapter();

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
