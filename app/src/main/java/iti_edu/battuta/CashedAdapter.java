package iti_edu.battuta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class CashedAdapter extends ArrayAdapter<Trip> {

    Context context;
    ArrayList<Trip> tripsList;

    public CashedAdapter(Context context, ArrayList<Trip> tripsList) {
        super(context, R.layout.list_item, R.id.tvTitle, tripsList);
        this.tripsList = tripsList;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewCache viewCache;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item, parent, false);

            viewCache = new ViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) rowView.getTag();
        }

        Trip mTrip = tripsList.get(position);

        viewCache.getTitleTV().setText(mTrip.getTitle());
        viewCache.getStartTV().setText(mTrip.getStartPoint());
        viewCache.getEndTV().setText(mTrip.getEndPoint());
        viewCache.getDateTimeTV().setText(mTrip.getDateTime());

        return viewCache.getBaseView();
    }
}
