package edu.unicen.project.dicomseg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import edu.unicen.project.dicomseg.R;

public class ListedFileArrayAdapter extends ArrayAdapter<File> {

    private Context c;
    private int id;
    private List<File> items;

    public ListedFileArrayAdapter(Context context, int textViewResourceId, List<File> items) {
        super(context, textViewResourceId, items);
        this.c = context;
        this.id = textViewResourceId;
        this.items = items;
    }

    public File getItem(int i) {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        final File item = items.get(position);

        if (item != null) {
            TextView nameView = (TextView) v.findViewById(R.id.NameView);
            TextView sizeView = (TextView) v.findViewById(R.id.SizeView);

            if (nameView != null) {
                nameView.setText(item.getName());
            }
            if (sizeView != null) {
                sizeView.setText("Size: " + String.valueOf(item.length()));
            }
        }
        return v;
    }
}
