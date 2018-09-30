package com.assignment.moneytap.moneytap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_detail);

        Intent intent = getIntent();
        String pageId = intent.getStringExtra(MainActivity.PAGEID);

        WebView webView = findViewById(R.id.page_webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://en.wikipedia.org/?curid=" + pageId);
    }
}
