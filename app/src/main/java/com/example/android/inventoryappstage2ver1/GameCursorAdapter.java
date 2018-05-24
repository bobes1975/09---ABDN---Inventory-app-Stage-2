package com.example.android.inventoryappstage2ver1;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryappstage2ver1.data.DatabaseContract.GameEntry;

/**
 * {@link GameCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */

public class GameCursorAdapter extends CursorAdapter {


    /**
     * Constructs a new {@link GameCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public GameCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the game data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView mGameName = view.findViewById(R.id.game_name);
        TextView mGamePrice = view.findViewById(R.id.game_price);
        TextView mGameQuantity = view.findViewById(R.id.game_in_stock);
        ImageButton sellButton = view.findViewById(R.id.sell_button);

        // Find the columns of game attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_NAME);
        int priceColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_PRICE);
        int stockColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_QUANTITY);

        // Read the game attributes from the Cursor for the current pet
        String gameName = cursor.getString(nameColumnIndex);
        String gamePrice = cursor.getString(priceColumnIndex);
        String gameQuantity = cursor.getString(stockColumnIndex);


        //Add text to display values

        String gamePriceText = "Price: " + gamePrice + " $";
        String gameQuantityText = "In stock: " + gameQuantity + " pcs";

        // Update the TextViews with the attributes for the current game
        mGameName.setText(gameName);
        mGamePrice.setText(gamePriceText);
        mGameQuantity.setText(gameQuantityText);


        final int quantityColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_QUANTITY);
        String currentQuantity = cursor.getString(quantityColumnIndex);
        final int quantityIntCurrent = Integer.valueOf(currentQuantity);

        final int productId = cursor.getInt(cursor.getColumnIndex(GameEntry._ID));

        //Sell button which decrease quantity in storage
        sellButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (quantityIntCurrent > 0) {
                    int newQuantity = quantityIntCurrent - 1;
                    Uri quantityUri = ContentUris.withAppendedId(GameEntry.CONTENT_URI, productId);

                    ContentValues values = new ContentValues();
                    values.put(GameEntry.COLUMN_QUANTITY, newQuantity);
                    context.getContentResolver().update(quantityUri, values, null, null);
                } else {
                    Toast.makeText(context, "This game is out of stock!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
