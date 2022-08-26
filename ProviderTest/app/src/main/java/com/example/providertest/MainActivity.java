package com.example.providertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 添加数据
                /*
                    首先调用了Uri.parse方法将一个内容URI解析成URI对象，然后把要添加的数据都存入到ContentValues
                    对象中，接着调用ContentResolver的insert方法执行添加操作即可，注意insert方法会返回一个Uri
                    对象，这个对象中包含了新增数据的id，我们通过getPathSegments方法将它取出
                 */
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                ContentValues values = new ContentValues();
                values.put("name", "A Clash of Kings");
                values.put("author", "George Martin");
                values.put("pages", 1040);
                values.put("price", 55.55);
                Uri newUri = getContentResolver().insert(uri, values);
                newId = newUri.getPathSegments().get(1);
//                Log.d("MainActivity", "bookid: " + newId);
            }
        });

        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 查询数据
                /*
                    调用ContentResolver的query方法去查询数据，结果存放在cursor对象中，再对cursor进行遍历
                    取出查询对象并打印出来
                 */
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                        int pages = cursor.getInt(cursor.getColumnIndexOrThrow("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                        Log.d("MainActivity", "book name is " + name);
                        Log.d("MainActivity", "book author is " + author);
                        Log.d("MainActivity", "book pages is " + pages);
                        Log.d("MainActivity", "book price is " + price);
                    }
                    cursor.close();
                }
            }
        });

        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 更新数据
                /*
                    调用ContentResolver的update方法去更新数据，注意这里，我们为了不让book表中的其他行受影响，
                    在调用Uri.parse的时候，给内容Uri尾部增加了一个id，而这个id正好是添加数据时返回的，这就表示
                    我们只希望更新刚刚添加的那条数据，Book表中的其他行不会受到影响
                 */
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book/" + newId);
                ContentValues values = new ContentValues();
                values.put("name", "A Storm of Swords");
                values.put("pages", 1216);
                values.put("price", 24.05);
                int update = getContentResolver().update(uri, values, null, null);
//                Log.d("MainActivity", "update rows: " + update + "bookId: " + newId);
            }
        });

        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 删除数据
                /*
                    删除数据的时候，也使用同样的方法解析了一个以id结尾的内容Uri，然后调用ContentResolver的delete
                    方法执行删除操作即可，book表中的其他行不会受到影响
                 */
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book/" + newId);
                getContentResolver().delete(uri, null, null);
            }
        });
    }
}



























