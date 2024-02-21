import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    alias(libs.plugins.junit5) apply false
    alias(libs.plugins.ktlint)
}

@Suppress("ktlint:standard:property-naming")
private val ANDROID_BASE_PLUGIN_ID = "com.android.base"

@Suppress("ktlint:standard:property-naming")
private val ANDROID_PLUGIN_IDS =
    listOf(
        "android",
        "android-library",
        "com.android.feature",
        "com.android.instantapp",
        "com.android.library",
        "com.android.test",
    )

val Project.isAndroidProject: Boolean
    get() =
        plugins.hasPlugin(ANDROID_BASE_PLUGIN_ID) ||
            ANDROID_PLUGIN_IDS.any { pluginId -> plugins.hasPlugin(pluginId) }

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<KtlintExtension> {
        version.set("1.1.1")
        outputColorName.set("RED")
        android.set(true)
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }
}

subprojects {
    tasks {
        afterEvaluate {
            val predicate: (Task) -> Boolean = { task: Task ->
                if (isAndroidProject) {
                    task.name.startsWith("preBuild")
                } else {
                    task.name.startsWith("compile")
                }
            }
            container
                .filter { predicate(it) }
                .forEach { task ->
                    task.dependsOn("ktlintCheck")
                }
        }
    }
}
