package com.dimfcompany.testappelari;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import im.delight.android.webview.AdvancedWebView;

public class Act_Web_Dialog extends AppCompatActivity
{
    AdvancedWebView webView;
    Button btn_close;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_web_dialog);
        init();
    }

    private void init()
    {
        webView = findViewById(R.id.web_view);
        webView.loadUrl(getUrl());
        btn_close = findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private String getUrl()
    {
        return getIntent().getStringExtra(Constants.KEY_URI_TO_SHOW);
    }

    @Override
    public void onBackPressed()
    {
//        super.onBackPressed();
    }
}
