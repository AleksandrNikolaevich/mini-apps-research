package com.mainapp

import android.view.View
import com.facebook.react.bridge.Arguments
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

class MiniAppViewEventEmitter(context: ThemedReactContext) {
    private val emitter = context.getJSModule(RCTEventEmitter::class.java)
    private var id = View.NO_ID

    fun onStartLoad() {
        emitter.receiveEvent(id, ON_START_LOAD, null)
    }

    fun onEndLoad() {
        emitter.receiveEvent(id, ON_END_LOAD, null)
    }

    fun onError(code: ErrorCode, message: String) {
        val params = Arguments.createMap().apply {
            putString("code", code.toString())
            putString("message", message)
        }
        emitter.receiveEvent(id, ON_ERROR, params)
    }

    fun setId(id: Int) {
        this.id = id
    }

    companion object {
        private const val ON_START_LOAD = "onStartLoad"
        private const val ON_END_LOAD = "onEndLoad"
        private const val ON_ERROR = "onError"

        val events = setOf(
            ON_START_LOAD,
            ON_END_LOAD,
            ON_ERROR
        )
    }
}

enum class ErrorCode {
    RUN
}