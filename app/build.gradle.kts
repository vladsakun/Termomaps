plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsKotlinAndroid)
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
  kotlin("kapt")
  kotlin("plugin.serialization") version libs.versions.kotlin
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.university.termomaps"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.university.termomaps"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    viewBinding = true
  }

  secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"
  }
}

dependencies {

  implementation(libs.google.maps)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.play.services.maps)
  implementation(libs.room.runtime)
  kapt(libs.room.compiler)
  implementation(libs.room.ktx)
  implementation(libs.hilt.android)
  kapt(libs.hilt.android.compiler)
  kapt(libs.hilt.compiler)

  implementation(libs.kotlinx.serialization.json)
  implementation(libs.lifecycle.viewmodel.ktx)
  implementation(libs.activity.ktx)
  implementation(libs.fragment.ktx)
  implementation(libs.navigatio.ui.ktx)
  implementation(libs.play.services.location)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}