plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.polaris.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {


    implementation(projects.core.util)
    implementation(projects.core.model)
    implementation (projects.core.domain)
    implementation (libs.androidx.room.runtime)
    implementation(libs.firebase.database)
    kapt (libs.androidx.room.compiler)
    //hilt
    implementation("androidx.room:room-ktx:2.6.1")


    implementation(libs.hilt.android)
    implementation (libs.androidx.hilt.common)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)

    // https://github.com/skydoves/sandwich
    implementation (libs.sandwich)
    implementation("javax.inject:javax.inject:1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}