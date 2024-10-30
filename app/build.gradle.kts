plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)

    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.teovladusic.widgetsforstripe"

    compileSdk = 34

    defaultConfig {
        applicationId = "com.teovladusic.widgetsforstripe"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
    }

    flavorDimensions.addAll(listOf("environment"))

    productFlavors {
        create("widgets-for-stripe") {
            dimension = "environment"

            resValue("string", "app_name", "Widgets For Stripe")

            buildConfigField("String", "BASE_URL", "\"https://api.stripe.com/v1/\"")
        }

        create("widgets-for-stripe-beta") {
            dimension = "environment"

            versionNameSuffix = "-beta"
            applicationIdSuffix = ".beta"

            resValue("string", "app_name", "Widgets For Stripe Beta")

            buildConfigField("String", "BASE_URL", "\"https://api.stripe.com/v1/\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ui
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // material
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended.android)

    // testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Dagger Hilt
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.bundles.dagger.hilt)
    implementation(libs.androidx.hilt.work)

    // lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)

    // splash screen api
    implementation(libs.androidx.core.splashscreen)

    // retrofit 2
    implementation(libs.bundles.retrofit)

    // navigation
    implementation(libs.destinations.animations.core)
    ksp(libs.compose.destinations)

    // coil
    implementation(libs.coil.compose)

    // gson
    implementation(libs.converter.gson)

    // Timber
    implementation(libs.timber)

    // datastore
    implementation(libs.androidx.datastore.preferences)

    // native review
    implementation(libs.play.review)

    // widget
    implementation(libs.bundles.glance)

    // work manager
    implementation(libs.work.runtime.ktx)
}