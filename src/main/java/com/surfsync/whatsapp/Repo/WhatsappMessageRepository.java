package com.surfsync.whatsapp.Repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class WhatsappMessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public WhatsappMessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveIncomingMessage(String messageId, String phoneNumber, String direction, String message, String messageType, String status) {

        jdbcTemplate.update("""
                INSERT INTO whatsapp_messages
                (message_id, phone_number, direction, message, message_type, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """, messageId, phoneNumber, direction, message, messageType, status);
    }

    public void saveOutgoingMessage(String phoneNumber, String direction, String message, String messageType, String status) {

        jdbcTemplate.update("""
                INSERT INTO whatsapp_messages
                (phone_number, direction, message, message_type, status)
                VALUES (?, ?, ?, ?, ?)
                """, phoneNumber, direction, message, messageType, status);
    }

    public List<Map<String, Object>> findRecentMessages(int limit) {

        return jdbcTemplate.queryForList("""
                SELECT
                    id,
                    message_id,
                    phone_number,
                    direction,
                    message,
                    message_type,
                    status,
                    created_at
                FROM whatsapp_messages
                ORDER BY created_at DESC, id DESC
                LIMIT ?
                """, limit);
    }
}