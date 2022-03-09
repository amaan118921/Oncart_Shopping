package com.example.oncart.eventBus

open class MessageEvent(
    var str: String
) {
    fun getString(): String {
        return str
    }
}