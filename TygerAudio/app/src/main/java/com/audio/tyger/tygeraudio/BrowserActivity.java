package com.audio.tyger.tygeraudio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.ArrayList;

public class BrowserActivity extends AppCompatActivity { //implements OnItemClickListener  {

    private ListView filelv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);


        filelv = (ListView) findViewById(R.id.fileList);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> myArray = new ArrayList<String>();
        myArray.add("foo1");
        myArray.add("foo2");
        myArray.add("foo3");
        myArray.add("foo4");
        myArray.add("foo5");
        myArray.add("foo6");
        myArray.add("foo7");
        myArray.add("foo8");
        myArray.add("foo9");
        myArray.add("foo10");
        myArray.add("foo11");
        myArray.add("foo12");
        myArray.add("foo13");
        myArray.add("foo14");
        myArray.add("foo15");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                myArray );

        filelv.setAdapter(arrayAdapter);


//        ListView listview = (ListView) findViewById(R.id.fileList);
//        listview.setOnItemClickListener(this);
    }

//    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
//        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
//        // Then you start a new Activity via Intent
//        Intent intent = new Intent();
//        intent.setClass(this, ListItemDetail.class);
//        intent.putExtra("position", position);
//        // Or / And
//        intent.putExtra("id", id);
//        startActivity(intent);
//    }
}
