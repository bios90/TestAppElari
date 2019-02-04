package com.dimfcompany.testappelari;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.dimfcompany.testappelari.networking.AdviatorApi;
import com.dimfcompany.testappelari.networking.RetrofitConverter;
import com.dimfcompany.testappelari.networking.retrofit_models.Model_ResponseOk;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;

public class AppService extends Service
{
    private static final String TAG = "AppService";
    boolean shouldRun;
    private String simId;
    private AdviatorApi adviatorApi;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        adviatorApi = ((AppClass) getApplication()).getAdviatorApi();
        simId = ((AppClass) getApplication()).getSimImsi();
        shouldRun = true;
        new Thread(new MyRunnable()).start();
        return START_STICKY;
    }

    private void checkResponse(final String response)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                Model_ResponseOk modelResponseOk = RetrofitConverter.getResponseOkFromString(response);
                if (modelResponseOk != null)
                {
                    if (modelResponseOk.getStatus().equals("OK"))
                    {
                        Intent okIntnet = new Intent(Constants.ACTION_OK_RECIEVER);
                        okIntnet.putExtra(Constants.KEY_MODEL_OK,modelResponseOk);
                        sendBroadcast(okIntnet);
                    }
                    else
                        {
                            Toast.makeText(AppService.this, modelResponseOk.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                }
                else
                {
                    //Got something unexpected from server
                    Toast.makeText(AppService.this, "Got some Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    shouldRun = false;
                    Log.e(TAG, "Failed to get data from server : " + e.getMessage());
                }
            }
        }
    }
}
