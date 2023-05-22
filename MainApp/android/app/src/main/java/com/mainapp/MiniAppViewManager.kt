package com.mainapp

import android.app.Application
import android.view.View
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp


class MiniAppViewManager(private val application: Application) : SimpleViewManager<View>() {
    override fun getName(): String {
        return "MiniApp"
    }

    override fun createViewInstance(context: ThemedReactContext): MiniAppView {
        return MiniAppView(application, context)
    }

    @ReactProp(name = "source")
    fun setSource(view: MiniAppView, source: ReadableMap) {
        val url = source.getString("url")
        val moduleName = source.getString("moduleName")
        if (url.isNullOrEmpty() || moduleName.isNullOrEmpty()) {
            return
        }
        view.runApp(url, moduleName)
    }

    override fun getExportedCustomBubblingEventTypeConstants(): MutableMap<String, Any>? {
        val events = mutableMapOf<String, Any>()
        MiniAppViewEventEmitter.events.forEach {
            events[it] = mapOf(
                    "phasedRegistrationNames" to mapOf(
                        "bubbled" to it
                    )
            )
        }
        return events
    }

    override fun receiveCommand(root: View, commandId: String?, args: ReadableArray?) {
        super.receiveCommand(root, commandId, args)
        val view = root as? MiniAppView ?: return
        when (commandId?.toInt()) {
            COMMAND_DISPATCH -> {
                view.dispatch()
            }
        }
    }

    override fun getCommandsMap() = mapOf(
        COMMAND_DISPATCH_NAME to COMMAND_DISPATCH
    )

    override fun getConstants() = mapOf(
        COMMAND_DISPATCH_NAME to COMMAND_DISPATCH_NAME
    )

    companion object {
        private const val COMMAND_DISPATCH = 1
        private const val COMMAND_DISPATCH_NAME = "dispatch"
    }
}

