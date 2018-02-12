package io.github.deweyreed.hmspicker.sample

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * Created on 2018/2/12.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }
}