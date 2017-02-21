package iti_edu.battuta;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

public class EditTripActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ptr-editTrip";
    private static final int PICK_START = 1, PICK_END = 2;

    private GoogleApiClient mGoogleApiClient;

    private Place startPlace, endPlace;

    private EditText titleET, dateTimeET, notesET;
    private Switch isRoundSwitch;

    private Calendar calendar;
    private String dateTimeStr;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    private int tripYear, tripMonth, tripDay, tripHour, tripMinute;


    @RequiresApi(api = Build.VERSION_CODES.N)
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

        initEditTexts();
        initPlaceFragments();
        initDateTime();
        initButtons();
    }

    void initEditTexts() {
        titleET = (EditText) findViewById(R.id.edit_title);
        notesET = (EditText) findViewById(R.id.edit_notes);
        isRoundSwitch = (Switch) findViewById(R.id.switch_isRound);
    }

    void initPlaceFragments() {
        SupportPlaceAutocompleteFragment startLocationFrag = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.start_place_fragment);
        startLocationFrag.setOnPlaceSelectedListener(new MyPlaceSelectionListener(PICK_START));

        SupportPlaceAutocompleteFragment endLocationFrag = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.end_place_fragment);
        endLocationFrag.setOnPlaceSelectedListener(new MyPlaceSelectionListener(PICK_END));
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

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, myDateListener, tripYear, tripMonth, tripDay);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeDate, tripHour, tripMinute, false);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int year, int month, int day) {
                    dateTimeStr = day + "/" + month + "/" + year;
                    showDialog(TIME_DIALOG_ID);
                }
            };

    private TimePickerDialog.OnTimeSetListener timeDate = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hours, int minutes) {
            dateTimeStr = dateTimeStr + "   " + hours + ":" + minutes;
            dateTimeET.setText(dateTimeStr);
        }
    };


    void initButtons() {
        Button startTripBtn = (Button) findViewById(R.id.edit_btnStartTrip);
        startTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startPlace == null) {
                    Toast.makeText(getApplicationContext(), "Please, select your starting point", Toast.LENGTH_SHORT).show();
                } else if (endPlace == null) {
                    Toast.makeText(getApplicationContext(), "Please, select your destination", Toast.LENGTH_SHORT).show();
                } else {
                    Uri directionsURI = getDirectionsURI();
                    Intent intent = new Intent(Intent.ACTION_VIEW, directionsURI);
                    startActivity(intent);
                }
            }
        });

        Button saveTripBtn = (Button) findViewById(R.id.edit_btnSaveTrip);
        saveTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleET.getText().toString() != null) {

                    addTripData();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Please, enter trip title", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    Uri getDirectionsURI() {
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



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDateTime(){
        dateTimeET = (EditText) findViewById(R.id.edit_date_time);
        calendar = Calendar.getInstance();
        tripYear = calendar.get(Calendar.YEAR);
        tripMonth = calendar.get(Calendar.MONTH);
        tripDay = calendar.get(Calendar.DAY_OF_MONTH);
        tripHour = calendar.get(Calendar.HOUR_OF_DAY);
        tripMinute = calendar.get(Calendar.MINUTE);

        dateTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    private Trip getTripData(){
        String tTitle =  titleET.getText().toString() + "";
        String tStart = startPlace != null ? startPlace.getAddress().toString() + "": "";
        String tEnd = endPlace != null ? endPlace.getAddress().toString() + "": "";
        String tDateTime = dateTimeStr + "";
        int tIsRound = isRoundSwitch.isChecked() ? 1 : 0;
        String tNotes = notesET.getText().toString() + "";

        return new Trip(tTitle,tStart,tEnd,tDateTime,tIsRound,tNotes);
    }

    private void addTripData(){
        BattutaDBadapter mDBhelper = new BattutaDBadapter(getApplicationContext());
        mDBhelper.insertTrip(getTripData());
    }
}
