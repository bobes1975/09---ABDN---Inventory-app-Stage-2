package com.example.android.inventoryappstage2ver1;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.android.inventoryappstage2ver1.data.DatabaseContract.GameEntry;

public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the game data loader
     */
    private static final int GAME_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    GameCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        //Insert new item into database
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the game data
        ListView gameListView = findViewById(R.id.list_games);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        gameListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of game data in the Cursor.
        // There is no game data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new GameCursorAdapter(this, null);
        gameListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link DetailActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific game that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link GameEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.inv.../games/2"
                // if the game with ID 2 was clicked on.
                Uri currentGameUri = ContentUris.withAppendedId(GameEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentGameUri);

                // Launch the {@link EditorActivity} to display the data for the current game.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(GAME_LOADER, null, this);
    }

    /**
     * Helper method to insert hardcoded game data into the database. For debugging purposes only.
     */
    private void insertGame() {
        // Create a ContentValues object where column names are the keys,
        // and game's example attributes are the values.
        ContentValues values = new ContentValues();
        values.put(GameEntry.COLUMN_NAME, getString(R.string.example_game_name_));
        values.put(GameEntry.COLUMN_CATEGORY, GameEntry.CATEGORY_RPG);
        values.put(GameEntry.COLUMN_PRICE, 22);
        values.put(GameEntry.COLUMN_QUANTITY, 10);
        values.put(GameEntry.COLUMN_SUPPLIER, getString(R.string.example_supplier));
        values.put(GameEntry.COLUMN_SUPPLIER_PHONE, getString(R.string.example_supplier_phone));
        values.put(GameEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.example_supplier_email));


        // Insert a new row for game's example into the provider using the ContentResolver.
        // Use the {@link GameEntry#CONTENT_URI} to indicate that we want to insert
        // into the games database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(GameEntry.CONTENT_URI, values);
    }

    /**
     * Helper method to delete all games in the database.
     */
    private void deleteAllGames() {
        int rowsDeleted = getContentResolver().delete(GameEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + getString(R.string.msg_rows_deteted));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.insert_dummy_data:
                insertGame();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllGames();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                GameEntry._ID,
                GameEntry.COLUMN_NAME,
                GameEntry.COLUMN_PRICE,
                GameEntry.COLUMN_QUANTITY};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                GameEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link GameCursorAdapter} with this new cursor containing updated game data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }


}
