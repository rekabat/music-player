package com.audio.tyger.tygeraudio;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


//$base03:    #002b36; Background
//$blue:      #268bd2; Dir font
//$base00:    #657b83; File font


public class BrowserActivity extends AppCompatActivity { //implements OnItemClickListener  {

    // permissions related
    private final int MY_REQUEST_CODE = 18750443; // just some random number for permission requests

    // browser related
    private ListView browserlv;
//    private final String initialDir = "/";
    private final String initialDir = "/storage/19F2-330E/Music";
    private Stack<Parcelable> scrollPositions = new Stack<>();

    // audio service related
    private AudioService audioSrv;
    private boolean audioSrvBound;
    private Queue<String> playlist;
    private Intent audioSrvIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        // permissions
        boolean permissionsGranted = checkPermissions();

        // init calls
        initAudioService();
        initBrowserLV();
        initControls();
        initTitleBar();
    }

    @Override
    protected void onDestroy() {
//        stopAudioService();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // TODO: use only a stack, and exit program if stack exhausted

        // get the titlebar textview
        final TextView tvCurDir = (TextView) findViewById(R.id.textCurDir);
        // go up a directory
        DirectoryAdapter da = (DirectoryAdapter) browserlv.getAdapter();
        boolean hasParent = da.upDirectory();
        // exit if no parent directory
        if (!hasParent) {
//            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//            homeIntent.addCategory( Intent.CATEGORY_HOME );
//            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(homeIntent);

            // stop audio service
//            stopAudioService();

            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        // update list
        da.updateDisplay();
        // get directory listview
        browserlv.onRestoreInstanceState(scrollPositions.peek());
        scrollPositions.pop();

        // update title bar display
        setTitleBarText(PathParser.getDirectoryName(da.getCurrentDirectory()));
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




    private boolean checkPermissions() {
        // TODO: handle a denied permission. It shouldn't matter, they can just read from internal storage
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        return true;
    }

    private void initAudioService() {
        audioSrv = new AudioService();
        audioSrvBound = false;

        if(audioSrvIntent == null){
            audioSrvIntent = new Intent(this, AudioService.class);
            bindService(audioSrvIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(audioSrvIntent);
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioService.AudioBinder binder = (AudioService.AudioBinder) service;
            //get service
            audioSrv = binder.getService();
            //pass list
            audioSrvBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioSrvBound = false;
        }
    };

    private void initBrowserLV() {
        // get directory listView
        browserlv = (ListView) findViewById(R.id.fileList);
        browserlv.setAdapter(new DirectoryAdapter(initialDir));

        // set the onClick listener
        browserlv.setOnItemClickListener(browserOnClick);
    }

    private AdapterView.OnItemClickListener browserOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // get adapter
            DirectoryAdapter da = (DirectoryAdapter) parent.getAdapter();

            if (da.isDirectory(position)) {
                // save the old position for going back
                scrollPositions.push(browserlv.onSaveInstanceState());

                // get directory name
                String s = (String) parent.getItemAtPosition(position);
                // set new directory
                da.selectChildDirectory(s);
                // update the list
                da.updateDisplay();

                // scroll to top
                parent.setSelection(0);
                // update title bar display
                setTitleBarText(PathParser.getDirectoryName(da.getCurrentDirectory()));

            } else { // it's a file
                // send to service
                audioSrv.setPlaylist(
                        da.getFilesBefore(position),
                        da.getPath(position),
                        da.getFilesAfter(position));

                // play
                audioSrv.playCurrentTrack();
            }
        }
    };

    private void initControls() {
        // prev button
        findViewById(R.id.buttonPrev).setOnClickListener(
                new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        audioSrv.prev();
                    }
                });

        // next button
        findViewById(R.id.buttonNext).setOnClickListener(
                new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        audioSrv.next();
                    }
                });

        // rewind button
        findViewById(R.id.buttonRewind).setOnClickListener(
                new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        audioSrv.rewind(5);
                    }
                });


        // ff button
        findViewById(R.id.buttonForward).setOnClickListener(
                new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        audioSrv.fastforward(5);
                    }
                });

        // play button
        findViewById(R.id.togglePausePlay).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] states = v.getDrawableState();
                        boolean checked = false;
                        for (int s : states) {
                            if (s == android.R.attr.state_checked) {
                                checked = true;
                            }
                        }
                        if (checked) {
                            audioSrv.play();
                        } else {
                            audioSrv.pause();
                        }
                    }
                }
        );
    }



    private void initTitleBar() {
        // set initial directory to be displayed
        setTitleBarText(PathParser.getDirectoryName(initialDir));
    }

    private void setTitleBarText(String title) {
        // get the titlebar textview
        final TextView tvCurDir = (TextView) findViewById(R.id.textCurDir);
        // set text
        tvCurDir.setText(title);
    }

    private void stopAudioService() {
        stopService(audioSrvIntent);
        audioSrv = null;
    }




    private void setColorTheme(int background, int fontDir, int fontFile) {
        // set color scheme
//        int background = 0x002b36;
//        int fontDir    = 0x268bd2;
//        int fontFile   = 0x657b83;
////        setColorTheme(background, fontDir, fontFile);
//        setColorTheme(Color.parseColor("#002b36"), fontDir, fontFile);


        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(background);
//        view.set

    }

//    private int rgbToInt(int r, int g, int b) {
//        return ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
//    }

}
