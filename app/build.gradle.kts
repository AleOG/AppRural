plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.proyecto.apprural"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.proyecto.apprural"
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

    dataBinding {
        enable = true
    }

    viewBinding {
        enable = true
    }
}


buildscript {
    repositories {
        // Repositorios necesarios para el sistema de compilación
        google()  // Repositorio de Google para las herramientas de Android
        mavenCentral()  // Repositorio central de Maven
        // Otros repositorios si es necesario
    }
    dependencies {
        // Dependencias para el sistema de compilación
        classpath ("com.google.gms:google-services:4.3.14")
    }
}


dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation (platform("com.google.firebase:firebase-bom:27.1.0"))
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth:23.0.0")
    implementation ("com.google.firebase:firebase-messaging")
    implementation ("com.google.firebase:firebase-database")
    implementation ("com.google.firebase:firebase-config")

    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}