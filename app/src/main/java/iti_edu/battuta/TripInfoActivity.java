package iti_edu.battuta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TripInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);

        Intent intent = getIntent();
        if (intent == null) return;

        int position = intent.getIntExtra("id", 9999);
        TextView tv;
        tv = (TextView) findViewById(R.id.info_edTitle);
        tv.setText("Trip num: " + position);

        tv = (TextView) findViewById(R.id.info_start);
        tv.setText("stPt num: " + position);

        tv = (TextView) findViewById(R.id.info_end);
        tv.setText("edPt num: " + position);

        tv = (TextView) findViewById(R.id.info_date);
        tv.setText("date num: " + position);

        tv = (TextView) findViewById(R.id.info_time);
        tv.setText("time num: " + position);

    }
}
