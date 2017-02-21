package iti_edu.battuta;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TripInfoActivity extends AppCompatActivity {

    TextView titleTV, startTV, endTV, dateTimeTV;
    Button startBtn, editBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_trip_dialog);

        Intent sourceIntent = getIntent();
        final Trip trip = (Trip) sourceIntent.getSerializableExtra("trip");

        titleTV = (TextView) findViewById(R.id.info_titleTV);
        titleTV.setText(trip.getTitle());

        startTV = (TextView) findViewById(R.id.info_startAddressTV);
        startTV.setText(trip.getStartPoint());

        endTV = (TextView) findViewById(R.id.info_destinationTV);
        endTV.setText(trip.getEndPoint());

        dateTimeTV = (TextView) findViewById(R.id.info_dateTimeTV);
        dateTimeTV.setText(trip.getDateTime());


        Button startBtn = (Button) findViewById(R.id.info_startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri directionsURI = getDirectionsURI();
//                Intent goMapsIntent = new Intent(android.content.Intent.ACTION_VIEW, directionsURI);
//                startActivity(goMapsIntent);
            }
        });

        Button editBtn = (Button) findViewById(R.id.info_editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent editIntent = new Intent(getApplicationContext(), EditTripActivity.class);
//                editIntent.putExtra("title", title);
//                startActivity(editIntent);
//                finish();
            }
        });


        Button deleteBtn = (Button) findViewById(R.id.info_deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setResult(RESULT_OK);
//                finish();
            }
        });



    }


    Uri getDirectionsURI() {
        /*
            the map http link format should be like this:
            http://maps.google.com/maps?saddr= lat,long &daddr= lat,long
        */
        return Uri.parse("https://www.google.com/maps/dir/Current+Location/Cairo");
    }

}
