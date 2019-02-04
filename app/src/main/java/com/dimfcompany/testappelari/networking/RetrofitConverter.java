package com.dimfcompany.testappelari.networking;

import com.dimfcompany.testappelari.networking.retrofit_models.Model_ResponseOk;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RetrofitConverter
{
    public static Model_ResponseOk getResponseOkFromString(String string)
    {
        Gson gson = new Gson();
        Model_ResponseOk modelResponseOk = gson.fromJson(string.toString(), new TypeToken<Model_ResponseOk>()
        {
        }.getType());

        return modelResponseOk;
    }
}
