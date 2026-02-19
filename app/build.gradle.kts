plugins { alias(libs.plugins.android.application) }

android {
    namespace = "me.zhanghai.android.textselectionwebsearch"
    buildToolsVersion = libs.versions.android.buildTools.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    enableKotlin=false

    defaultConfig {
        applicationId = "me.zhanghai.android.textselectionwebsearch"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 7
        versionName = "1.1.3"
    }

    signingConfigs {
        create("release") {
            storeFile = System.getenv("STORE_FILE")?.let { rootProject.file(it) }
            storePassword = System.getenv("STORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    lint { disable += "MissingTranslation" }
}

dependencies {
    implementation(libs.androidx.annotation) { exclude(group = "org.jetbrains.kotlin") }
    implementation(libs.androidx.browser) {
        exclude(group = "androidx.annotation", module = "annotation-experimental")
        exclude(group = "androidx.collection")
        exclude(group = "androidx.concurrent")
        exclude(group = "androidx.interpolator")
        exclude(group = "androidx.lifecycle")
        exclude(group = "androidx.versionedparcelable")
        exclude(group = "com.google.guava")
        exclude(group = "org.jetbrains.kotlin")
    }
}
