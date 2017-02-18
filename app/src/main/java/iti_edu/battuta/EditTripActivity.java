package iti_edu.battuta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.net.URI;

public class EditTripActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ptr";
    private static final int PICK_START = 1, PICK_END = 2;

    private GoogleApiClient mGoogleApiClient;

    private Place startPlace, endPlace;

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


//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_APP_MAPS);
//                intent.setData();
////                intent.setCa CATEGORY_APP_MAPS, "Battuta Play Store Link");
//                startActivity(Intent.createChooser(intent, "Start Google Maps"));
            }
        });

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
