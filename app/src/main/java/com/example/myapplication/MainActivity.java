package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText productNameInput;
    private ListView productList;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productNameInput = findViewById(R.id.productName);
        productList = findViewById(R.id.productList);
        dbHelper = new DBHelper(this);

        displayProductList();

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Open the EditActivity for editing
                Cursor cursor = (Cursor) productList.getItemAtPosition(i);
//                int id = cursor.getInt(0);
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                EditActivity editActivity = new EditActivity();
                editActivity.openForEdit(MainActivity.this, id);
            }
        });
    }

    public void addProduct(View view) {
        String productName = productNameInput.getText().toString().trim();

        if (!productName.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_PRODUCT_NAME, productName);
            db.insert(DBHelper.TABLE_PRODUCTS, null, values);
            productNameInput.getText().clear();
            displayProductList();
        } else {
            Toast.makeText(this, "Product name cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayProductList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_PRODUCTS,
                null,
                null,
                null,
                null,
                null,
                null);
        List<String> productListData = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Lấy dữ liệu sản phẩm từ cột cụ thể trong Cursor
                String productName = String.valueOf(cursor.getColumnIndex("product_name"));
                // Thêm sản phẩm vào danh sách
                productListData.add(productName);
            } while (cursor.moveToNext());

            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productListData);
        productList.setAdapter(adapter);
    }

}