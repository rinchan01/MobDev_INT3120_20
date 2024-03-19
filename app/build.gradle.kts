plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.planegame"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.planegame"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    splits {
        abi {
            isEnable = true
            isUniversalApk = true
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

    sourceSets["main"].apply {
        manifest.srcFile("src/main/AndroidManifest.xml")
        java.setSrcDirs(listOf("src/main/java"))
        jniLibs.setSrcDirs(listOf("libs"))
        assets.setSrcDirs(listOf("src/main/assets"))
    }
}

configurations {
    create("natives")
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("com.badlogicgames.gdx:gdx:1.12.1")
    implementation("com.badlogicgames.gdx:gdx-backend-android:1.12.1")
    "natives"("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-arm64-v8a")
    "natives"("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-armeabi-v7a")
    "natives"("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-x86")
    "natives"("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-x86_64")
    implementation("com.badlogicgames.gdx:gdx-freetype:1.12.1")
    "natives"("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-armeabi-v7a")
    "natives"("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-arm64-v8a")
    "natives"("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-x86")
    "natives"("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-x86_64")

}

val copyAndroidNatives = tasks.register("copyAndroidNatives") {
    doFirst {
        file("libs/armeabi-v7a/").mkdirs()
        file("libs/arm64-v8a/").mkdirs()
        file("libs/x86_64/").mkdirs()
        file("libs/x86/").mkdirs()

        configurations["natives"].copy().files.forEach { jar ->
            var outputDir: File? = null
            if (jar.name.endsWith("natives-armeabi-v7a.jar")) outputDir = file("libs/armeabi-v7a")
            if (jar.name.endsWith("natives-arm64-v8a.jar")) outputDir = file("libs/arm64-v8a")
            if (jar.name.endsWith("natives-x86_64.jar")) outputDir = file("libs/x86_64")
            if (jar.name.endsWith("natives-x86.jar")) outputDir = file("libs/x86")
            outputDir?.let {
                copy {
                    from(zipTree(jar))
                    into(it)
                    include("*.so")
                }
            }
        }
    }
}