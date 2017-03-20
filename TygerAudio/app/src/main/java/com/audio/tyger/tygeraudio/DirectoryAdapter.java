package com.audio.tyger.tygeraudio;

import android.graphics.Path;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
//import java.util.Collection;

/**
 * Created by troy on 3/17/17.
 */

public class DirectoryAdapter extends BaseAdapter {

    final private ArrayList<String> musicExtensions = new ArrayList<>();

    private String currentPath;
    private ArrayList<String> dirs;
    private ArrayList<String> files;

    public DirectoryAdapter(String path) {
        musicExtensions.addAll(Arrays.asList(
                "mp3", "flac"));

        currentPath = path;
        dirs = PathParser.getDirectories(path);
        files = PathParser.getFilesOfType(path, musicExtensions);
    }

    @Override
    public int getCount() {
        return dirs.size() + files.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < dirs.size())
            return dirs.get(position);
        else {
            position -= dirs.size();
            return files.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get view format from xml
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.listview_item_directory, parent, false);
        }

        // get textview
        TextView tv = (TextView) convertView.findViewById(R.id.lv_item_dir_text);

        // set the display string
        final String s = (String) getItem(position);
        tv.setText(s);

        // set the font weight (bold if dir, normal if file)
        if (isDirectory(position))
            tv.setTextColor(ContextCompat.getColor(convertView.getContext(), R.color.colorFontPrimary));
        else
            tv.setTextColor(ContextCompat.getColor(convertView.getContext(), R.color.colorFontSecondary));
        //            tv.setTypeface(null, Typeface.BOLD);\


//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                view.get
//                Log.d("click", s);
////                Toast.makeText(parent.getContext(), "view clicked: " + dataModel.getOtherData(), Toast.LENGTH_SHORT).show();
//            }
//        });

        return convertView;
    }




    public void selectDirectory(String dir, TextView displayDir) {
        updatePath(currentPath+dir, displayDir);
    }

    public boolean upDirectory(TextView displayDir) {
        if (currentPath.equals("/"))
            return false;
        updatePath(PathParser.getParentDirectoryPath(currentPath), displayDir);
        return true;
    }

    public void updatePath(String path, TextView displayDir) {
        currentPath = path;
        dirs = PathParser.getDirectories(currentPath);
        files = PathParser.getFilesOfType(path, musicExtensions);

        updateDisplayedDirectory(displayDir);
        notifyDataSetChanged();
    }

    public boolean isDirectory(int position) {
        return (position < dirs.size());
    }




    private void updateDisplayedDirectory(TextView directoryDisplay) {
        directoryDisplay.setText(PathParser.getCurrentDirectoryName(currentPath));
    }

}
