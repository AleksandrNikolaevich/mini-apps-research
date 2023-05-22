package com.mainapp

import android.app.Application
import android.view.Choreographer
import android.widget.RelativeLayout
import android.widget.Toast
import com.facebook.hermes.reactexecutor.HermesExecutorFactory
import com.facebook.react.PackageList
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.common.LifecycleState
import com.facebook.react.uimanager.ThemedReactContext

class MiniAppView(private val application: Application, private val context: ThemedReactContext) :
    RelativeLayout(context) {
    private val eventEmitter = MiniAppViewEventEmitter(context)

    private lateinit var reactRootView: ReactRootView
    private lateinit var reactInstanceManager: ReactInstanceManager
    private val packages = PackageList(application).packages

    init {}

    fun runApp(url: String, moduleName: String) {
        eventEmitter.onStartLoad()
        BundleUtils.download(context, url) { path ->
            createReactInstance(path, moduleName)
        }
    }

    fun dispatch() {
        Toast.makeText(context, "dispatched props", Toast.LENGTH_SHORT).show()
    }

    private fun createReactInstance(path: String, moduleName: String) {
        reactInstanceManager = ReactInstanceManager.builder().setApplication(application)
            .setCurrentActivity(context.currentActivity)
            .setJSBundleFile(path)
            .addPackages(packages)
            .setUseDeveloperSupport(false)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .setJavaScriptExecutorFactory(HermesExecutorFactory())
            .setJSExceptionHandler {
                eventEmitter.onError(ErrorCode.RUN, it.message!!)
            }
            .build()

        reactRootView = ReactRootView(context)
        reactRootView.startReactApplication(reactInstanceManager, moduleName, null);
        reactRootView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        addView(reactRootView)

        eventEmitter.onEndLoad()
    }

    override fun setId(id: Int) {
        super.setId(id)
        eventEmitter.setId(id)
    }
}