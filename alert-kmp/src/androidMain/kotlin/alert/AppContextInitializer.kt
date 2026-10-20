package alert

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.startup.Initializer
import java.lang.ref.WeakReference

internal class AppContextInitializer : Initializer<Context> {
    override fun create(context: Context): Context {
        AppContext.setUp(context.applicationContext)
        return AppContext.get()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

internal object AppContext {
    private lateinit var application: Application
    private var resumed: WeakReference<Activity>? = null

    fun setUp(context: Context) {
        application = context as Application
        // Dialogs need an Activity, not the Application context, so remember the one in front.
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) { resumed = WeakReference(activity) }
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) { if (resumed?.get() == null) resumed = WeakReference(activity) }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) { if (resumed?.get() === activity) resumed = null }
        })
    }

    fun get(): Context {
        return if (AppContext::application.isInitialized.not()) throw Exception("Context is not initialized.")
        else application.applicationContext
    }

    /** The Activity in front, or null when the app has none (e.g. it runs in the background). */
    fun activity(): Activity? = resumed?.get()?.takeUnless { it.isFinishing || it.isDestroyed }

    /** Runs [block] on the main thread: views, dialogs and toasts can't be shown from other threads. */
    fun onMain(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) block() else Handler(Looper.getMainLooper()).post(block)
    }
}
