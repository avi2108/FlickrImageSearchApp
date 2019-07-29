package com.sample.flickrimagesearch.datarepository;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sample.flickrimagesearch.MyApplication;
import com.sample.flickrimagesearch.contracts.ApiCallBack;

public class CloudRepo {
    private static final Object mLock = new Object();

    private static CloudRepo cloudRepo;

    private CloudRepo() {
    }


    public static CloudRepo getInstance() {

        if (cloudRepo == null) {
            synchronized (mLock) {
                cloudRepo = new CloudRepo();
            }
        }
        return cloudRepo;
    }

    public void loadImageList(String imageSearchText, int pageNumber, ApiCallBack apiCallBack) {
        if (!isNetWorkConnected()) {
            apiCallBack.onFail();
            return;
        }
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=c9a5f2a0fec29c9d306756b55dbfdf5c&format=json&nojsoncallback=1&safe_search=1&text=" + imageSearchText + "&page=" + pageNumber;
        getApiTask(apiCallBack).execute(url);
    }

    ImageListAsyncTask getApiTask(ApiCallBack callBack) {
        return new ImageListAsyncTask(callBack);
    }


    boolean isNetWorkConnected(){
        NetworkInfo activeNetwork = ((ConnectivityManager)MyApplication.getContext().getSystemService(MyApplication.getContext().CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
