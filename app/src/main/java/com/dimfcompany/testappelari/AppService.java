package com.dimfcompany.testappelari;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.dimfcompany.testappelari.networking.AdviatorApi;
import com.dimfcompany.testappelari.networking.RetrofitConverter;
import com.dimfcompany.testappelari.networking.retrofit_models.Model_ResponseOk;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;

public class AppService extends Service
{
    private static final String TAG = "AppService";
    boolean shouldRun;
    private String simId;
    private AdviatorApi adviatorApi;
    private Thread thread;
    private MyRunnable myRunnable;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(TAG, "onStartCommand: Service Started");
        boolean shouldRestart = intent.getBooleanExtra(Constants.KEY_SHOULD_RESTART, false);
        if (shouldRestart)
        {
            stopSelf();
        }
        adviatorApi = ((AppClass) getApplication()).getAdviatorApi();
        simId = ((AppClass) getApplication()).getSimImsi();
        shouldRun = true;

        myRunnable = new MyRunnable();
        thread = new Thread(myRunnable);
        thread.start();
        return START_STICKY;
    }

    private void checkResponse(final String response)
    {
        final Model_ResponseOk modelResponseOk = RetrofitConverter.getResponseOkFromString(response);
        if (modelResponseOk != null)
        {
            if ("OK".equals(modelResponseOk.getStatus()))
            {
                Intent okIntnet = new Intent(Constants.ACTION_OK_RECIEVER);
                okIntnet.putExtra(Constants.KEY_MODEL_OK, modelResponseOk);
                sendBroadcast(okIntnet);
            } else
            {
                try
                {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(AppService.this, modelResponseOk.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e)
                {

                }


            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        Log.d(TAG, "onTaskRemoved: Removed called, will try to restart");

        Intent restartIntent = new Intent(getApplicationContext(), this.getClass());
        restartIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 300,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    private class MyRunnable implements Runnable
    {

        @Override
        public void run()
        {
            while (shouldRun)
            {
                try
                {
                    Log.d(TAG, "********* MyRunnable Start Retrieving data From Server *********");

                    Call<String> callToAdviator = adviatorApi.getAdviatorData(simId);
                    String response = callToAdviator.execute().body();

                    Log.d(TAG, "********* MyRunnable Got Response Body : " + response + " *********");

                    checkResponse(response);

                    Thread.sleep(60000);
                } catch (Exception e)
                {
                    Log.e(TAG, "Failed to get data from server : " + e.getMessage());
                }
            }
        }
    }
}
