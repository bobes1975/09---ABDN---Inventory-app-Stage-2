package com.example.android.inventoryappstage2ver1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryappstage2ver1.data.DatabaseContract.GameEntry;

import static com.example.android.inventoryappstage2ver1.R.*;


public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the game data loader
     */
    private static final int EXISTING_GAME_LOADER = 0;

    /**
     * Content URI for the existing game(null if it's a new game)
     */
    private Uri mCurrentGameUri;

    /**
     * EditText field to enter the game's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the game's price
     */
    private EditText mPriceEditText;

    /**
     * EditText field to enter the game's quantity
     */
    private EditText mQuantityEditText;

    /**
     * EditText field to enter the game's supplier
     */
    private EditText mSupplierEditText;

    /**
     * EditText field to enter the game's supplier phone
     */
    private EditText mPhoneEditText;

    /**
     * EditText field to enter the game's supplier email
     */
    private EditText mEmailEditText;

    /**
     * EditText field to enter the game's category
     */
    private Spinner mCategorySpinner;


    /**
     * int for quantity check
     */
    private int givenQuantity;

    /**
     * Category of the game. The possible valid values are in the GameContract.java file:
     * {@link GameEntry#CATEGORY_RPG},
     * {@link GameEntry#CATEGORY_SIMULATOR},
     * {@link GameEntry#CATEGORY_1PERSON},
     * {@link GameEntry#CATEGORY_ADVENTURE},
     * {@link GameEntry#CATEGORY_RTS},
     * {@link GameEntry#CATEGORY_PUZZLE},
     * {@link GameEntry#CATEGORY_ACTION},
     * {@link GameEntry#CATEGORY_COMBAT},
     * {@link GameEntry#CATEGORY_EDU},
     * or {@link GameEntry#CATEGORY_UNKNOWN}.
     */
    private int mCategory = GameEntry.CATEGORY_UNKNOWN;

    /**
     * Boolean flag that keeps track of whether the game has been edited (true) or not (false)
     */
    private boolean mGameHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mGameHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mGameHasChanged = true;
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.edit_layout);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new game or editing an existing one.
        Intent intent = getIntent();
        mCurrentGameUri = intent.getData();

        // If the intent DOES NOT contain a game content URI, then we know that we are
        // creating a new game
        if (mCurrentGameUri == null) {
            // This is a new game, so change the app bar to say "Add a new game"
            setTitle(getString(string.new_game));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a game that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing game, so change app bar to say "Edit game"
            setTitle(getString(string.editor_activity_title_edit_game));

            // Initialize a loader to read the game data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_GAME_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(id.edit_game_name);
        mPriceEditText = findViewById(id.edit_game_price);
        mQuantityEditText = findViewById(id.edit_game_quantity);
        mSupplierEditText = findViewById(id.edit_supplier);
        mPhoneEditText = findViewById(id.edit_phone);
        mEmailEditText = findViewById(id.edit_email);
        mCategorySpinner = findViewById(id.spinner_game_category);

        /* Button for increasing quantity */
        ImageButton mIncrease = findViewById(id.edit_quantity_increase);

        /* Button for decreasing quantity  */
        ImageButton mDecrease = findViewById(id.edit_quantity_decrease);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);
        mIncrease.setOnTouchListener(mTouchListener);
        mDecrease.setOnTouchListener(mTouchListener);
        mCategorySpinner.setOnTouchListener(mTouchListener);


        //increase quantity
        mIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quantity = mQuantityEditText.getText().toString();
                if (TextUtils.isEmpty(quantity)) {

                    Toast.makeText(EditorActivity.this, string.quantity_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    givenQuantity = Integer.parseInt(quantity);
                    mQuantityEditText.setText(String.valueOf(givenQuantity + 1));
                }

            }
        });

        //decrease quantity
        mDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = mQuantityEditText.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    Toast.makeText(EditorActivity.this, string.quantity_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    givenQuantity = Integer.parseInt(quantity);
                    //To validate if quantity is greater than 0
                    if ((givenQuantity - 1) >= 0) {
                        mQuantityEditText.setText(String.valueOf(givenQuantity - 1));
                    } else {
                        Toast.makeText(EditorActivity.this, string.quantity_cannot_be_less_0, Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
            }
        });

        /* Button for phone call  */
        ImageButton mPhone = findViewById(id.phoneButton);

        /* Button for email  */
        final ImageButton mEmail = findViewById(id.emailButton);


        //email
        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inStock = mQuantityEditText.getText().toString().trim();
                String supplier = mSupplierEditText.getText().toString().trim();
                String emailAddress = mEmailEditText.getText().toString().trim();
                String gameName = mNameEditText.getText().toString().trim();

                orderByEmail(emailAddress, gameName, inStock, supplier);

            }
        });

        //button for phone call
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PhoneNum = mPhoneEditText.getText().toString().trim();
                orderByPhone(PhoneNum);

            }
        });


        //spinner setup
        setupSpinner();

    }


    /**
     * Setup the dropdown spinner that allows the user to select the category of the game.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                array.array_game_category, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(categorySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(string.category_rpg))) {
                        mCategory = GameEntry.CATEGORY_RPG;
                    } else if (selection.equals(getString(string.category_simulator))) {
                        mCategory = GameEntry.CATEGORY_SIMULATOR;
                    } else if (selection.equals(getString(string.category_1stPerson))) {
                        mCategory = GameEntry.CATEGORY_1PERSON;
                    } else if (selection.equals(getString(string.category_adventure))) {
                        mCategory = GameEntry.CATEGORY_ADVENTURE;
                    } else if (selection.equals(getString(string.category_rts))) {
                        mCategory = GameEntry.CATEGORY_RTS;
                    } else if (selection.equals(getString(string.category_puzzle))) {
                        mCategory = GameEntry.CATEGORY_PUZZLE;
                    } else if (selection.equals(getString(string.category_action))) {
                        mCategory = GameEntry.CATEGORY_ACTION;
                    } else if (selection.equals(getString(string.category_combat))) {
                        mCategory = GameEntry.CATEGORY_COMBAT;
                    } else if (selection.equals(getString(string.category_educational))) {
                        mCategory = GameEntry.CATEGORY_EDU;
                    } else {
                        mCategory = GameEntry.CATEGORY_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategory = GameEntry.CATEGORY_UNKNOWN;
            }
        });
    }


    /**
     * Get user input from editor and save game into database.
     */
    private void saveGame() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space

        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();
        String emailString = mEmailEditText.getText().toString().trim();


        // Check if this is supposed to be a new item.
        // Also check if all the fields in the editor are blank.
        if (mCurrentGameUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(supplierString) &&
                TextUtils.isEmpty(phoneString) && TextUtils.isEmpty(emailString)) {

            Toast.makeText(this, (getString(string.fill_blank)), Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(nameString)) {
            mNameEditText.setError(getString(string.question_game_price));
            return;
        }

        if (TextUtils.isEmpty(priceString)) {
            mPriceEditText.setError(getString(string.question_game_price));
            return;
        }

        if (TextUtils.isEmpty(quantityString)) {
            mQuantityEditText.setError(getString(string.question_game_quantity));
            return;
        }

        if (TextUtils.isEmpty(supplierString)) {
            mSupplierEditText.setError(getString(string.question_game_supplier));
            return;
        }

        if (TextUtils.isEmpty(phoneString)) {
            mPhoneEditText.setError(getString(string.question_game_supplier_phone));
            return;
        }

        if (TextUtils.isEmpty(emailString)) {
            mEmailEditText.setError(getString(string.question_game_supplier_email));
            return;
        }


        Double priceInt = Double.parseDouble(priceString);
        int quantityInt = Integer.parseInt(quantityString);
        int phoneInt = Integer.parseInt(phoneString);


        // Create a ContentValues object where column names are the keys,
        // and game attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(GameEntry.COLUMN_NAME, nameString);
        values.put(GameEntry.COLUMN_CATEGORY, mCategory);
        values.put(GameEntry.COLUMN_PRICE, priceInt);
        values.put(GameEntry.COLUMN_QUANTITY, quantityInt);
        values.put(GameEntry.COLUMN_SUPPLIER, supplierString);
        values.put(GameEntry.COLUMN_SUPPLIER_PHONE, phoneInt);
        values.put(GameEntry.COLUMN_SUPPLIER_EMAIL, emailString);

        // Determine if this is a new or existing game by checking if mCurrentGameUri is null or not
        if (mCurrentGameUri == null) {
            // This is a new game, so insert a new game into the provider,
            // returning the content URI for the new game.
            Uri newUri = getContentResolver().insert(GameEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(string.insert_game_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(string.insert_game_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an existing game, so update the game with content URI: mCurrentGameUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentGameUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentGameUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(string.update_game_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(string.update_game_successful),
                        Toast.LENGTH_SHORT).show();
            }

            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new game, hide the "Delete" menu item.
        if (mCurrentGameUri == null) {
            MenuItem menuItem = menu.findItem(id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case id.action_save:
                // Save game to database
                saveGame();
                // Exit activity
                return true;
            // Respond to a click on the "Delete" menu option
            case id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the game hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mGameHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the game hasn't changed, continue with handling back button press
        if (!mGameHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all game attributes, define a projection that contains
        // all columns from the game table
        String[] projection = {
                GameEntry._ID,
                GameEntry.COLUMN_NAME,
                GameEntry.COLUMN_CATEGORY,
                GameEntry.COLUMN_PRICE,
                GameEntry.COLUMN_QUANTITY,
                GameEntry.COLUMN_SUPPLIER,
                GameEntry.COLUMN_SUPPLIER_PHONE,
                GameEntry.COLUMN_SUPPLIER_EMAIL,};


        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentGameUri,         // Query the content URI for the current game
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of game attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_NAME);
            int categoryColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_CATEGORY);
            int priceColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_SUPPLIER_PHONE);
            int emailColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_SUPPLIER_EMAIL);

            // Extract out the value from the Cursor for the given column index
            final String name = cursor.getString(nameColumnIndex);
            int category = cursor.getInt(categoryColumnIndex);
            double price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            int phone = cursor.getInt(phoneColumnIndex);
            final String email = cursor.getString(emailColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mPriceEditText.setText(Double.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierEditText.setText(supplier);
            mPhoneEditText.setText(Integer.toString(phone));
            mEmailEditText.setText(email);


            // Game category is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options
            //  CATEGORY_RPG = 1;
            //  CATEGORY_SIMULATOR = 2;
            //  CATEGORY_1PERSON = 3;
            //  CATEGORY_ADVENTURE = 4;
            //  CATEGORY_RTS = 5;
            //  CATEGORY_PUZZLE= 6;
            //  CATEGORY_ACTION= 7;
            //  CATEGORY_COMBAT= 8;
            //  CATEGORY_EDU= 9;
            //  CATEGORY_UNKNOWN= 0;
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (category) {
                case GameEntry.CATEGORY_RPG:
                    mCategorySpinner.setSelection(1);
                    break;
                case GameEntry.CATEGORY_SIMULATOR:
                    mCategorySpinner.setSelection(2);
                    break;
                case GameEntry.CATEGORY_1PERSON:
                    mCategorySpinner.setSelection(3);
                    break;
                case GameEntry.CATEGORY_ADVENTURE:
                    mCategorySpinner.setSelection(4);
                    break;
                case GameEntry.CATEGORY_RTS:
                    mCategorySpinner.setSelection(5);
                    break;
                case GameEntry.CATEGORY_PUZZLE:
                    mCategorySpinner.setSelection(6);
                    break;
                case GameEntry.CATEGORY_ACTION:
                    mCategorySpinner.setSelection(7);
                    break;
                case GameEntry.CATEGORY_COMBAT:
                    mCategorySpinner.setSelection(8);
                    break;
                case GameEntry.CATEGORY_EDU:
                    mCategorySpinner.setSelection(9);
                    break;
                default:
                    mCategorySpinner.setSelection(0);
                    break;
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierEditText.setText("");
        mPhoneEditText.setText("");
        mEmailEditText.setText("");
        mCategorySpinner.setSelection(0); // Select "Unknown" category
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(string.unsaved_changes);
        builder.setPositiveButton(string.discard, discardButtonClickListener);
        builder.setNegativeButton(string.keep_edit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the game.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this game.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(string.delete_msg);
        builder.setPositiveButton(string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the game.
                deleteGame();
            }
        });
        builder.setNegativeButton(string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the game.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the game in the database.
     */
    private void deleteGame() {
        // Only perform the delete if this is an existing game.
        if (mCurrentGameUri != null) {
            // Call the ContentResolver to delete the game at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentGameUri
            // content URI already identifies the game that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentGameUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(string.delete_game_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(string.delete_game_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    /**
     * Send an order via email to supplier
     *
     * @param emailAddress - supplier's email address
     * @param inStock      - quantity
     * @param supplier     - Supplier's name
     * @param gameName     - Game's name
     */
    private void orderByEmail(String emailAddress, String gameName, String inStock, String supplier) {
        String subject = getString(string.email_order_of) + " " + gameName + " " + getString(string.email_game);

        int quantityInStock = Integer.valueOf(inStock);

        //Create general email body text

        //Dear @param supplier
        String emailBody = getString((string.email_body1)) + " " + supplier + ",";

        if (quantityInStock < 1) {
            //Could you tell me when @param gameName will be available again?
            emailBody = emailBody + "\n" + getString((string.email_body4)) + " " + gameName + " " + getString((string.email_body5));

        } else {
            //I would like to order @param gameName game from your offer.
            emailBody = emailBody + "\n" + getString((string.email_body2)) + " " + gameName + " " + getString((string.email_body3));

        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Call game's supplier
     *
     * @param phoneNumber - supplier's phone number
     */
    private void orderByPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}