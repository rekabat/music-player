package com.audio.tyger.tygeraudio;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;


public class BrowserActivity extends AppCompatActivity { //implements OnItemClickListener  {

    private ListView filelv;
    final private String initialDir = "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        // get directory listView
        filelv = (ListView) findViewById(R.id.fileList);
        filelv.setAdapter(new DirectoryAdapter(initialDir));

        // get the titlebar textview
        final TextView tvCurDir = (TextView) findViewById(R.id.textCurDir);

        // set the onClick listener
        filelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // add current state to the backstack
//                getSupportFragmentManager().beginTransaction()
//                        .add(details)
//                        .addToBackStack()
//                        .commit();
                // get new directory
                String s = (String) parent.getItemAtPosition(position);
                // set new directory
                DirectoryAdapter da = (DirectoryAdapter) parent.getAdapter();
                da.selectDirectory(s, tvCurDir);
                // scroll to top
                parent.setSelection(0);
            }
        });

        // set initial directory to be displayed
        tvCurDir.setText(initialDir);
    }

    @Override
    public void onBackPressed() {
        // get the titlebar textview
        final TextView tvCurDir = (TextView) findViewById(R.id.textCurDir);
        // go up a directory
        DirectoryAdapter da = (DirectoryAdapter) filelv.getAdapter();
        boolean hasParent = da.upDirectory(tvCurDir);
        // exit if no parent directory
        if (!hasParent) {
//            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//            homeIntent.addCategory( Intent.CATEGORY_HOME );
//            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(homeIntent);
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        // scroll to top
        filelv.setSelection(0);
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
