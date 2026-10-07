import com.jetbase.buildlogic.configureKotlinJvm
import com.jetbase.buildlogic.configureKtfmt
import com.jetbase.buildlogic.libs
import com.jetbase.buildlogic.library
import com.jetbase.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** `jetbase.jvm.library`: pure Kotlin module, no Android. Compiles and tests faster. */
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.pluginId("kotlin-jvm"))
            configureKotlinJvm()
            configureKtfmt()

            dependencies {
                add("testImplementation", libs.library("junit4"))
                add("testImplementation", libs.library("kotlinx-coroutines-test"))
            }
        }
    }
}
