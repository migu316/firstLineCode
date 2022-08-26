package com.example.litepaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LitePal.getDatabase();
            }
        });

        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = new Book();
                book.setName("The Da Vinci Code");
                book.setAuthor("Dan Brown");
                book.setPages(454);
                book.setPrice(16.96);
                book.setPress("Unknow");
                book.save();
            }
        });

//        Button updateData = (Button) findViewById(R.id.update_data);
//        updateData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Book book = new Book();
//                book.setName("The Lost Symbol");
//                book.setAuthor("Dan Brown");
//                book.setPages(510);
//                book.setPrice(19.95);
//                book.setPress("Unknow");
//                book.save();
//                book.setPrice(10.99);
//                book.save();
//            }
//        });

        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = new Book();
                book.setPrice(14.95);
                book.setPress("Anchor");
                book.setPages(500);
                book.updateAll("name = ? and author = ?", "The Da Vinci Code", "Dan Brown");
                // 将某列修改为默认值
//                book.setToDefault("pages");
//                book.updateAll();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 将价格小于15的书全部删除
                // 注意：DataSupport已经弃用，改成了LitePal
                LitePal.deleteAll(Book.class, "price < ?", "15");
            }
        });

        Button queryButton = (Button) findViewById(R.id.query_data);
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Book> books = LitePal.findAll(Book.class);
//                for (Book book : books) {
//                    Log.d("MainActivity", "book name is " + book.getName());
//                    Log.d("MainActivity", "book author is " + book.getAuthor());
//                    Log.d("MainActivity", "book pages is " + book.getPages());
//                    Log.d("MainActivity", "book price is " + book.getPrice());
//                    Log.d("MainActivity", "book press is " + book.getPress());
//                }
//            }
//        });


//         查询表中第一条数据
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Book book = LitePal.findFirst(Book.class);
//                Log.d("MainActivity", "book name is " + book.getName());
//                Log.d("MainActivity", "book author is " + book.getAuthor());
//                Log.d("MainActivity", "book pages is " + book.getPages());
//                Log.d("MainActivity", "book price is " + book.getPrice());
//                Log.d("MainActivity", "book press is " + book.getPress());
//            }
//        });
//
//         查询表中最后一条数据
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Book book = LitePal.findLast(Book.class);
//                Log.d("MainActivity", "book name is " + book.getName());
//                Log.d("MainActivity", "book author is " + book.getAuthor());
//                Log.d("MainActivity", "book pages is " + book.getPages());
//                Log.d("MainActivity", "book price is " + book.getPrice());
//                Log.d("MainActivity", "book press is " + book.getPress());
//            }
//        });
//
//         通过Select方法查询，对应了Sql中的select
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Book> books = LitePal.select("name", "author").find(Book.class);
//                for (Book book : books) {
//                    Log.d("MainActivity", "book name is " + book.getName());
//                    Log.d("MainActivity", "book author is " + book.getAuthor());
//                }
//            }
//        });
//
//         通过where方法指定查询的约束条件
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Book> books = LitePal.where("pages > ?", "400").find(Book.class);
//                for (Book book : books) {
//                    Log.d("MainActivity", "book name is " + book.getName());
//                    Log.d("MainActivity", "book author is " + book.getAuthor());
//                    Log.d("MainActivity", "book pages is " + book.getPages());
//                    Log.d("MainActivity", "book price is " + book.getPrice());
//                    Log.d("MainActivity", "book press is " + book.getPress());
//                }
//            }
//        });
//
//
//         order方法用于指定结果的排序方式
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Book> books = LitePal.order("price desc").find(Book.class);
//                for (Book book : books) {
//                    Log.d("MainActivity", "book name is " + book.getName());
//                    Log.d("MainActivity", "book author is " + book.getAuthor());
//                    Log.d("MainActivity", "book pages is " + book.getPages());
//                    Log.d("MainActivity", "book price is " + book.getPrice());
//                    Log.d("MainActivity", "book press is " + book.getPress());
//                }
//            }
//        });
//
//         limit方法用于查询结果的数量
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Book> books = LitePal.limit(3).find(Book.class);
//                for (Book book : books) {
//                    Log.d("MainActivity", "book name is " + book.getName());
//                    Log.d("MainActivity", "book author is " + book.getAuthor());
//                    Log.d("MainActivity", "book pages is " + book.getPages());
//                    Log.d("MainActivity", "book price is " + book.getPrice());
//                    Log.d("MainActivity", "book press is " + book.getPress());
//                }
//            }
//        });
//
//
//         offset方法用于指定结果的偏移量
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Book> books = LitePal.limit(3).offset(1).find(Book.class);
//                for (Book book : books) {
//                    Log.d("MainActivity", "book name is " + book.getName());
//                    Log.d("MainActivity", "book author is " + book.getAuthor());
//                    Log.d("MainActivity", "book pages is " + book.getPages());
//                    Log.d("MainActivity", "book price is " + book.getPrice());
//                    Log.d("MainActivity", "book press is " + book.getPress());
//                }
//            }
//        });
//
//         支持上述方法的任意连缀组合，来完成一个比较复杂的查询操作
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Book> books = LitePal.select("name", "author", "pages", "price")
//                        .where("pages > ?", "400")
//                        .order("pages")
//                        .limit(10)
//                        .offset(10)
//                        .find(Book.class);
//                for (Book book : books) {
//                    Log.d("MainActivity", "book name is " + book.getName());
//                    Log.d("MainActivity", "book author is " + book.getAuthor());
//                    Log.d("MainActivity", "book pages is " + book.getPages());
//                    Log.d("MainActivity", "book price is " + book.getPrice());
//                    Log.d("MainActivity", "book press is " + book.getPress());
//                }
//            }
//        });
    }
}



























