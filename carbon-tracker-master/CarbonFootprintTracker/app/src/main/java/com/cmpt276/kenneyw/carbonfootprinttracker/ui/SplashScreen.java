package com.cmpt276.kenneyw.carbonfootprinttracker.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.DailyReset;
import com.cmpt276.kenneyw.carbonfootprinttracker.model.NotificationReceiver;

/**
 * This class creates a spalsh screen with a progress bar
 * Code for spalsh screen wwas retrieved from :http://abhiandroid.com/ui/progressbar
 */

public class SplashScreen extends AppCompatActivity {
    ProgressBar loadingBar;
    TextView loadingText;
    Thread splashTread; // Called when the activity is first created.

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        setupSplashScreen();
        setupFadeTextAnimation();
        monthlyChanger();
        dailyChanger();
        notificationSpawner();
    }

    // This function checks every month to see if utilites are updated
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void monthlyChanger() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    // This function checks every day to see if utilites are updated
    private void dailyChanger() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Intent intent = new Intent(getApplicationContext(), DailyReset.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void notificationSpawner() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void setupFadeTextAnimation() {
        TextView appNameText = (TextView) findViewById(R.id.Splash_Title);
        TextView authorNameText = (TextView) findViewById(R.id.SplashAuthor);
        TextView lordingText = (TextView) findViewById(R.id.SplashLoading);
        TextView welcomeText = (TextView) findViewById(R.id.SplashWelcome);
        Animation fading = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        Animation blinking = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        appNameText.startAnimation(fading);
        authorNameText.startAnimation(fading);
        lordingText.startAnimation(fading);
        welcomeText.startAnimation(blinking);
    }

    private void setupSplashScreen() {
        loadingBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingText = (TextView) findViewById(R.id.SplashLoading);
        loadingBar.setProgress(0); // set progress value to 0
        loadingBar.setMax(3000);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0; // get progress value from the progress bar
                    // Splash screen pause time
                    while (waited < 3000) {
                        sleep(100);
                        waited += 100;
                        loadingBar.setProgress(waited);
                    }
                    Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                }
            }
        };
        splashTread.start();
    }
}