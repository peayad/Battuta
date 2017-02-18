package iti_edu.battuta;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.util.Date;


public class EditTripActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ptr";
    private static final int PICK_START = 1, PICK_END = 2;

    private GoogleApiClient mGoogleApiClient;

    private Place startPlace, endPlace;
    private EditText dateTimeET;

    private int tripYear, tripMonth, tripDay, tripHour, tripMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        SupportPlaceAutocompleteFragment startLocationFrag = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.start_place_fragment);
        startLocationFrag.setOnPlaceSelectedListener(new MyPlaceSelectionListener(PICK_START));

        SupportPlaceAutocompleteFragment endLocationFrag = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.end_place_fragment);
        endLocationFrag.setOnPlaceSelectedListener(new MyPlaceSelectionListener(PICK_END));

        Button startTripBtn = (Button) findViewById(R.id.edit_btnStartTrip);
        startTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startPlace == null) {
                    Toast.makeText(getApplicationContext(), "Please, select your starting point", Toast.LENGTH_SHORT).show();
                } else if (endPlace == null) {
                    Toast.makeText(getApplicationContext(), "Please, select your destination", Toast.LENGTH_SHORT).show();
                } else {
                    Uri directionsURI = getDirectionsURI(startPlace, endPlace);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, directionsURI);
                    startActivity(intent);
                }
            }
        });


        dateTimeET = (EditText) findViewById(R.id.edit_date_time);
        dateTimeET.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                startDateDialog();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void startTimeDialog(){
        TimePickerDialog timePicker = new TimePickerDialog(EditTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                tripHour = selectedHour;
                tripMinute = selectedMinute;

                Date selectedDate = new Date(tripYear, tripMonth, tripDay, tripHour, tripMinute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YY hh:mm a");
                String dateString = dateFormat.format(selectedDate);

                dateTimeET.setText(dateString);

                Log.i(TAG, dateString);
            }
        }, Calendar.HOUR, Calendar.MINUTE, false);
        timePicker.setTitle("Select Time");
        timePicker.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void startDateDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditTripActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                tripYear = selectedYear;
                tripMonth = selectedMonth;
                tripDay = selectedDay;
                startTimeDialog();
            }
        }, 2017,Calendar.MONTH,Calendar.DAY_OF_MONTH );
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    Uri getDirectionsURI(Place sPlace, Place ePlace) {
        /*
            the map http link format should be like this:
            http://maps.google.com/maps?saddr= lat,long &daddr= lat,long
        */
        String startLatLng = startPlace.getLatLng().latitude + "," + startPlace.getLatLng().longitude;
        String endLatLng = endPlace.getLatLng().latitude + "," + endPlace.getLatLng().longitude;
        return Uri.parse("http://maps.google.com/maps?saddr=" + startLatLng + "&daddr=" + endLatLng);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Cannot connect to Google Places!", Toast.LENGTH_SHORT).show();
    }

    class MyPlaceSelectionListener implements PlaceSelectionListener {

        int picker;

        MyPlaceSelectionListener(int picker) {
            this.picker = picker;
        }

        @Override
        public void onPlaceSelected(Place place) {
            if (picker == PICK_START) {
                startPlace = place;
            } else if (picker == PICK_END) {
                endPlace = place;
            }
            Log.i(TAG, "Place: " + place.getName());
        }

        @Override
        public void onError(Status status) {
            Log.i(TAG, "An error occurred: " + status);
        }
    }
}
