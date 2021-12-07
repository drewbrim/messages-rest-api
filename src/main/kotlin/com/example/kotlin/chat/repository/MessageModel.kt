package com.example.kotlin.chat.repository


import com.example.kotlin.chat.extentions.uuid
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant


@Table("messages")
data class Message(
    val content: String,
    val content_type: ContentType,
    val sent: Long?,
    val username: String,
    val user_avatar_image_link: String,
    val created_on: Long?,
    @Id val id: String?,
)

enum class ContentType {
    PLAIN
}