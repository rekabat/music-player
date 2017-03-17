package com.audio.tyger.tygeraudio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;


public class BrowserActivity extends AppCompatActivity { //implements OnItemClickListener  {

    private ListView filelv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        // get directory listView
        filelv = (ListView) findViewById(R.id.fileList);
        filelv.setAdapter(new DirectoryAdapter("/"));

        // set the onClick listener
        filelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                DirectoryAdapter da = (DirectoryAdapter) parent.getAdapter();
                da.selectDirectory(s);
                parent.setSelection(0);
            }
        });
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
