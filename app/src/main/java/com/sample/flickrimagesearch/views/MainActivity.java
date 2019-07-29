package com.sample.flickrimagesearch.views;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.sample.flickrimagesearch.R;
import com.sample.flickrimagesearch.databinding.ActivityMainBinding;
import com.sample.flickrimagesearch.helpers.EndlessListScrollListener;
import com.sample.flickrimagesearch.viewmodels.ImageSearchViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    ImageSearchViewModel imageSearchViewModel;
    //    ImageListAdapter imageListAdapter;
    EndlessListScrollListener endlessListScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        imageSearchViewModel = ViewModelProviders.of(this).get(ImageSearchViewModel.class);
        activityMainBinding.setImageListViewModel(imageSearchViewModel);
        activityMainBinding.executePendingBindings();
        setSupportActionBar(activityMainBinding.toolbar);
        subscribeObservers();
    }

    void subscribeObservers() {
        imageSearchViewModel.getIsScrollResetObserver().observe(this, aBoolean -> {
            if (aBoolean) imageSearchViewModel.getEndlessListScrollListener().resetState();
        });
        imageSearchViewModel.getIsNetWorkConnected().observe(this, aBoolean -> {
            if (!aBoolean)
            Snackbar.make(activityMainBinding.cordinatorLayoutParent, R.string.internet_error, Snackbar.LENGTH_LONG)
                    .show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
//                UserFeedback.show( "SearchOnQueryTextSubmit: " + query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchView.setQuery("",false);
                imageSearchViewModel.submitImageSearch(query);
                myActionMenuItem.collapseActionView();
                getSupportActionBar().setTitle(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
