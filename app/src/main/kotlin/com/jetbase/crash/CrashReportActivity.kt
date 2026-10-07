package com.jetbase.crash

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.jetbase.BuildConfig
import com.jetbase.R
import com.jetbase.core.designsystem.theme.JetBaseTheme
import kotlin.system.exitProcess

/**
 * "The app crashed" screen with a copyable stack trace, like Seal's CrashReportActivity.
 *
 * Declared in the manifest with `android:process=":crash"`: it runs in a SEPARATE process because
 * the main process is broken and has to be killed.
 */
class CrashReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val report = intent.getStringExtra(EXTRA_REPORT).orEmpty()
        setContent {
            JetBaseTheme {
                Scaffold(
                    bottomBar = {
                        Button(
                            onClick = {
                                copyToClipboard(report)
                                finishAffinity()
                            },
                            modifier =
                                Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp),
                        ) {
                            Text(stringResource(R.string.crash_copy_and_exit))
                        }
                    }
                ) { padding ->
                    Column(
                        Modifier.fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(
                            stringResource(R.string.crash_title),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Text(
                            report,
                            style =
                                MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace
                                ),
                            modifier = Modifier.padding(top = 16.dp),
                        )
                    }
                }
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("crash report", text))
    }

    companion object {
        private const val EXTRA_REPORT = "crash_report"

        /** Installs the global handler for uncaught exceptions. Call it from Application. */
        fun install(application: Application) {
            val previous = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                try {
                    application.startActivity(
                        Intent(application, CrashReportActivity::class.java)
                            .putExtra(EXTRA_REPORT, buildReport(throwable))
                            .addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            )
                    )
                    Process.killProcess(Process.myPid())
                    exitProcess(10)
                } catch (_: Throwable) {
                    // If even this fails, delegate to the system's default handler.
                    previous?.uncaughtException(thread, throwable)
                }
            }
        }

        private fun buildReport(throwable: Throwable): String = buildString {
            appendLine("App: ${BuildConfig.APPLICATION_ID} ${BuildConfig.VERSION_NAME}")
            appendLine("Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            appendLine("Device: ${Build.MANUFACTURER} ${Build.MODEL}")
            appendLine("ABIs: ${Build.SUPPORTED_ABIS.joinToString()}")
            appendLine()
            append(throwable.stackTraceToString())
        }
    }
}
