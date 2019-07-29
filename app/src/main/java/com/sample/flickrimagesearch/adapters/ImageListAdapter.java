package com.sample.flickrimagesearch.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sample.flickrimagesearch.R;
import com.sample.flickrimagesearch.databinding.LayoutImageItemBinding;
import com.sample.flickrimagesearch.models.ImageModel;

import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageItemHolder> {

    private ArrayList<ImageModel> imageModels;

    public ImageListAdapter(ArrayList<ImageModel> imageModelList){
        this.imageModels = imageModelList;
    }

    public void setImageModels(ArrayList<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }

    @NonNull
    @Override
    public ImageItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ImageItemHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.layout_image_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageItemHolder imageItemHolder, int position) {
        imageItemHolder.layoutImageItemBinding.setImageurl(imageModels.get(position).getImageUrl());
        imageItemHolder.layoutImageItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    public ArrayList<ImageModel> getImageModels() {
        return imageModels;
    }

    public class ImageItemHolder extends RecyclerView.ViewHolder {

        LayoutImageItemBinding layoutImageItemBinding;

        public ImageItemHolder(LayoutImageItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.layoutImageItemBinding = itemBinding;
        }
    }
}
