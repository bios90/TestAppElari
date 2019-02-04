package com.dimfcompany.testappelari.networking.retrofit_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_ResponseNOTOk
{
    @Expose
    @SerializedName("status")
    String status;
    @Expose
    @SerializedName("message")
    String message;

    public Model_ResponseNOTOk()
    {

    }

    public Model_ResponseNOTOk(String status, String message)
    {
        this.status = status;
        this.message = message;
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

    @Override
    public String toString()
    {
        return "Model_ResponseNOTOk{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
