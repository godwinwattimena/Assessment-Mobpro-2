plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "org.d3if3126.assessment2"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.d3if3126.assessment2"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //noinspection UseTomlInstead
    implementation("androidx.core:core-ktx:1.13.1")
    //noinspection UseTomlInstead
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    //noinspection UseTomlInstead
    implementation("androidx.activity:activity-compose:1.9.0")
    //noinspection UseTomlInstead
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui")
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui-graphics")
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui-tooling-preview")
    //noinspection UseTomlInstead
    implementation("androidx.compose.material3:material3")
    //noinspection UseTomlInstead
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    //noinspection UseTomlInstead
    implementation("androidx.navigation:navigation-compose:2.7.7")
    //noinspection UseTomlInstead
    implementation("androidx.room:room-runtime:2.6.1")
    //noinspection UseTomlInstead
    implementation("androidx.room:room-ktx:2.6.1")
    //noinspection UseTomlInstead
    ksp("androidx.room:room-compiler:2.6.1")
    //noinspection UseTomlInstead
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //noinspection UseTomlInstead
    testImplementation("junit:junit:4.13.2")
    //noinspection UseTomlInstead
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    //noinspection UseTomlInstead
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //noinspection UseTomlInstead
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    //noinspection UseTomlInstead
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    //noinspection UseTomlInstead
    debugImplementation("androidx.compose.ui:ui-tooling")
    //noinspection UseTomlInstead
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}