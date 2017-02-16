package iti_edu.battuta;

import android.view.View;
import android.widget.TextView;

public class ViewCache {

    private View baseView;
    private TextView titleTV, startTV, endTV, dateTV, timeTV;

    public View getBaseView(){
        return baseView;
    }

    public TextView getTitleTV(){
        if(titleTV == null) titleTV = (TextView) baseView.findViewById(R.id.tvTitle);
        return titleTV;
    }

    public TextView getDateTV() {
        if(dateTV == null) dateTV = (TextView) baseView.findViewById(R.id.tvDate);
        return dateTV;
    }

    public TextView getEndTV() {
        if(endTV == null) endTV = (TextView) baseView.findViewById(R.id.tvEndPoint);
        return endTV;
    }

    public TextView getStartTV() {
        if(startTV == null) startTV = (TextView) baseView.findViewById(R.id.tvStartPoint);
        return startTV;
    }

    public TextView getTimeTV() {
        if(timeTV == null) timeTV = (TextView) baseView.findViewById(R.id.tvTime);
        return timeTV;
    }

    public ViewCache(View baseView){
        this.baseView = baseView;
    }
}
