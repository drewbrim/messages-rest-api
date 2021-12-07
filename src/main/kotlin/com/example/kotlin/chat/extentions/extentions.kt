package com.example.kotlin.chat.extentions

import java.util.*

fun String.uuid(): String =
    UUID.nameUUIDFromBytes(this.encodeToByteArray()).toString()