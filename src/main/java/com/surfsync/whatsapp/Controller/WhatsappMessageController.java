package com.surfsync.whatsapp.Controller;

import com.surfsync.whatsapp.Service.OpenWaService;
import com.surfsync.whatsapp.Dto.SendMessageRequest;
import com.surfsync.whatsapp.Service.WhatsappMessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappMessageController {

    private final OpenWaService openWaService;
    private final WhatsappMessageService messageService;

    public WhatsappMessageController(OpenWaService openWaService, WhatsappMessageService messageService) {
        this.openWaService = openWaService;
        this.messageService = messageService;
    }

    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@Valid @RequestBody SendMessageRequest request) {

        String response = openWaService.sendTextMessage(request.getPhone(), request.getMessage());

        messageService.saveOutgoingMessage(request.getPhone(), request.getMessage(), "SENT");

        return ResponseEntity.ok(response);
    }
}