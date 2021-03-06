package iti_edu.battuta;

import android.view.View;
import android.widget.TextView;

public class ViewCache {

    private View baseView;
    private TextView titleTV, endTV, dateTimeTV;

    public View getBaseView(){
        return baseView;
    }

    public TextView getTitleTV(){
        if(titleTV == null) titleTV = (TextView) baseView.findViewById(R.id.tvTitle);
        return titleTV;
    }

    public TextView getDateTimeTV() {
        if(dateTimeTV == null) dateTimeTV = (TextView) baseView.findViewById(R.id.tvDate);
        return dateTimeTV;
    }

    public TextView getEndTV() {
        if(endTV == null) endTV = (TextView) baseView.findViewById(R.id.tvEndPoint);
        return endTV;
    }

    public ViewCache(View baseView){
        this.baseView = baseView;
    }
}
