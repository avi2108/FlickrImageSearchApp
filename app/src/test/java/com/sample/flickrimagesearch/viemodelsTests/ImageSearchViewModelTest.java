package com.sample.flickrimagesearch.viemodelsTests;

import android.media.Image;
import android.preference.PreferenceManager;

import com.sample.flickrimagesearch.adapters.ImageListAdapter;
import com.sample.flickrimagesearch.contracts.ApiCallBack;
import com.sample.flickrimagesearch.datarepository.CloudRepo;
import com.sample.flickrimagesearch.models.ImageModel;
import com.sample.flickrimagesearch.viewmodels.ImageSearchViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//@RunWith(MockitoJUnitRunner.class)

@RunWith(PowerMockRunner.class)
@PrepareForTest({CloudRepo.class,ImageListAdapter.class})
public class ImageSearchViewModelTest {


    @Mock
    ApiCallBack apiCallBack;

    @Mock
    CloudRepo cloudRepo;



    ImageSearchViewModel imageSearchViewModel;


    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    @Captor
    private ArgumentCaptor<ApiCallBack> apiCallBackArgumentCaptor;

    @Mock
    ImageListAdapter imageListAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(CloudRepo.class);
        PowerMockito.mockStatic(ImageListAdapter.class);
         imageSearchViewModel = new ImageSearchViewModel();
        Mockito.when(CloudRepo.getInstance()).thenReturn(cloudRepo);
    }

    @Test
    public void testCorrectArgsPassedWhenLoadImageListCalled() {
        //Arrange
        Mockito.doNothing().when(cloudRepo).loadImageList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(ApiCallBack.class));

        //Act
        imageSearchViewModel.loadImageListPagesFromApi("human", 0);

        //Assert
        Mockito.verify(cloudRepo).loadImageList(stringArgumentCaptor.capture(), integerArgumentCaptor.capture(), apiCallBackArgumentCaptor.capture());
        Assert.assertEquals("search string passed is not matched","human", stringArgumentCaptor.getValue());
    }



    @Test
    public void testLoadFirstPageWhenDataEmpty(){
        //Arrange
        ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
        ImageSearchViewModel imageSearchViewModel = Mockito.spy(new ImageSearchViewModel());
        imageSearchViewModel.getImageListAdapter().setImageModels(imageModelArrayList);
        imageSearchViewModel.setSearchText("dummy");

        //Act
        imageSearchViewModel.loadImagesToList(0);

        //Assert
        Mockito.verify(imageSearchViewModel).loadImageListPagesFromApi(stringArgumentCaptor.capture(),integerArgumentCaptor.capture());
        Assert.assertEquals("page is not 0 when imagelist is empty",0,(int)integerArgumentCaptor.getValue());
    }

    @Test
    public void testLoadActualPageWhenDataNotEmpty(){
        //Arrange
        ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
        ImageSearchViewModel imageSearchViewModel = Mockito.spy(new ImageSearchViewModel());
        imageModelArrayList.add(new ImageModel("","","","",0,"",true,true,true));
        imageSearchViewModel.getImageListAdapter().setImageModels(imageModelArrayList);
        imageSearchViewModel.setSearchText("dummy");

        //Act
        imageSearchViewModel.loadImagesToList(11);

        //Assert
        Mockito.verify(imageSearchViewModel).loadImageListPagesFromApi(stringArgumentCaptor.capture(),integerArgumentCaptor.capture());
        Assert.assertEquals("pagenum not matched when imagelist is not empty",11,(int)integerArgumentCaptor.getValue());
    }

    @Test
    public void testVerifySearchStringPassed(){
        //Arrange
//        ImageListAdapter imageListAdapter = PowerMockito.spy(ImageListAdapter.class);
        ImageSearchViewModel imageSearchViewModel = Mockito.spy(new ImageSearchViewModel());
        Mockito.doNothing().when(imageListAdapter).notifyDataSetChanged();
        Mockito.doNothing().when(imageSearchViewModel).loadImageListPagesFromApi(ArgumentMatchers.anyString(),ArgumentMatchers.anyInt());

        //Act
        imageSearchViewModel.submitImageSearch("dummy");

        //Assert
        Mockito.verify(imageListAdapter).notifyDataSetChanged();
        Mockito.verify(imageSearchViewModel).loadImagesToList(integerArgumentCaptor.capture());
        Assert.assertEquals("page is 0 when search submmited",0,(int)integerArgumentCaptor.getValue());
        Mockito.verify(imageSearchViewModel).loadImageListPagesFromApi(stringArgumentCaptor.capture(),integerArgumentCaptor.capture());
        Assert.assertEquals("search string passed to api is not matched with user's search string","dummy",stringArgumentCaptor.getValue());
    }


}
