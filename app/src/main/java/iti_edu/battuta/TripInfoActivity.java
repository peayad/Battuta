package iti_edu.battuta;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;

public class TripInfoActivity extends AppCompatActivity {

    TextView titleTV, startTV, endTV, dateTimeTV;
    Button startBtn, editBtn, deleteBtn;

    final private int TRIP_EDIT_REQUEST = 0;

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
                Uri directionsURI = getDirectionsURI();
                Intent goMapsIntent = new Intent(android.content.Intent.ACTION_VIEW, directionsURI);
                startActivity(goMapsIntent);
            }
        });

        Button editBtn = (Button) findViewById(R.id.info_editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditTripActivity.class);
                intent.putExtra("trip", (Serializable) trip);
                startActivity(intent);
                finish();
            }
        });


        Button deleteBtn = (Button) findViewById(R.id.info_deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(TripInfoActivity.this);
                a_builder.setMessage("Do you want to Delete this Trip !!!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Alert !!!");
                alert.show();
            }
        });

        Button infoshare = (Button) findViewById(R.id.info_share_infoBtn);
        infoshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, trip.getTitle() + "\n" + trip.getDateTime() + "\n" + getDirectionsURI());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share Trip"));

            }
        });

        Button reverseBtn = (Button) findViewById(R.id.info_reverseBtn);
        reverseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTV.setText(trip.getEndPoint());
                endTV.setText(trip.getStartPoint());
                trip.set_infoReverse();
            }
        });

        Switch tripDone = (Switch) findViewById(R.id.info_switch);
        tripDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO change trip to isDone
                finish();
            }
        });

    }


    Uri getDirectionsURI() {
        /*
            the map http link format should be like this:
            http://maps.google.com/maps?saddr= lat,long &daddr= lat,long
        */
        return Uri.parse("https://www.google.com/maps?saddr=Current+Location&daddr=Cairo");
    }

}
