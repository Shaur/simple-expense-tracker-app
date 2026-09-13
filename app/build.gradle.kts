plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
    id("androidx.room3")
}

android {
    namespace = "org.home.tracker"
    compileSdk = 37

    defaultConfig {
        applicationId = "org.home.tracker"
        minSdk = 34
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    val roomVersion = "3.0.3"
    val chartsVersion = "3.3.1"
    val navigationVersion = "2.10.1"

    implementation("androidx.core:core-ktx:1.19.0")
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.2")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-compose:$navigationVersion")

    implementation("androidx.room3:room3-runtime:${roomVersion}")
    ksp("androidx.room3:room3-compiler:$roomVersion")

    //Charts
    implementation("com.patrykandpatrick.vico:compose:$chartsVersion")
    implementation("com.patrykandpatrick.vico:compose-m3:$chartsVersion")

    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.material:material:1.12.1")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.12.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.12.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}