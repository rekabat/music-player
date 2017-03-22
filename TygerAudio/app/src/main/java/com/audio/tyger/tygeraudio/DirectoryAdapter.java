package com.audio.tyger.tygeraudio;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
//import java.util.Collection;

/**
 * Created by troy on 3/17/17.
 */

public class DirectoryAdapter extends BaseAdapter {

    final private ArrayList<String> musicExtensions = new ArrayList<>();

    private String currentDirPath; // terminated with forward slash
    private ArrayList<String> dirs; // terminated with forward slash
    private ArrayList<String> files;

    public DirectoryAdapter(String path) {
        musicExtensions.addAll(Arrays.asList(
                "mp3", "flac", "wav", "ogg"));

        setCurrentDirectory(path);
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



    public boolean isDirectory(int position) {
        return (position < dirs.size());
    }

    public String getCurrentDirectory() {
        return currentDirPath;
    }

    public String getPath(int position) {
        return currentDirPath + getItem(position);
    }

    private void setCurrentDirectory(String path) {
        // append a "/" if none present
        if (path.lastIndexOf("/") != path.length() - 1)
            path += "/";

        currentDirPath = path;
        dirs = PathParser.getDirectories(path);
        files = PathParser.getFilesOfType(path, musicExtensions);
    }

    public void selectChildDirectory(String dir) {
        setCurrentDirectory(currentDirPath + dir);
    }

    public boolean upDirectory() {
        if (currentDirPath.equals("/"))
            return false;

        setCurrentDirectory(PathParser.getParentDirectoryPath(currentDirPath));

        return true;
    }

    public void updateDisplay() {
        notifyDataSetChanged();
    }

    public LinkedList<String> getFilesBefore(int position) {
        LinkedList<String> ret = new LinkedList<>();
        for (int i = dirs.size(); i < position; i++) {
            ret.addLast(getPath(i));
        }
        return ret;
    }

    public LinkedList<String> getFilesAfter(int position) {
        LinkedList<String> ret = new LinkedList<>();
        for (int i = position+1; i < dirs.size() + files.size(); i++) {
            ret.addLast(getPath(i));
        }
        return ret;
    }
}
