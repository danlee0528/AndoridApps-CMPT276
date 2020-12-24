package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * This class is called every night at 9pm and manages which notification that is to be sent to the
 * user. Pulls variables from sharedpreferences and uses them to judge which one to show and adds
 * values if necessary.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.cmpt276.kenneyw.carbonfootprinttracker.R;
import com.cmpt276.kenneyw.carbonfootprinttracker.ui.MainMenu;
import com.cmpt276.kenneyw.carbonfootprinttracker.ui.SelectJourney;
import com.cmpt276.kenneyw.carbonfootprinttracker.ui.SelectUtilities;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String SHAREDPREF_SET = "CarbonFootprintTrackerJournies";
    private static final String SHAREDPREF_ITEM_AMOUNTOFJOURNEYS = "AmountOfJourneys";
    private static final String SHAREDPREF_SET_2 = "CarbonFootprintTrackerUtilities";
    private static final String SHAREDPREF_ITEM_AMOUNTOFUTILITIES = "AmountOfUtilities";
    private static final String ADDEDTODAY ="addedToday";


    int journeysAmount;
    int journeysTodayAmount;
    int utilitiesMonthAmount;
    int utilitiesAmount;
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onReceive(Context context, Intent intent){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        journeysAmount = preferences.getInt(SHAREDPREF_ITEM_AMOUNTOFJOURNEYS, 0);
        journeysTodayAmount = preferences.getInt(ADDEDTODAY, 0);
        utilitiesAmount = preferences.getInt(SHAREDPREF_ITEM_AMOUNTOFUTILITIES, 0);
        utilitiesMonthAmount = preferences.getInt(SHAREDPREF_ITEM_AMOUNTOFUTILITIES, 0);

        if (journeysAmount == 0 && utilitiesAmount == 0){
           showDefaultNotification(context);
        } else if (journeysAmount == 0){
            showJourneysNotification(context);
        } else if (utilitiesAmount == 0){
            showBillsNotification(context);
        } else if (utilitiesMonthAmount == 0){
            showMonthlyBillsNotification(context);
        } else if (journeysTodayAmount == 0){
            showNoDailyJourneysNotification(context);
        } else if (journeysTodayAmount > 0) {
            showMoreNotification(context);
        }
        Log.i("TAG", "TESTING");
    }

    //The case where there are no bills or journeys
    public void showDefaultNotification(Context context) { //Done
        Intent intent = new Intent(context, MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.car_notification)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text))
                .setContentIntent(pi)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    //The case where a user has no bills.
    public void showBillsNotification(Context context) { //DONE
        Intent intent = new Intent(context, SelectUtilities.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.car_notification)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text_bills))
                .setContentIntent(pi)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public void showMonthlyBillsNotification(Context context) {
        Intent intent = new Intent(context, SelectUtilities.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.car_notification)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text_bills_monthly))
                .setContentIntent(pi)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    //The case where a user has no journeys
    public void showJourneysNotification(Context context) {
        Intent intent = new Intent(context, SelectJourney.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.car_notification)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text_journeys))
                .setContentIntent(pi)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    //Asking the user if they would like to add more journeys when some have been added for the day
    public void showMoreNotification(Context context) {
        Intent intent = new Intent(context, SelectJourney.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.car_notification)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(String.format(context.getString(R.string.notification_text_journeys_more_than_one), journeysTodayAmount))
                .setContentIntent(pi)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    //The case where a user hasn't entered a journey today
    public void showNoDailyJourneysNotification(Context context) {
        Intent intent = new Intent(context, SelectJourney.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.car_notification)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text_journeys_none_today))
                .setContentIntent(pi)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
