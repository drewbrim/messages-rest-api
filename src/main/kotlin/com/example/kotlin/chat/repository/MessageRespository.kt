package com.example.kotlin.chat.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface MessageRespository: CrudRepository<Message, String> {

    // language=SQL
    @Query("""
        SELECT * FROM messages
            ORDER BY sent DESC
            LIMIT 10
    """)
    fun findLatest(): List<Message>

    // language=SQL
    @Query("""
        SELECT * FROM messages
            WHERE sent > (SELECT sent FROM MESSAGES WHERE id = :id)
            ORDER BY sent DESC
    """)
    fun findLatest(@Param("id") id: String): List<Message>
}