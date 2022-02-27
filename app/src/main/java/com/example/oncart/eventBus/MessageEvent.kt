package com.example.oncart.eventBus

data class MessageEvent(
    var str: String
) {
    fun getString(): String {
        return str
    }
}