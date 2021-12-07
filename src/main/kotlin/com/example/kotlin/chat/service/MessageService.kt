package com.example.kotlin.chat.service

import java.util.*

interface MessageService {

    fun latest(): List<MessageVM>

    fun findMessagesById(id: String): Optional<MessageVM>

    fun findMessages(): List<MessageVM>

    fun after(lastMessageId: String): List<MessageVM>

    fun post(message: MessageVM)
}