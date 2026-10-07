package com.jetbase

import android.app.Application
import com.jetbase.crash.CrashReportActivity
import com.jetbase.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Process entry point. Compared with Seal's App.kt (~200 lines of lateinit globals, a static
 * Context, native binary init, services...), here we ONLY start Koin and the crash handler.
 * Everything else is created lazily when somebody needs it.
 */
class JetBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrashReportActivity.install(this)
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.INFO else Level.NONE)
            androidContext(this@JetBaseApplication)
            modules(appModule)
        }
    }
}
