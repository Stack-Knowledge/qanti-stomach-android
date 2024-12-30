plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    id("com.google.protobuf") version "0.9.4"
}

android {
    namespace = "com.qanti.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.qanti.myapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    protobuf {
        protoc {
            artifact = "com.google.protobuf:protoc:3.21.12"
        }
        generateProtoTasks {
            all().forEach { task ->
                task.builtins {
                    create("java") {
                        option("lite") // 경량화된 Protobuf 생성
                    }
                }
            }
        }
    }

}

dependencies {

    // 기존 의존성
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Retrofit 추가
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation(libs.protobuf.javalite)

    implementation(libs.androidx.hilt.navigation.compose)
    // Hilt 추
    implementation(libs.hilt.android)
    implementation(libs.androidx.navigation.compose) // Hilt Core 라이브러리
    ksp(libs.hilt.compiler)

    // 테스트 및 디버그 의존성
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
