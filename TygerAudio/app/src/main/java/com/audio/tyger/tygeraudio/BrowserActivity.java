package com.audio.tyger.tygeraudio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;
import java.util.Stack;


//$base03:    #002b36; Background
//$blue:      #268bd2; Dir font
//$base00:    #657b83; File font


public class BrowserActivity extends AppCompatActivity { //implements OnItemClickListener  {

    private ListView filelv;
    final private String initialDir = "/";

    private Stack<Parcelable> scrollPositions = new Stack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        // set color scheme
//        int background = 0x002b36;
//        int fontDir    = 0x268bd2;
//        int fontFile   = 0x657b83;
////        setColorTheme(background, fontDir, fontFile);
//        setColorTheme(Color.parseColor("#002b36"), fontDir, fontFile);

        // get directory listView
        filelv = (ListView) findViewById(R.id.fileList);
        filelv.setAdapter(new DirectoryAdapter(initialDir));

        // get the titlebar textview
        final TextView tvCurDir = (TextView) findViewById(R.id.textCurDir);

        // set initial directory to be displayed
        tvCurDir.setText(initialDir);

        // set the onClick listener
        filelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get adapter
                DirectoryAdapter da = (DirectoryAdapter) parent.getAdapter();
                // get clicked item
                // TODO: no reason to get string, just pass along the position
                String s = (String) parent.getItemAtPosition(position);

                if (da.isDirectory(position)) {
                    // save the old position for going back
                    scrollPositions.push(filelv.onSaveInstanceState());
                    Log.d("Scroll pos click", Integer.toString(parent.getScrollY()));

                    // set new directory
                    da.selectDirectory(s, tvCurDir);
                    // scroll to top
                    parent.setSelection(0);

                } else { // it's a file
                    // open file
                }
            }
        });

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
//        filelv.setSelection(0);

        // get directory listview
        filelv.onRestoreInstanceState(scrollPositions.peek());
//        Log.d("Scroll pos back", Integer.toString(scrollPositions.peek()));
        scrollPositions.pop();
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



    private void setColorTheme(int background, int fontDir, int fontFile) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(background);
//        view.set

    }

//    private int rgbToInt(int r, int g, int b) {
//        return ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
//    }

}
