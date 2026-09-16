package com.surfsync.whatsapp.Service;

import com.surfsync.whatsapp.Repo.WhatsappMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WhatsappMessageService {

    private final WhatsappMessageRepository repository;

    public WhatsappMessageService(WhatsappMessageRepository repository) {
        this.repository = repository;
    }

    public void saveIncomingMessage(String messageId, String phoneNumber, String message, String messageType, String status) {

        repository.saveIncomingMessage(messageId, phoneNumber, "INCOMING", message, messageType, status);
    }

    public void saveOutgoingMessage(String phoneNumber, String message, String status) {

        repository.saveOutgoingMessage(phoneNumber, "OUTGOING", message, "TEXT", status);
    }

    public List<Map<String, Object>> getRecentMessages(int limit) {

        if (limit < 1) {
            limit = 20;
        }

        if (limit > 100) {
            limit = 100;
        }

        return repository.findRecentMessages(limit);
    }
}