package com.surfsync.whatsapp.Controller;

import com.surfsync.whatsapp.Service.OpenWaService;
import com.surfsync.whatsapp.Service.WhatsappMessageService;
import tools.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappWebhookController {

    private final OpenWaService openWaService;
    private final WhatsappMessageService messageService;

    public WhatsappWebhookController(OpenWaService openWaService, WhatsappMessageService messageService) {
        this.openWaService = openWaService;
        this.messageService = messageService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody JsonNode payload) {

        System.out.println("========== OPENWA WEBHOOK ==========");
        System.out.println(payload.toPrettyString());
        System.out.println("====================================");

        JsonNode data = payload.path("data");

        String messageId = data.path("id").asText("");
        String from = data.path("from").asText("");
        String chatId = data.path("chatId").asText("");
        String body = data.path("body").asText("");
        String type = data.path("type").asText("unknown");
        boolean fromMe = data.path("fromMe").asBoolean(false);

        // Group / Channel identify karo
        boolean isGroup = data.path("isGroup").asBoolean(false);
        boolean isNewsletter = chatId.endsWith("@newsletter");

        if (!fromMe && "text".equalsIgnoreCase(type)) {

            if (isGroup || chatId.endsWith("@g.us") || isNewsletter) {

                System.out.println(" Ignored group/newsletter message from: " + chatId);

                return ResponseEntity.ok("Group/newsletter ignored");
            }

            String incomingMessage = body.trim();
            String normalizedMessage = incomingMessage.toLowerCase();

            messageService.saveIncomingMessage(messageId, from, incomingMessage, "TEXT", "RECEIVED");

            String reply = null;

            if (normalizedMessage.equals("hello") || normalizedMessage.equals("helo") || normalizedMessage.equals("hi") || normalizedMessage.equals("menu") || normalizedMessage.equals("0")) {

                reply = """
                        Welcome to SurfSync Infotech!
                        
                        Please choose an option:
                        
                        1. Website Development
                        2. App Development
                        3. Internship
                        4. Contact Sales
                        
                        Reply with 1, 2, 3 or 4.
                        """;
            }

            // 3. Website Development
            else if (normalizedMessage.equals("1")) {

                reply = """
                        Website Development
                        
                        SurfSync Infotech provides modern, responsive
                        and business-focused website development.
                        
                        Services include:
                        • Business websites
                        • Landing pages
                        • E-commerce websites
                        • Custom web applications
                        
                        Reply 0 to return to the main menu.
                        """;
            }

            // 4. App Development
            else if (normalizedMessage.equals("2")) {

                reply = """
                        App Development
                        
                        We build scalable mobile applications
                        tailored to business requirements.
                        
                        Services include:
                        • Android applications
                        • iOS applications
                        • Cross-platform apps
                        • API integrations
                        
                        Reply 0 to return to the main menu.
                        """;
            }

            // 5. Internship
            else if (normalizedMessage.equals("3")) {

                reply = """
                        🎓 Internship at SurfSync Infotech
                        
                        We offer internship opportunities for
                        students and aspiring developers.
                        
                        Areas may include:
                        • Java / Spring Boot
                        • Web Development
                        • Mobile Development
                        • API Integration
                        
                        Reply 0 to return to the main menu.
                        """;
            }

            // 6. Contact Sales
            else if (normalizedMessage.equals("4")) {

                reply = """
                        Contact Sales
                        
                        Thank you for your interest in SurfSync Infotech.
                        
                        Our sales team can help you with:
                        • Project requirements
                        • Website development
                        • App development
                        • Custom software solutions
                        
                        Please share your requirement and our team
                        will get in touch with you.
                        
                        Reply 0 to return to the main menu.
                        """;
            }

            // 7. Invalid option
            else {

                reply = """
                        Sorry, I didn't understand that.
                        
                        Please choose:
                        
                        1. Website Development
                        2. App Development
                        3. Internship
                        4. Contact Sales
                        
                        Reply with 1, 2, 3 or 4.
                        """;
            }

            // 8. Send reply + DB logging
            try {

                String openWaResponse = openWaService.sendTextMessageByChatId(chatId, reply);

                // Bot reply ko bhi DB mein save karo
                messageService.saveOutgoingMessage(chatId, reply, "SENT");

                System.out.println(" Auto-reply sent to: " + chatId);
                System.out.println("OpenWA response: " + openWaResponse);

            } catch (Exception e) {

                System.err.println(" Auto-reply failed for " + chatId + ": " + e.getMessage());
            }
        }

        return ResponseEntity.ok("Webhook received");
    }
}