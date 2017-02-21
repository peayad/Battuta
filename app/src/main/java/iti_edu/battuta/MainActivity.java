package iti_edu.battuta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ptr-main";
    final static private int ADD_TRIP_REQUEST = 0;
    final static private int TRIP_INFO_REQUEST = 1;

    private CashedAdapter myCashedAdapter;
    private BattutaDBhelper myDBhelper;
    private ArrayList<Trip> tripList = new ArrayList<>();

    SharedPreferences loginPreferences;

    boolean doubleBackToExitPressedOnce;

    private static Trip selectedTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginPreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditTripActivity.class);
                startActivityForResult(intent, ADD_TRIP_REQUEST);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myDBhelper = new BattutaDBhelper(getApplicationContext());
        tripList = myDBhelper.getAllTrips();
        myCashedAdapter = new CashedAdapter(getApplicationContext(), tripList);

        ListView myListView = (ListView) findViewById(R.id.mainListView);
        myListView.setAdapter(myCashedAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTrip = tripList.get(position);
                Intent intent = new Intent(getApplicationContext(), TripInfoActivity.class);
                intent.putExtra("title", selectedTrip);
                startActivityForResult(intent, TRIP_INFO_REQUEST);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_feedback) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:battuta@iti.com"));
            intent.putExtra(Intent.EXTRA_EMAIL, "battuta@iti.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            intent.putExtra(Intent.EXTRA_TEXT, "Hello,\n this is my feedback for Battuta App.");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_upcoming) {
            // TODO - change list view items

        } else if (id == R.id.nav_passed) {
            // TODO - change list view items
            Toast.makeText(getApplicationContext(), "Should Sync with Firebase later :)", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_sync) {
            // TODO - sync data with firebase
            Toast.makeText(getApplicationContext(), "Should Sync with Firebase later :)", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Battuta Play Store Link");
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "Share App"));

        } else if (id == R.id.nav_about) {
            AppCompatDialog aboutDialog = new AppCompatDialog(this);
            aboutDialog.setTitle("About!");
            aboutDialog.setContentView(R.layout.about_dialog);
            aboutDialog.show();

        } else if (id == R.id.nav_languages) {
            // TODO - show dialoge


        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();

            myDBhelper.deleteTable();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TRIP_REQUEST) {
            if (resultCode == RESULT_OK) {
//
//                Trip newTrip = (Trip) data.getSerializableExtra("trip");
//                myDBhelper.insertTrip(newTrip);

                tripList = myDBhelper.getAllTrips();
                myCashedAdapter.clear();
                myCashedAdapter.addAll(tripList);

                Log.i(TAG, tripList.size() + " list size");
                Log.i(TAG, myDBhelper.numberOfRows() + " rows");
            }
//        } else if (requestCode == TRIP_INFO_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                myDBhelper.deleteTrip(selectedTrip);
//                titles.remove(selectedTrip);
//                titles = myDBhelper.getAllTrips();
//                myCashedAdapter.clear();
//                myCashedAdapter.addAll(titles);
//
//            }
        }
    }
}
