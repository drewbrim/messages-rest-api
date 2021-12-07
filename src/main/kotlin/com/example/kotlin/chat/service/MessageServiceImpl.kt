package com.example.kotlin.chat.service

import com.example.kotlin.chat.extentions.uuid
import com.example.kotlin.chat.repository.ContentType
import com.example.kotlin.chat.repository.Message
import com.example.kotlin.chat.repository.MessageRespository
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

import com.google.gson.Gson

import java.net.URL
import java.time.Instant
import java.util.*

@Service
@Primary
class MessageServiceImpl(val messageRepository: MessageRespository): MessageService {

    private fun createProducer(): Producer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = "localhost:9092"
        props["acks"] = "all"
        props["retries"] = 0
        props["linger.ms"] = 1
        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"

        return KafkaProducer(props)
    }

    private val producer = createProducer()

    override fun latest(): List<MessageVM> =
        messageRepository.findLatest()
            .map { with(it) { MessageVM(content, UserVM(username,
                URL(user_avatar_image_link)
            ), sent, created_on, id) } }

    override fun findMessagesById(id: String): Optional<MessageVM>  = messageRepository.findById(id)
        .map { with(it) { MessageVM(content, UserVM(username,
            URL(user_avatar_image_link)
        ), sent, created_on, id) } }

    override fun findMessages(): List<MessageVM> = messageRepository.findAll()
        .map { with(it) { MessageVM(content, UserVM(username,
            URL(user_avatar_image_link)
        ), sent, created_on, id) } }


    override fun after(lastMessageId: String): List<MessageVM>  =
        messageRepository.findLatest(lastMessageId)
            .map { with(it) { MessageVM(content, UserVM(username,
                URL(user_avatar_image_link)), sent, created_on, id) } }

    override fun post(messageVM: MessageVM) {
        val message = with(messageVM) { Message(content, ContentType.PLAIN, sent,
                user.name, user.avatarImageLink.toString(), created_on ?: Instant.now().toEpochMilli(), id ?: content.uuid())}
        val gson = Gson();
        val topicMessage = ProducerRecord(
            "message.example", // topic
            message.id,
            gson.toJson(message)
        )
        this.producer.send(topicMessage)
    }

}