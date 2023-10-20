package com.example.myapplication;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private EditText editProductName;
    private DBHelper dbHelper;
    private int productId;

    public void openForEdit(MainActivity mainActivity, int id) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editProductName = findViewById(R.id.editProductName);
        dbHelper = new DBHelper(this);
        productId = getIntent().getIntExtra("productId", -1);

        if (productId != -1) {
            // Load the product's name into the edit text
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DBHelper.TABLE_PRODUCTS,
                    new String[]{DBHelper.COLUMN_PRODUCT_NAME},
                    DBHelper.COLUMN_ID + "=?",
                    new String[]{String.valueOf(productId)},
                    null,
                    null,
                    null);

            if (cursor.moveToNext()) {
                String productName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
                editProductName.setText(productName);
            }
        }
    }

//    public void openForEdit(MainActivity mainActivity, int productId) {
//        Intent intent = new Intent(this, EditActivity.class);
//        intent.putExtra("productId", productId);
//        startActivity(intent);
//    }
    public void saveProduct(View view) {
        String productName = editProductName.getText().toString().trim();

        if (!productName.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_PRODUCT_NAME, productName);

            int rowsUpdated = db.update(DBHelper.TABLE_PRODUCTS, values,
                    DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(productId)});

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Product name cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }
}