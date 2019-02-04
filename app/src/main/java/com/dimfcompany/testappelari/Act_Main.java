package com.dimfcompany.testappelari;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dimfcompany.testappelari.networking.retrofit_models.Model_ResponseOk;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

public class Act_Main extends AppCompatActivity implements GetDataBReciever.Listener
{
    private static final String TAG = "Act_Main";
    Button btn_start, btn_stop;
    TextView tv_indicator;
    GetDataBReciever getDataBReciever;
    AppClass appClass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        if(!hasPermission())
        {
            requestPermission();
        }
        init();

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        setBroadcastListener();
        showHideIndicator();
    }

    @Override
    protected void onPause()
    {
        getDataBReciever.unregisterListener(this);
        super.onPause();
    }

    private void setBroadcastListener()
    {
        if(getDataBReciever == null)
        {
            getDataBReciever = new GetDataBReciever();
        }
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_OK_RECIEVER);
        registerReceiver(getDataBReciever,intentFilter);

        getDataBReciever.registerListener(this);
    }



    private void init()
    {
        appClass = (AppClass)getApplicationContext();
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        tv_indicator = findViewById(R.id.tv_indicator);

        btn_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!hasPermission())
                {
                    requestPermission();
                    return;
                }
                Intent intent = new Intent(Act_Main.this,AppService.class);
                startService(intent);
                getDataBReciever.registerListener(Act_Main.this);

                Toast.makeText(Act_Main.this, "Service started, now will retrieve data from server", Toast.LENGTH_SHORT).show();
                showHideIndicator();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Act_Main.this,AppService.class);
                stopService(intent);

                Toast.makeText(Act_Main.this, "Service Stopped, will not get info from server.", Toast.LENGTH_SHORT).show();
                showHideIndicator();
                getDataBReciever.unregisterListener(Act_Main.this);
            }
        });
    }

    private void requestPermission()
    {
        XXPermissions.with(this)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermission()
                {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll)
                    {

                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick)
                    {

                    }
                });
    }

    private boolean hasPermission()
    {
        return XXPermissions.isHasPermission(this,Permission.READ_PHONE_STATE);
    }

    @Override
    public void getResponseOk(Model_ResponseOk model_responseOk)
    {
        Intent intent = new Intent(Act_Main.this,Act_Web_Dialog.class);
        intent.putExtra(Constants.KEY_URI_TO_SHOW,model_responseOk.getUrl());
        startActivity(intent);
    }

    private void showHideIndicator()
    {
        if(appClass.isServiceRunning(AppService.class))
        {
            tv_indicator.setVisibility(View.VISIBLE);
        }
        else
            {
                tv_indicator.setVisibility(View.GONE);
            }
    }

    @Override
    protected void onDestroy()
    {
        getDataBReciever.unregisterListener(this);
        unregisterReceiver(getDataBReciever);
        Intent restartIntent = new Intent(Act_Main.this,AppService.class);
        restartIntent.putExtra(Constants.KEY_SHOULD_RESTART,true);
        startService(restartIntent);

        super.onDestroy();
    }
}
