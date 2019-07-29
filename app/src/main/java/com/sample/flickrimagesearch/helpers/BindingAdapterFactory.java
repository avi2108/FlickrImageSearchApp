package com.sample.flickrimagesearch.helpers;

import android.databinding.BindingAdapter;
import android.media.Image;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.sample.flickrimagesearch.viewmodels.ImageSearchViewModel;

public class BindingAdapterFactory {

    @BindingAdapter({"viewModel"})
    public static void bindRecyclerListAdapter(RecyclerView recyclerView, ImageSearchViewModel imageSearchViewModel) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imageSearchViewModel.getImageListAdapter());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(),3);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(imageSearchViewModel.getImageListAdapter());
        EndlessListScrollListener endlessListScrollListener = new EndlessListScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                imageSearchViewModel.loadImagesToList(page);
            }
        };
        recyclerView.addOnScrollListener(endlessListScrollListener);
        imageSearchViewModel.setEndlessListScrollListener(endlessListScrollListener);

    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        CachedImageViewLoader.with(view.getContext()).loadImage(imageUrl,view);
    }
}
