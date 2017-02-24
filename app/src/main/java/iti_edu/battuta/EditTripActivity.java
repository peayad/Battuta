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

import java.sql.Time;
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

    private GoogleApiClient mGoogleApiClient;

    private Place startPlace, endPlace;

    private EditText titleET, dateTimeET, notesET;
    private Switch isRoundSwitch;

    private Calendar calendar;
    private String dateTimeStr;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    private int tripYear, tripMonth, tripDay, tripHour, tripMinute;

    String aa = "";


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

        Button saveTripBtn = (Button) findViewById(R.id.edit_btnSaveTrip);
        saveTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasNotEnteredAllDetails()) {
                    if (isEditingTrip()) {
                        editTripData();
                    } else {
                        addTripData();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                    }

                    createReminder();
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
        String startLatLng = startPlace.getLatLng().latitude + "," + startPlace.getLatLng().longitude;
        String endLatLng = endPlace.getLatLng().latitude + "," + endPlace.getLatLng().longitude;
        return Uri.parse("http://maps.google.com/maps?saddr=" + startLatLng + "&daddr=" + endLatLng);
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
        String tStart = startPlace != null ? startPlace.getAddress().toString() + "" : "";
        String tEnd = endPlace != null ? endPlace.getAddress().toString() + "" : "";
        String tDateTime = dateTimeStr + "";
        int tIsRound = isRoundSwitch.isChecked() ? 1 : 0;
        String tNotes = notesET.getText().toString() + "";

        return new Trip(tTitle, tStart, tEnd, tDateTime, tIsRound, tNotes);
    }

    private void addTripData() {
        BattutaDBadapter mDBhelper = new BattutaDBadapter(getApplicationContext());
        mDBhelper.insertTrip(getTripData());
    }

    private void editTripData() {
        //TODO Tasnim
    }

    private boolean isEditingTrip() {
        Intent sourceIntent = getIntent();
        Trip editedTrip = (Trip) sourceIntent.getSerializableExtra("trip");
        if (editedTrip != null) {
            titleET.setText(editedTrip.getTitle());
            dateTimeET.setText(editedTrip.getDateTime());
            isRoundSwitch.setChecked(editedTrip.getIsRound() == 1);
            notesET.setText(editedTrip.getNotes());
            return true;
        }
        return false;
    }

    private boolean hasNotEnteredAllDetails() {
        Log.i(TAG, "i am here now");
        return titleET.getText().toString().equals("") ||
                dateTimeET.getText().toString().equals("");

    }

    private void createReminder() {

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy  hh:mm aa");

        try {
            c.setTime(sdf.parse(dateTimeStr));
            c.add(Calendar.MONTH, 1);
            Log.i(TAG, "worked well!");
            Log.i(TAG, System.currentTimeMillis() + "system time");
            Log.i(TAG, c.getTimeInMillis() + "calendar time");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i(TAG, "didn't happen");
        }


        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, pendingIntent);

        SimpleDateFormat formatter = new SimpleDateFormat("DD-MMM-yyyy hh:mm aa");
        String currentDate = formatter.format(c.getTimeInMillis());
        Log.i(TAG, currentDate + " that's the current date");

        Log.i(TAG, c.get(c.HOUR) + " look");
        Log.i(TAG, c.get(c.MINUTE) + " look");

    }
}
