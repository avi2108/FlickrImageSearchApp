package com.sample.flickrimagesearch.contracts;


public interface ApiCallBack {
    void onRequest();

    void onResponse(String response);

    void onFail();
}