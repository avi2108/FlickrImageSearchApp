package com.sample.flickrimagesearch;

import com.sample.flickrimagesearch.models.ImageModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ImageModelTest {


    ImageModel imageModel;
    final String urlTemplate = "https://farm100.static.flickr.com/dumServer/dummyId_dumSecret.jpg";


    @Before
    public void setUp() {
        imageModel = new ImageModel("dummyId", "dumOwner", "dumSecret", "dumServer", 100, "dumTitle", false, false, false);
    }


    @Test
    public void testImageUrlComposed() {
        Assert.assertEquals("Image url composed not matched", urlTemplate, imageModel.getImageUrl());
    }

}
