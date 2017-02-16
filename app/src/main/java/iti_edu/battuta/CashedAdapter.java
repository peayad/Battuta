package iti_edu.battuta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CashedAdapter extends ArrayAdapter {

    Context context;
    String[] values;

    public CashedAdapter(Context context, String[] values) {
        super(context, R.layout.list_item, R.id.tvTitle, values);
        this.values = values;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewCache viewCache;
        final int positionCopy = position;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item, parent, false);

            viewCache = new ViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) rowView.getTag();
        }

        viewCache.getTitleTV().setText("Trip num: " + values[position]);
        viewCache.getStartTV().setText("stPt num: " + values[position]);
        viewCache.getEndTV().setText("edPt num: " + values[position]);
        viewCache.getDateTV().setText("date num: " + values[position]);
        viewCache.getTimeTV().setText("time num: " + values[position]);

        return viewCache.getBaseView();
    }
}
