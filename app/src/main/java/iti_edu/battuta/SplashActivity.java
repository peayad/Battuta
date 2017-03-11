package iti_edu.battuta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Random;


public class SplashActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    SharedPreferences themePrefs;
    static int theme = R.style.AppTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initTheme();
        initRemoteConfig();

        setContentView(R.layout.activity_splash);
        initQuotes();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
    }

    private void initTheme(){
        themePrefs = getSharedPreferences("ThemeInfo", Context.MODE_PRIVATE);
        boolean changeTheme = themePrefs.getBoolean("changeTheme", false);

        if(changeTheme) {
            theme = R.style.AppThemenew_NoActionBarnew;
        }else{
            theme = R.style.AppTheme_NoActionBar;
        }

        setTheme(theme);
    }

    private void initQuotes() {
        String[] quotes = getResources().getStringArray(R.array.quotesArray);
        String pickedQuote = "\"" + quotes[new Random().nextInt(quotes.length)] + "\"";

        TextView quoteTV = (TextView) findViewById(R.id.splash_quote);
        quoteTV.setText(pickedQuote);
    }

    private void initRemoteConfig() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.setDefaults(R.xml.remote_config_default);

        FirebaseRemoteConfigSettings configSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default);

        mFirebaseRemoteConfig.fetch(0L)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SplashActivity.this, "Fetch Succeeded", Toast.LENGTH_SHORT).show();
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(SplashActivity.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
                        }
                        changeTheme();
                    }
                });
    }

    private void changeTheme() {
        boolean changeTheme = mFirebaseRemoteConfig.getBoolean("changeColor");
        themePrefs.edit().putBoolean("changeTheme", changeTheme).apply();
    }
}
