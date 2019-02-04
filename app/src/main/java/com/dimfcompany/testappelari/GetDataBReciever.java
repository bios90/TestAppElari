package com.dimfcompany.testappelari;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;

import com.dimfcompany.testappelari.Constants;
import com.dimfcompany.testappelari.networking.retrofit_models.Model_ResponseOk;

import java.util.HashSet;
import java.util.Set;

public class GetDataBReciever extends BroadcastReceiver
{
    private static final String TAG = "GetDataBReciever";
    public interface Listener
    {
        void getResponseOk(Model_ResponseOk model_responseOk);
    }

    Set<Listener> setOfListeners = new HashSet<>();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(Constants.ACTION_OK_RECIEVER.equals(intent.getAction()))
        {
            Model_ResponseOk responseOk = (Model_ResponseOk)intent.getSerializableExtra(Constants.KEY_MODEL_OK);
            for(Listener listener : setOfListeners)
            {
                listener.getResponseOk(responseOk);
            }
        }
    }

    public void registerListener(Listener listener)
    {
        setOfListeners.add(listener);
    }

    public void unregisterListener(Listener listener)
    {
        setOfListeners.remove(listener);
    }


}
