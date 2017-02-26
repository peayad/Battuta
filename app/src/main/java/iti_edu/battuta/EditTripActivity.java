package iti_edu.battuta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.net.Uri;
import android.support.annotation.NonNull;
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


    private BattutaDBadapter mDBhelper;
    private GoogleApiClient mGoogleApiClient;

    private Place startPlace, endPlace;
    private String startAddress, endAddress;

    private EditText titleET, dateTimeET, notesET;
    private SupportPlaceAutocompleteFragment startFrag, endFrag;
    private Switch isRoundSwitch;
    private Button saveTripBtn;

    private Calendar calendar;
    private String dateTimeStr;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    private int tripYear, tripMonth, tripDay, tripHour, tripMinute;

    private boolean isEditingTrip;
    private Trip editedTrip;

    String aa = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        mDBhelper = new BattutaDBadapter(getApplicationContext());

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
        checkSaveOrEdit();
    }

    void initEditTexts() {
        titleET = (EditText) findViewById(R.id.edit_title);
        notesET = (EditText) findViewById(R.id.edit_notes);
        isRoundSwitch = (Switch) findViewById(R.id.switch_isRound);
    }

    void initPlaceFragments() {
        startFrag = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.edit_start_place_fragment);
        startFrag.setOnPlaceSelectedListener(new MyPlaceSelectionListener(PICK_START));

        endFrag = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.edit_end_place_fragment);
        endFrag.setOnPlaceSelectedListener(new MyPlaceSelectionListener(PICK_END));
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
                startAddress = startPlace.getAddress().toString();
            } else if (picker == PICK_END) {
                endPlace = place;
                endAddress = endPlace.getAddress().toString();
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
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    dateTimeStr = day + "/" + month + "/" + year;
                    showDialog(TIME_DIALOG_ID);
                }
            };

    private TimePickerDialog.OnTimeSetListener timeDate = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {

            aa = hourOfDay > 11 ? "pm" : "am";
            String timeStr = String.valueOf(hourOfDay) + ":" + String.valueOf(minutes);
            DateFormat sdf = new SimpleDateFormat("hh:mm");

            try {
                Date date = sdf.parse(timeStr);
                dateTimeStr += "  " + sdf.format(date) + " " + aa;
            } catch (ParseException e) {
                e.printStackTrace();
            }

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

        saveTripBtn = (Button) findViewById(R.id.edit_btnSaveTrip);
        saveTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasNotEnteredAllDetails()) {

                    Log.i(TAG, "isEditingTrip: " + isEditingTrip);

                    if (isEditingTrip) {
                        editTripData();
                    } else {
                        addTripData();
                    }

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Some fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    Uri getDirectionsURI() {
        /*
            the map http link format should be like this:
            http://maps.google.com/maps?saddr= lat,long &daddr= lat,long
        */
        String dir = String.format("http://maps.google.com/maps?saddr=%s&daddr=%s", startAddress, endAddress);
        return Uri.parse(dir);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Cannot connect to Google Places!", Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("NewApi")
    private void initDateTime() {

        dateTimeET = (EditText) findViewById(R.id.edit_date_time);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
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

    private Trip getTripData() {
        String tTitle = titleET.getText().toString() + "";
        String tStart = startAddress + "";
        String tEnd = endAddress + "";
        String tDateTime = dateTimeStr + "";
        int tIsRound = isRoundSwitch.isChecked() ? 1 : 0;
        String tNotes = notesET.getText().toString() + "";

        return new Trip(tTitle, tStart, tEnd, tDateTime, tIsRound, tNotes);
    }

    private void addTripData() {
        mDBhelper.insertTrip(getTripData());
    }

    private void editTripData() {
        mDBhelper.updateTrip(editedTrip.getId(), getTripData());
    }

    private void checkSaveOrEdit() {
        Intent sourceIntent = getIntent();
        Trip intentTrip = (Trip) sourceIntent.getSerializableExtra("trip");

        if (intentTrip == null) return;

        editedTrip = intentTrip;
        editedTrip.setId(mDBhelper.getTripID(editedTrip));
        titleET.setText(editedTrip.getTitle());

        String startPoint = editedTrip.getStartPoint();
        startFrag.setText(startPoint);
        startAddress = startPoint;

        String endPoint = editedTrip.getEndPoint();
        endFrag.setText(endPoint);
        endAddress = endPoint;

        dateTimeET.setText(editedTrip.getDateTime());
        isRoundSwitch.setChecked(editedTrip.getIsRound() == 1);
        notesET.setText(editedTrip.getNotes());

        dateTimeStr = editedTrip.getDateTime();

        isEditingTrip = true;
        saveTripBtn.setText(R.string.update_trip);

    }

    private boolean hasNotEnteredAllDetails() {
        Log.i(TAG, "checking if user has entered all details");
        return startAddress == null || endAddress == null ||
                titleET.getText().toString().equals("") || dateTimeET.getText().toString().equals("");

    }
}
