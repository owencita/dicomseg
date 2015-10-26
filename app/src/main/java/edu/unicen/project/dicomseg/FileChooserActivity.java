package edu.unicen.project.dicomseg;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.unicen.project.dicomseg.adapters.ListedFileArrayAdapter;

public class FileChooserActivity extends ListActivity {

    private static final String TAG = "FileChooserActivity";

    private File dicomDir;
    private ListedFileArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.i(TAG, "Root Dir: " + externalStorage);
        dicomDir = new File(externalStorage + "/dicom/");

        Log.i(TAG, "Dicom Dir: " + dicomDir.getAbsolutePath());
        Log.i(TAG, "Dicom Dir exists: " + String.valueOf(dicomDir.exists()));
        Log.i(TAG, "Dicom Dir is directory: " + String.valueOf(dicomDir.isDirectory()));
        Log.i(TAG, "Dicom Dir can read: " + String.valueOf(dicomDir.canRead()));

        fill(dicomDir);
    }

    private void fill(File f) {
        File[] content = f.listFiles();
        this.setTitle("Current Dir: " + f.getName());
        List<File> dirs = new ArrayList<File>();
        List<File> files = new ArrayList<File>();
        try {
            for (File item: content) {
                if (item.isDirectory()) {
                    dirs.add(item);
                }
                else {
                    files.add(item);
                }
            }
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }

        Collections.sort(dirs);
        Collections.sort(files);
        dirs.addAll(files);

        adapter = new ListedFileArrayAdapter(FileChooserActivity.this, R.layout.content_file_chooser, dirs);
        this.setListAdapter(adapter);
    }

}
