package com.audio.tyger.tygeraudio;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by troy on 3/20/17.
 */

public class PathParser {

    static public String getCurrentDirectoryName(String path) {
        String s = path.substring(0, path.length()-1);
        s = s.substring(s.lastIndexOf("/")+1);
        if (s.length() == 0) s = "/";
        return s;
    }

    static public String getParentDirectoryPath(String path) {
        String s = path.substring(0, path.length()-1);
        s = s.substring(0, s.lastIndexOf("/")+1);
        return s;
    }

    static public ArrayList<String> getDirectories(String path) {
        ArrayList<String> ret = new ArrayList<String>();

        // get file list
        File f = new File(path);
        File[] files = f.listFiles();

        if (files != null) {

            // get all directories
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    String s = inFile.toString();
                    s = s.substring(path.length());
                    ret.add(s);
                }
            }

            alphabetizeList(ret);

            // append "/" to all directories. Must be done afterwards for alphabetizing purposes
            for (ListIterator s = ret.listIterator(); s.hasNext(); )
                s.set(s.next() + "/");
        }

        return ret;
    }

    static public ArrayList<String> getFiles(String path) {
        ArrayList<String> ret = new ArrayList<String>();

        // get file list
        File f = new File(path);
        File[] files = f.listFiles();

        if (files != null) {

            // get all files
            for (File inFile : files) {
                if (inFile.isFile()) {
                    String s = inFile.toString();
                    s = s.substring(path.length());
                    ret.add(s);
                }
            }

            alphabetizeList(ret);
        }

        return ret;
    }

    static public ArrayList<String> getFilesOfType(String path, ArrayList<String> extensions) {
        ArrayList<String> ret = new ArrayList<String>();

        // get file list
        File f = new File(path);
        File[] files = f.listFiles();

        if (files != null) {

            // get all files
            for (File inFile : files) {
                if (inFile.isFile()) {
                    String s = inFile.toString();
                    if (isFileOfType(s, extensions)) {
                        s = s.substring(path.length());
                        ret.add(s);

                    }
                }
            }

            alphabetizeList(ret);
        }

        return ret;
    }

    static private void alphabetizeList(ArrayList<String> strings) {
        java.util.Collections.sort(strings, java.text.Collator.getInstance());
    }

    static private boolean isFileOfType(String path, ArrayList<String> extensions) {
        String extension = getFileExtension(path);
        Log.d("Extension", extension);
        extension = extension.toLowerCase();
        for(String str: extensions) {
            Log.d("Valid ext", str);
            if(str.trim().contains(extension))
                return true;
        }
        return false;
    }

    static private String getFileExtension(String name) {
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
}
