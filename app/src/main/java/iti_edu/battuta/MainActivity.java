package iti_edu.battuta;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    static private int doneflag = 0;

    final static private String TAG = "ptr-main";
    final static private int ADD_TRIP_REQUEST = 0;
    final static private int TRIP_INFO_REQUEST = 1;

    TextView emptyListNotice;

    private CashedAdapter myCashedAdapter;
    private BattutaDBadapter myDBhelper;
    private ArrayList<Trip> tripList = new ArrayList<>();

    SharedPreferences loginPreferences;

    boolean doubleBackToExitPressedOnce;

    private static Trip selectedTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginPreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.upcoming);
        setSupportActionBar(toolbar);

        emptyListNotice = (TextView) findViewById(R.id.emptyNoticeTV);

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

        myDBhelper = new BattutaDBadapter(getApplicationContext());
        tripList = myDBhelper.getTrips(doneflag);
        myCashedAdapter = new CashedAdapter(getApplicationContext(), tripList);

        ListView myListView = (ListView) findViewById(R.id.mainListView);
        myListView.setAdapter(myCashedAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTrip = tripList.get(position);
                Intent intent = new Intent(getApplicationContext(), TripInfoActivity.class);
                intent.putExtra("trip", (Serializable) selectedTrip);
                startActivityForResult(intent, TRIP_INFO_REQUEST);
            }
        });

        String email = loginPreferences.getString("email", "");
        View headerView = navigationView.getHeaderView(0);
        TextView emailTV = (TextView) headerView.findViewById(R.id.tvUserEmail);
        emailTV.setText(email);

        String[] username = email.split("@");
        TextView usernameTv = (TextView) headerView.findViewById(R.id.tvUsername);
        usernameTv.setText(username[0]);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
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
            Toast.makeText(this, "click BACK again to exit", Toast.LENGTH_SHORT).show();

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_upcoming) {
            toolbar.setTitle(R.string.upcoming);
            doneflag = 0;
            updateListView();

        } else if (id == R.id.nav_passed) {
            toolbar.setTitle(R.string.passed);
            doneflag = 1;
            updateListView();

        } else if (id == R.id.nav_sync) {
            // TODO - sync data with firebase
//            Toast.makeText(getApplicationContext(), "Should Sync with Firebase later :)", Toast.LENGTH_SHORT).show();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String userEmail = loginPreferences.getString("email", "");
            String[] username = userEmail.split("@");
            DatabaseReference userDB_ref = database.getReference().child(username[0]);

            userDB_ref.setValue(myDBhelper.getAllTrips());
            Toast.makeText(getApplicationContext(), "Synced with Firebase!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Battuta Play Store Link");
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "Share App"));

        } else if (id == R.id.nav_about) {
            AppCompatDialog aboutDialog = new AppCompatDialog(this);
            aboutDialog.setTitle("About!");
            aboutDialog.setContentView(R.layout.about_dialog);
            aboutDialog.setCanceledOnTouchOutside(true);
            aboutDialog.show();

        } else if (id == R.id.nav_logout) {
            // remove flag from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();

            BattutaReminder.removeAllReminders(getApplicationContext(), myDBhelper.getTrips(0));

            // delete database
            myDBhelper.deleteTable();

            // move user to login screen
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
                Snackbar.make(findViewById(R.id.content_main), R.string.snackbar_trip_added, Snackbar.LENGTH_SHORT).show();
            }
        } else if (requestCode == TRIP_INFO_REQUEST) {
            if (resultCode == RESULT_OK) {
                BattutaReminder.deleteReminder(getApplicationContext(), myDBhelper.getTripID(selectedTrip));
                myDBhelper.deleteTrip(selectedTrip);
                Snackbar.make(findViewById(R.id.content_main), R.string.snackbar_trip_deleted, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    void updateListView() {
        tripList = myDBhelper.getTrips(doneflag);

        int showNotice = tripList.size() == 0 ? View.VISIBLE : View.INVISIBLE;
        emptyListNotice.setVisibility(showNotice);

        myCashedAdapter.clear();
        myCashedAdapter.addAll(tripList);
        myCashedAdapter.notifyDataSetChanged();
        BattutaReminder.updateAllReminders(getApplicationContext(), myDBhelper.getTrips(0));
    }
}
