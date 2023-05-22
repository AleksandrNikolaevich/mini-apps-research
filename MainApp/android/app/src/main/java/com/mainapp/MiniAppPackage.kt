package com.mainapp

import android.app.Application
import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager


class MiniAppPackage(private val application: Application) : ReactPackage {
    override fun createNativeModules(context: ReactApplicationContext): MutableList<NativeModule> {
        return mutableListOf()
    }

    override fun createViewManagers(context: ReactApplicationContext): MutableList<SimpleViewManager<View>> {
        return mutableListOf(
            MiniAppViewManager(application)
        )
    }
}