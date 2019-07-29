# FlickrImageSearchApp
Flickr api Image search app demo repo

This app provides a search option to browse search based image listing feature.

API used : Flickr public image api.

Technical stack : Language : Core Java Architecture : MVVM with Databinding Test frameworks : JUnit, Mockito , PowerMock

Code approach :

Implemented image caching mechanism with DiskLRUCache and LRUCache and designed a custome image loader. Implemented Pagenation for endless scrolling recyclerView list retrieval using CustomScrollListener listening to recyclerview's layoutmanager. HttpURLConnection for API calls.

Code flow :

MainActiivity - > ImageSearchViewModel(business logic) - > CloudRepo (Repository) -> ApiCall 
ImageListAdapter - > CachedImageLoader for offline image caching.
BindingAdapterFactory - > Handles all custom bindings injected into ui xml

Covered possible testcases execution with Mockito and PowerMock.

UnitTestCases found dependency mocking limitations for which Dependency Injection must be improved.
