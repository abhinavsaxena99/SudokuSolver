package com.example.sudokulab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Advanced extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_advanced,container,false);
        WebView wv = (WebView)v.findViewById(R.id.wbbasic);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://www.sudokudragon.com/advancedstrategy.htm");

        return v;
    }

}
