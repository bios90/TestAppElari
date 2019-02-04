package com.dimfcompany.testappelari.networking;

import com.dimfcompany.testappelari.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AdviatorApi
{
    @FormUrlEncoded
    @POST(Constants.URL_GIVE_ME_AD)
    Call<String> getAdviatorData(@Field(Constants.FIELD_ID) String id);
}
