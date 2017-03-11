package iti_edu.battuta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class TripInfoActivity extends AppCompatActivity {

    TextView titleTV, startTV, endTV, dateTimeTV, notesTV;
    boolean isFromNotification;
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.info_trip_dialog);

        Intent sourceIntent = getIntent();
        trip = (Trip) sourceIntent.getSerializableExtra("trip");
        isFromNotification = sourceIntent.getBooleanExtra("isFromNotification", false);

        initTextViews();
        initButtons();
        initSwitch();
    }

    private void initTheme(){
        SharedPreferences themePrefs = getSharedPreferences("ThemeInfo", Context.MODE_PRIVATE);
        boolean changeTheme = themePrefs.getBoolean("changeTheme", false);
        int theme;
        if(changeTheme) {
            theme = R.style.Theme_AppCompat_Translucentnew;
        }else{
            theme = R.style.Theme_AppCompat_Translucent;
        }
        setTheme(theme);
    }

    void initTextViews() {
        titleTV = (TextView) findViewById(R.id.info_titleTV);
        titleTV.setText(trip.getTitle());

        startTV = (TextView) findViewById(R.id.info_startAddressTV);
        startTV.setText(trip.getStartPoint());

        endTV = (TextView) findViewById(R.id.info_destinationTV);
        endTV.setText(trip.getEndPoint());

        dateTimeTV = (TextView) findViewById(R.id.info_dateTimeTV);
        dateTimeTV.setText(trip.getDateTime());

        notesTV = (TextView) findViewById(R.id.info_notes);
        if (isFromNotification) {
            ((ViewGroup) notesTV.getParent()).removeView(notesTV);
        } else {
            notesTV.setText(trip.getNotes());
        }
    }

    void initButtons() {
        Button startBtn = (Button) findViewById(R.id.info_startBtn);
        Button editBtn = (Button) findViewById(R.id.info_editBtn);
        Button deleteBtn = (Button) findViewById(R.id.info_deleteBtn);

        // act as notification dialog
        if (isFromNotification) {
            Intent sourceIntent = getIntent();
            Trip selectedTrip = (Trip) sourceIntent.getSerializableExtra("trip");
//            trip.setId(1);
            BattutaReminder.deleteReminder(getApplicationContext(), selectedTrip.getId());

            // later button
            editBtn.setText(R.string.remind_later);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trip.setIsDone(0);
                    BattutaReminder.showNotification(getApplicationContext(), trip);
                    finish();
                }
            });

            // dismiss button
            deleteBtn.setText(R.string.remind_dismiss);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDismissConfirmationDialog();
                }
            });

        } else {

            editBtn.setText(R.string.info_edit);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EditTripActivity.class);
                    intent.putExtra("trip", (Serializable) trip);
                    startActivity(intent);
                    finish();
                }
            });

            deleteBtn.setText(R.string.info_delete);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(TripInfoActivity.this);
                    a_builder.setMessage("You are going to delete this trip,\n are you sure?!")
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
                    alert.setTitle("Delete Trip");
                    alert.show();
                }
            });
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri directionsURI = getDirectionsURI();
                Intent goMapsIntent = new Intent(android.content.Intent.ACTION_VIEW, directionsURI);
                startActivity(goMapsIntent);
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

    }

    void initSwitch() {
        Switch tripDone = (Switch) findViewById(R.id.info_switch);
        if (isFromNotification) {
            tripDone.setVisibility(View.INVISIBLE);
        }

        boolean isTripDone = trip.getIsDone() > 0;
        if (!isTripDone) {
            tripDone.setText(R.string.upcoming_trip);
        } else {
            tripDone.setText(R.string.done);
        }

        tripDone.setChecked(!isTripDone);
        tripDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    trip.setIsDone(0);
                    Toast.makeText(getApplicationContext(), "Your trip is set as not done!", Toast.LENGTH_SHORT).show();
                } else {
                    if (trip.getIsRound() == 1) {
                        trip.setIsRound(0);
                        trip.set_infoReverse();
                        Toast.makeText(getApplicationContext(), "Your trip has been reversed!", Toast.LENGTH_SHORT).show();
                    } else {
                        trip.setIsDone(1);
                        Toast.makeText(getApplicationContext(), "Your trip is set as done!", Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO firebase update trip on exit
        FireDB fireDB = FireDB.getInstance();
        fireDB.updateTrip(trip.getId(), trip);
    }

    @Override
    public void onBackPressed() {
        if(isFromNotification){
            showDismissConfirmationDialog();
        }else {
            super.onBackPressed();
        }
    }

    private void showDismissConfirmationDialog(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(TripInfoActivity.this);
        a_builder.setMessage("You are going to dismiss this trip,\n are you sure?!")
                .setCancelable(false)
                .setPositiveButton("Dismiss Trip", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trip.setIsDone(1);
                        finish();
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Dismiss Trip");
        alert.show();
    }

    Uri getDirectionsURI() {
        /*
            the map http link format should be like this:
            http://maps.google.com/maps?saddr= lat,long &daddr= lat,long
        */
        String dir = String.format("https://www.google.com/maps?saddr=%s&daddr=%s", trip.getStartPoint(), trip.getEndPoint());
        return Uri.parse(dir);
    }

}
