OpenSource
==========

[customviews](./customviews/src/main/kotlin)
-----------
[utils](./utils/src/main/kotlin)
-----


[livedataCallAdapter](./retrofitlivedatacalladapter/src/main/kotlin)
--------------------
- INTRODUCE <br>

   - The Retrofit's callAdapter for LiveData
   - A Develop-Project ,may be have bugs.

- HOW TO USE IT?

    1. In module's build.gradle
    ```
    implementation "com.aqrlei.opensource:retrofitlivedatacalladapter:latestVersion"
    ```
    2.
    ````
      Retrofit.Builder()
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .addConverterFactory(LiveDataConverterFactory.create())
                ...
    ````
    3. The response data body
    ````
    LiveObservable<LiveResponse<your response data body>>
    ````
    4.
    ````
    //container is LifecycleOwner
    liveObservable.observable(lifecycleOwner,{your action })

    //container is not LifecycleOwner
    liveObservable.observable({your action})
    or
    LiveObservable.observable(mediatorLiveData,{your action})
    ````

License
-------



