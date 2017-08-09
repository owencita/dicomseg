package edu.exa.unicen.dicomseg.activities;

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

import edu.exa.unicen.dicomseg.R;
import edu.exa.unicen.dicomseg.adapters.ListedFileArrayAdapter;

public class FileChooserActivity extends ListActivity {

    private static final String TAG = "FileChooserActivity";

    private ListedFileArrayAdapter adapter;
    private static final String DICOM_FOLDER = "/dicom/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        final ListView listView = (ListView) findViewById(android.R.id.list);

        String primaryStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dicomDirInternal = new File(primaryStorage + DICOM_FOLDER);
        if (dicomDirInternal.listFiles() != null) {
            fill(dicomDirInternal.listFiles());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File dicom = (File) listView.getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(), SelectDicomImageActivity.class);
                intent.putExtra("dicom", dicom);
                view.getContext().startActivity(intent);
            }
        });
    }

    private void fill(File[] content) {
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
