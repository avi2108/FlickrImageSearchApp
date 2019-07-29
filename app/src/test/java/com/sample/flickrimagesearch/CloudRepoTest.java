package com.sample.flickrimagesearch;

import android.net.ConnectivityManager;

import com.sample.flickrimagesearch.contracts.ApiCallBack;
import com.sample.flickrimagesearch.datarepository.CloudRepo;
import com.sample.flickrimagesearch.datarepository.ImageListAsyncTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({CloudRepo.class})
public class CloudRepoTest {

    @Mock
    ConnectivityManager connectivityManager;

    CloudRepo cloudRepoSpy;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    @Captor
    private ArgumentCaptor<ApiCallBack> apiCallBackArgumentCaptor;


    @Before
    public void setUp() {
        cloudRepoSpy = PowerMockito.spy(CloudRepo.getInstance());
    }

    @Test(expected = RuntimeException.class)
    public void testFailCalledOnNoNetwork() throws Exception {
        //Arrange
        PowerMockito.when(cloudRepoSpy, "isNetWorkConnected",null).thenReturn(false);

        //Act
        cloudRepoSpy.loadImageList(stringArgumentCaptor.capture(), integerArgumentCaptor.capture(), apiCallBackArgumentCaptor.capture());
        ApiCallBack apiCallBack = apiCallBackArgumentCaptor.getValue();

        //Assert
        Mockito.verify(apiCallBack).onFail();
    }

    @Test(expected = RuntimeException.class)
    public void testCallImageAsyncTaskOnNetwork() throws Exception {

        //Arrange
        PowerMockito.when(cloudRepoSpy, "isNetWorkConnected",null).thenReturn(true);
        ImageListAsyncTask imageListAsyncTaskMocked = Mockito.mock(ImageListAsyncTask.class);
        PowerMockito.when(cloudRepoSpy,"getApiTask", ArgumentMatchers.any(ApiCallBack.class)).thenReturn(imageListAsyncTaskMocked);

       //Act
        cloudRepoSpy.loadImageList(stringArgumentCaptor.capture(), integerArgumentCaptor.capture(), apiCallBackArgumentCaptor.capture());

      //Assert
        Mockito.verify(imageListAsyncTaskMocked).execute(stringArgumentCaptor.capture());
    }
}
