package com.sample.flickrimagesearch.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sample.flickrimagesearch.MyApplication;
import com.sample.flickrimagesearch.adapters.ImageListAdapter;
import com.sample.flickrimagesearch.contracts.ApiCallBack;
import com.sample.flickrimagesearch.datarepository.CloudRepo;
import com.sample.flickrimagesearch.helpers.EndlessListScrollListener;
import com.sample.flickrimagesearch.models.ImageModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageSearchViewModel extends ViewModel implements ApiCallBack {

    private ImageListAdapter imageListAdapter;
    private ArrayList<ImageModel> imageModels;
    private String searchText;
    private MutableLiveData<Boolean> isScrollResetObserver = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetWorkConnected = new MutableLiveData<>();
    private EndlessListScrollListener endlessListScrollListener;

    public ImageSearchViewModel() {
        imageListAdapter = new ImageListAdapter(new ArrayList<>());
        imageModels = new ArrayList<>();
        isScrollResetObserver.postValue(false);
        isNetWorkConnected.postValue(true);
    }

    /**
     * contacts cloud repository for fetching paged image list
     * @param imageText image search text
     * @param pageNumber
     */
    public void loadImageListPagesFromApi(String imageText, int pageNumber){
        CloudRepo.getInstance().loadImageList(imageText,pageNumber,this);
    }


    /**
     * takes care of functionality post image search text submission
     * @param text searchText
     */
    public void submitImageSearch(String text){
        searchText = text;
        // 1. First, clear the array of data
        imageModels.clear();
// 2. Notify the adapter of the update
        imageListAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
// 3. Reset endless scroll listener when performing a new search
//        scrollListener.resetState();
        isScrollResetObserver.postValue(true);
        loadImagesToList(0);
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * decides page number to request depending on current adapter's items
     * @param page
     */
    public void loadImagesToList(int page){
        if (searchText==null)return;
        if (!imageListAdapter.getImageModels().isEmpty())
            loadImageListPagesFromApi(searchText,page);
        else loadImageListPagesFromApi(searchText,0);
    }

    /**
     * notifies adapter with proper data set and proper range
     * @param data
     */
    public void setUpAdapterData(ArrayList<ImageModel> data) {
        if (imageModels.isEmpty())
        {
            this.imageModels= data;
            imageListAdapter.setImageModels(imageModels);
            imageListAdapter.notifyDataSetChanged();
        }else{
            int prevItemCount = imageModels.size();
            this.imageModels.addAll(data);
            imageListAdapter.setImageModels(imageModels);
            imageListAdapter.notifyItemRangeInserted(prevItemCount,imageModels.size()-1);
        }


    }

    public MutableLiveData<Boolean> getIsScrollResetObserver() {
        return isScrollResetObserver;
    }

    public MutableLiveData<Boolean> getIsNetWorkConnected() {
        return isNetWorkConnected;
    }

    public void setIsScrollResetObserver(MutableLiveData<Boolean> isScrollResetObserver) {
        this.isScrollResetObserver = isScrollResetObserver;
    }

    public ImageListAdapter getImageListAdapter() {
        return imageListAdapter;
    }

    public void setEndlessListScrollListener(EndlessListScrollListener endlessListScrollListener) {
        this.endlessListScrollListener = endlessListScrollListener;
    }

    public EndlessListScrollListener getEndlessListScrollListener() {
        return endlessListScrollListener;
    }

    @Override
    public void onRequest() {

    }

    @Override
    public void onResponse(String response) { //imageListResponse
        try {
            isNetWorkConnected.postValue(true);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONObject("photos").getJSONArray("photo");
            ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
            for (int index = 0; index < jsonArray.length(); index++) {
                imageModelArrayList.add(new ImageModel(jsonArray.getJSONObject(index)));
            }
            setUpAdapterData(imageModelArrayList);
        }catch (Exception e){e.printStackTrace();}
      }

    @Override
    public void onFail() {
        Log.e("imageserchViewmodel","api failed");
        isNetWorkConnected.postValue(false);
    }
}
