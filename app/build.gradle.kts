plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.junit5)
}

@Suppress("ktlint:standard:property-naming")
object Version {
    val version_code: Int
        get() = (System.currentTimeMillis() / 60000).toInt()
    val version_name: String
        get() = "$major.$minor.$patch.$revision"

    private const val major = 0
    private const val minor = 0
    private const val patch = 1
    private const val revision = 0
}

android {
    namespace = "com.teamfilmo.filmo"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.teamfilmo.filmo"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = Version.version_code
        versionName = Version.version_name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("$rootDir/settings/debug.keystore")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.ksp)

    // Androidx
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.navigation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)

    implementation(libs.google.material)

    implementation(libs.bundles.datastore)
    implementation(libs.protobuf.java)
    implementation(libs.protobuf.kotlin)

    // firebase
    implementation(platform(libs.firebase.bom))

    // network
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.serialization)

    // Image
    implementation(libs.bundles.glide)
    ksp(libs.glide.compiler)
    implementation(libs.bundles.lottie)

    // hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // Logging
    implementation(libs.timber)

    // LeakCanary
    debugImplementation(libs.leakcanary)

    // Flipper
    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.network)
    debugImplementation(libs.flipper.soloader)
    debugImplementation(libs.flipper.leakcanary)

    // Test
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlin.reflection)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.junit.ktx)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.androidx.test.monitor)

    testImplementation(libs.truth)
    testImplementation(libs.truth.ext)

    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)

    testImplementation(libs.hilt.test)
    kaptTest(libs.hilt.compiler)

    testImplementation(libs.okhttp.mockwebserver)

    // AndroidTest
    androidTestImplementation(libs.junit5.api)
    androidTestRuntimeOnly(libs.junit5.engine)
    androidTestImplementation(libs.junit5.android.test)
    androidTestRuntimeOnly(libs.junit5.android.runner)

    androidTestImplementation(libs.kotlin.test.junit)
    androidTestImplementation(libs.kotlin.reflection)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.turbine)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.junit.ktx)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.monitor)

    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)

    androidTestImplementation(libs.hilt.test)
}

kapt {
    correctErrorTypes = true
}

val isArmProcessor: Boolean =
    Runtime.getRuntime().exec("uname -p")
        .inputStream
        .bufferedReader()
        .use { reader -> reader.readLine()?.trim() }
        .let { processor -> processor == "arm" }

protobuf {
    val protocClassifier = if (isArmProcessor) ":osx-x86_64" else ""
    val protobufVersion = libs.versions.protobuf.asProvider().get() + protocClassifier

    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                register("java") { option("lite") }
                register("kotlin") { option("lite") }
            }
        }
    }
}
