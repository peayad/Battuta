package iti_edu.battuta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TripInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);

        Intent intent = getIntent();
        if (intent == null) return;

        String title = intent.getStringExtra("title");
        TextView tv;

        tv = (TextView) findViewById(R.id.info_titleTV);
        tv.setText(title);

        tv = (TextView) findViewById(R.id.info_startTV);
        tv.setText("stPt: " + title);

        tv = (TextView) findViewById(R.id.info_endTV);
        tv.setText("edPt: " + title);

        tv = (TextView) findViewById(R.id.info_dateTimeTV);
        tv.setText("date/time: " + title);

        Button deleteBtn = (Button) findViewById(R.id.info_deleteTripBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
