plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
    id("dagger.hilt.android.plugin")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.example.employeeportal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.employeeportal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"



    }

    buildTypes {
        release {
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation ("com.google.android.material:material:1.1.0-alpha08")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.firebase:firebase-messaging:23.4.1")
    implementation ("com.google.android.gms:play-services-location:21.2.0")
    implementation ("com.google.android.libraries.places:places:3.3.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation ("com.google.android.gms:play-services-location:21.2.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.5.2")


    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.22-1.0.17")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.google.android.material:material:1.11.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.14.2")

    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")
    implementation("com.squareup.moshi:moshi:1.15.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("androidx.preference:preference-ktx:1.2.1")

    implementation("androidx.fragment:fragment-ktx:1.6.2")

    ksp("com.google.dagger:hilt-android-compiler:2.50")
    implementation("com.google.dagger:hilt-android:2.50")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


}