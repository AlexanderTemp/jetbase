import com.android.build.api.dsl.LibraryExtension
import com.jetbase.buildlogic.configureKotlinAndroid
import com.jetbase.buildlogic.configureKtfmt
import com.jetbase.buildlogic.libs
import com.jetbase.buildlogic.library
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/** `jetbase.android.library`: any core:* or feature:* module with Android code. */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            configureKtfmt()

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.animationsDisabled = true
                // Libraries ship no R8 rules of their own except consumer-rules.pro.
                defaultConfig.consumerProguardFiles("consumer-rules.pro")
            }

            dependencies {
                add("testImplementation", libs.library("junit4"))
                add("testImplementation", libs.library("kotlinx-coroutines-test"))
            }
        }
    }
}
