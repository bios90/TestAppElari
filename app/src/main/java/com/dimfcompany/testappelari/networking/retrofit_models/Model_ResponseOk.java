package com.dimfcompany.testappelari.networking.retrofit_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Model_ResponseOk implements Serializable
{
    @Expose
    @SerializedName("status")
    String status;
    @Expose
    @SerializedName("message")
    String message;
    @Expose
    @SerializedName("url")
    String url;

    public Model_ResponseOk(String status, String message, String url)
    {
        this.status = status;
        this.message = message;
        this.url = url;
    }

    public Model_ResponseOk()
    {

    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "Model_ResponseOk{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
