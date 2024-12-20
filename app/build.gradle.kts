plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    // Add the Google services Gradle plugin
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.equipotres"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.equipotres"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    implementation(libs.androidx.annotation)
    ksp("androidx.room:room-compiler:2.4.3")

    val nav_version = "2.7.3"
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.airbnb.android:lottie:6.5.2")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.navigation:navigation-common:$nav_version")
    implementation ("com.google.android.material:material:1.9.0")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-firestore")

    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    //authentication
    implementation("com.google.firebase:firebase-auth-ktx")

    //viewmodel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.activity:activity-ktx:1.8.0")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    //dagger hilt
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-compiler:2.51.1")

    // JUnit
    testImplementation ("junit:junit:4.13.2")

    // MockK para mocking
    testImplementation ("io.mockk:mockk:1.13.5")
    testImplementation ("io.mockk:mockk-android:1.13.5")

    // Coroutines para testing
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Librería de LiveData para pruebas unitarias
    testImplementation ("androidx.arch.core:core-testing:2.2.0")

    // Hilt para testing
    kaptAndroidTest ("com.google.dagger:hilt-android-compiler:2.48")
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.48")
    testImplementation ("com.google.dagger:hilt-android-testing:2.48")


    testImplementation ("org.mockito:mockito-junit-jupiter:3.12.4")// Para JUnit 5
    testImplementation ("org.mockito:mockito-core:4.5.1")
    testImplementation ("org.mockito:mockito-inline:4.5.1")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:5.0.0") // integración con Kotlin
    testImplementation ("androidx.arch.core:core-testing:2.2.0") // Para LiveData testing{
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")


}

