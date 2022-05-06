# News application for Android with Kotlin

This project covers topics below!

```text
    MVVM , Retrofit, LiveData, ViewModel, Room, Coroutine, Glide, Navigation Component etc
```

## Add dependencies to the project 
```kotlin
dependencies {

    // ROOM
    implementation 'androidx.room:room-runtime:2.4.2'
    implementation 'androidx.room:room-ktx:2.4.2'
    annotationProcessor 'androidx.room:room-compiler:2.4.2'
    kapt 'androidx.room:room-compiler:2.4.2'

    // RETROFIT & OKHTTP
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation "com.squareup.okhttp3:logging-interceptor:4.5.0"

    // COROUTINE
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0'

    // ARCHITECTURAL COMPONENTS
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1'

    // LIFECYCLE SCOPES
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.4.1'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.4.1'

    // NAVIGATION COMPONENTS
    implementation 'androidx.navigation:navigation-fragment-ktx:2.4.2'
    implementation 'androidx.navigation:navigation-ui-ktx:2.4.2'

    // GLIDE
    implementation 'com.github.bumptech.glide:glide:4.13.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.13.1'
}
```

Conclusion
