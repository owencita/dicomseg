package edu.unicen.project.dicomseg;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

        final ListView listView = (ListView) findViewById(android.R.id.list);

        String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        dicomDir = new File(externalStorage + "/dicom/");

        fill(dicomDir);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File dicom = (File) listView.getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(), DicomViewActivity.class);
                intent.putExtra("dicom", dicom);
                view.getContext().startActivity(intent);
            }
        });
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
