plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.polaris.main"
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
    buildFeatures {
        compose = true
    }
}


dependencies {
    implementation(projects.core.model)
    implementation (libs.firebase.auth)
    implementation (libs.play.services.auth)
    implementation (platform(libs.firebase.bom))
    implementation(projects.core.domin)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.designsystem)
    implementation(projects.core.util)

    implementation(projects.feature.signIn)
    implementation(projects.feature.splash)
    implementation(projects.feature.clipboard)
    implementation(projects.feature.clipboardList)
    implementation(projects.feature.clipboardSaveAnimation)
    implementation(projects.feature.clipboardEdit)
    implementation(projects.feature.folderEdit)
    implementation(projects.feature.folderJoin)
    implementation(projects.feature.mainSave)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //hilt
    implementation (libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    implementation (libs.androidx.hilt.common)

    implementation (libs.androidx.activity.ktx)
    implementation(libs.androidx.runtime.livedata)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}