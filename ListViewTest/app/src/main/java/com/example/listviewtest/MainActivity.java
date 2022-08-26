package com.example.listviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    public String[] data = {"Apple", "Banana", "Orange", "Watermelon", "Pear", "Grape",
//            "Pineapple", "Strawberry", "Cherry", "Mango", "Apple", "Banana", "Orange",
//            "Watermelon", "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango"};
private List<Fruit> fruitList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFruits();
        FruitAdapter adapter = new FruitAdapter(
                MainActivity.this, R.layout.fruit_item, fruitList
        );
        ListView listView = (ListView) findViewById(R.id.list_view);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                MainActivity.this, android.R.layout.simple_list_item_1, data
//        );
//        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Fruit fruit = fruitList.get(position);
                Toast.makeText(MainActivity.this, fruit.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFruits() {
        for (int i = 0; i < 2; i++) {
            Fruit apple = new Fruit("Apple", R.drawable.apple_pic);
            fruitList.add(apple);
            Fruit Banana = new Fruit("Banana", R.drawable.banana_pic);
            fruitList.add(Banana);
            Fruit Orange = new Fruit("Orange", R.drawable.orange_pic);
            fruitList.add(Orange);
            Fruit Watermelon = new Fruit("Watermelon", R.drawable.watermelon_pic);
            fruitList.add(Watermelon);
            Fruit Pear = new Fruit("Pear", R.drawable.pear_pic);
            fruitList.add(Pear);
            Fruit Grape = new Fruit("Grape", R.drawable.grape_pic);
            fruitList.add(Grape);
            Fruit Pineapple = new Fruit("Pineapple", R.drawable.pineapple_pic);
            fruitList.add(Pineapple);
            Fruit Strawberry = new Fruit("Strawberry", R.drawable.strawberry_pic);
            fruitList.add(Strawberry);
            Fruit Cherry = new Fruit("Cherry", R.drawable.cherry_pic);
            fruitList.add(Cherry);
            Fruit Mango = new Fruit("Mango", R.drawable.mango_pic);
            fruitList.add(Mango);
        }
    }
}