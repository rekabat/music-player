package com.audio.tyger.tygeraudio;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
//import java.util.Collection;

/**
 * Created by troy on 3/17/17.
 */

public class DirectoryAdapter extends BaseAdapter {

    private String currentPath;
    private ArrayList<String> dirs;

    public DirectoryAdapter(String path) {
        currentPath = path;
        dirs = getDirectories(path);
    }

    public void selectDirectory(String dir, TextView displayDir) {
        updatePath(currentPath+dir, displayDir);
    }

    public boolean upDirectory(TextView displayDir) {
        if (currentPath.equals("/"))
            return false;
        updatePath(getParentDirectoryPath(currentPath), displayDir);
        return true;
    }

    public void updatePath(String path, TextView displayDir) {
        currentPath = path;
        dirs = getDirectories(currentPath);
        updateDisplayedDirectory(displayDir);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dirs.size();
    }

    @Override
    public Object getItem(int position) {
        return dirs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.listview_item_directory, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.lv_item_dir_text);

        final String s = dirs.get(position);
        tv.setText(s);

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




    private void updateDisplayedDirectory(TextView directoryDisplay) {
        directoryDisplay.setText(getCurrentDirectoryName(currentPath));
    }

    private String getCurrentDirectoryName(String path) {
        String s = currentPath.substring(0, currentPath.length()-1);
        s = s.substring(s.lastIndexOf("/")+1);
        if (s.length() == 0) s = "/";
        return s;
    }

    private String getParentDirectoryPath(String path) {
        String s = path.substring(0, path.length()-1);
        s = s.substring(0, s.lastIndexOf("/")+1);
        return s;
    }

    private ArrayList<String> getDirectories(String path) {
        ArrayList<String> ret = new ArrayList<String>();

        // get file list
        File f = new File(path);
        File[] files = f.listFiles();

        if (files != null) {

            // get all directories
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    // is directory
                    String s = inFile.toString();
                    s = s.substring(path.length());
                    ret.add(s);
                }
            }

            // alphabetize
            java.util.Collections.sort(ret, java.text.Collator.getInstance());

            // append "/" to all directories. Must be done afterwards for alphabetizing purposes
            for (ListIterator s = ret.listIterator(); s.hasNext(); )
                s.set(s.next() + "/");
        }

        return ret;
    }
}
