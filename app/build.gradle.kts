import java.util.Properties

val passwordsPropertiesFile = project.rootProject.file("app/passwords.properties")
val passwordsProperties = Properties()
passwordsProperties.load(passwordsPropertiesFile.inputStream())

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.readinglist"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.readinglist"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey: String = passwordsProperties.getProperty("key")
        buildConfigField("String", "APIKEY", "\"$apiKey\"")
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

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.android.volley:volley:1.2.1")
}