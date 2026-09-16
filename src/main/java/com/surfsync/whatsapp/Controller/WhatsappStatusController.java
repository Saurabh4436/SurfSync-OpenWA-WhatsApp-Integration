package com.surfsync.whatsapp.Controller;

import com.surfsync.whatsapp.Service.OpenWaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappStatusController {

    private final OpenWaService openWaService;

    public WhatsappStatusController(OpenWaService openWaService) {
        this.openWaService = openWaService;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {

        String status = openWaService.getSessionStatus();

        return ResponseEntity.ok(status);
    }
}