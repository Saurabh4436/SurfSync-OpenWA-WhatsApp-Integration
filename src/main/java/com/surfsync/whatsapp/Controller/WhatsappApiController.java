package com.surfsync.whatsapp.Controller;

import com.surfsync.whatsapp.Dto.CreateSessionRequest;
import com.surfsync.whatsapp.Service.OpenWaService;
import com.surfsync.whatsapp.Dto.SendMediaRequest;
import com.surfsync.whatsapp.Service.WhatsappMessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappApiController {

    private final OpenWaService openWaService;
    private final WhatsappMessageService messageService;

    public WhatsappApiController(OpenWaService openWaService, WhatsappMessageService messageService) {

        this.openWaService = openWaService;
        this.messageService = messageService;
    }

    @PostMapping("/session")
    public ResponseEntity<String> createSession(@Valid @RequestBody CreateSessionRequest request) {

        return ResponseEntity.ok(openWaService.createSession(request.getName()));
    }

    @PostMapping("/send-media")
    public ResponseEntity<String> sendMedia(@Valid @RequestBody SendMediaRequest request) {

        String chatId = request.getPhone() + "@c.us";

        return ResponseEntity.ok(openWaService.sendMedia(chatId, request.getMediaType(), request.getUrl(), request.getCaption(), request.getFilename(), request.getMimetype()));
    }

    @GetMapping("/chats")
    public ResponseEntity<String> getChats() {

        return ResponseEntity.ok(openWaService.getChats());
    }

    @GetMapping("/inbox")
    public ResponseEntity<List<Map<String, Object>>> getInbox(@RequestParam(defaultValue = "50") int limit) {

        return ResponseEntity.ok(messageService.getRecentMessages(limit));
    }
}