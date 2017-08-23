package edu.exa.unicen.dicomseg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.exa.unicen.dicomseg.R;
import edu.exa.unicen.dicomseg.segmentation.Segmentation;


public class ListedSegmentationArrayAdapter extends ArrayAdapter<Segmentation> {

    private Context context;
    private int id;
    private List<Segmentation> items;

    public ListedSegmentationArrayAdapter(Context context, int textViewResourceId, List<Segmentation> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.id = textViewResourceId;
        this.items = items;
    }

    public Segmentation getItem(int i) {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        final Segmentation item = items.get(position);

        if (item != null) {
            TextView segmentationView = (TextView) v.findViewById(R.id.segmentationView);

            if (segmentationView != null) {
                segmentationView.setText(item.getType().getName());
            }
        }
        return v;
    }

}
