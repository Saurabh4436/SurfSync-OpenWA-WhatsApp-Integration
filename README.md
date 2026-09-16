# SurfSync WhatsApp Integration

A WhatsApp integration service developed for the SurfSync Infotech internship assignment.

The project integrates OpenWA with a Spring Boot backend, MySQL database, webhook processing, a rule-based chatbot, and a web dashboard.

## Features

* OpenWA WhatsApp session integration
* QR-based WhatsApp authentication
* Session status monitoring
* Text message sending
* Image, video, audio and document media support
* OpenWA webhook integration
* Incoming message storage
* Outgoing message storage
* MySQL message history
* Rule-based WhatsApp chatbot
* Web-based administration dashboard
* Group and newsletter message filtering
* Basic input validation and error handling

## Technology Stack

* Java
* Spring Boot
* Spring Web
* Spring JDBC
* MySQL
* OpenWA
* WhatsApp Web.js
* HTML
* CSS
* JavaScript
* Maven

## Architecture

```text
WhatsApp User
      |
      v
    OpenWA
      |
      | REST API / Webhook
      v
Spring Boot Backend
      |
      +------------------+
      |                  |
      v                  v
   MySQL             Dashboard
   Database          HTML/CSS/JS
```

## Project Structure

```text
whatsapp-integration/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── surfsync/
│       │           └── whatsapp/
│       │               ├── controller/
│       │               ├── service/
│       │               ├── repository/
│       │               └── dto/
│       │
│       └── resources/
│           └── static/
│               ├── index.html
│               ├── style.css
│               └── app.js
│
├── pom.xml
├── README.md
├── .gitignore
└── .env.example
```

## OpenWA

Start OpenWA before starting the Spring Boot application.

Default OpenWA URL:

```text
http://localhost:2785
```

OpenWA API documentation:

```text
http://localhost:2785/api/docs
```

The OpenWA API is protected using an API key.

## Spring Boot

Start the application from the project directory:

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

Dashboard:

```text
http://localhost:8080
```

Health endpoint:

```text
http://localhost:8080/api/health
```

## REST APIs

### Create WhatsApp Session

```http
POST /api/whatsapp/session
```

Example:

```json
{
  "name": "my-bot"
}
```

### WhatsApp Session Status

```http
GET /api/whatsapp/status
```

### Send Text Message

```http
POST /api/whatsapp/send-message
```

Example:

```json
{
  "phone": "919XXXXXXXXX",
  "message": "Hello from SurfSync!"
}
```

### Send Media

```http
POST /api/whatsapp/send-media
```

Example:

```json
{
  "phone": "919XXXXXXXXX",
  "mediaType": "image",
  "url": "https://example.com/image.jpg",
  "caption": "SurfSync"
}
```

Supported media types:

* image
* video
* audio
* document

### Upload Media

```http
POST /api/whatsapp/upload-media
```

This endpoint accepts a multipart file upload and returns a local media URL used by OpenWA.

### OpenWA Chats

```http
GET /api/whatsapp/chats
```

### MySQL Inbox

```http
GET /api/whatsapp/inbox?limit=50
```

### Webhook

```http
POST /api/whatsapp/webhook
```

OpenWA sends incoming WhatsApp messages to the webhook.

## Webhook Flow

```text
WhatsApp Message
       |
       v
OpenWA
       |
       v
POST /api/whatsapp/webhook
       |
       v
Spring Boot
       |
       +-----------> MySQL
       |
       v
Chatbot Rule Processing
       |
       v
OpenWA Send Message
       |
       v
WhatsApp User
```

## Chatbot

The application contains a simple rule-based chatbot.

Users can send:

```text
Hello
```

The bot responds with:

```text
Welcome to SurfSync Infotech! 👋

Please choose an option:

1. Website Development
2. App Development
3. Internship
4. Contact Sales
```

Supported options:

```text
0 - Main Menu
1 - Website Development
2 - App Development
3 - Internship
4 - Contact Sales
```

Messages from WhatsApp groups and newsletter/channel chats are ignored by the chatbot.

## Database

Database:

```text
surfsync_whatsapp
```

Main table:

```sql
CREATE TABLE IF NOT EXISTS whatsapp_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id VARCHAR(255) NULL,
    phone_number VARCHAR(30) NOT NULL,
    direction VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    message_type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

The database stores:

* Message ID
* Phone/chat identifier
* Direction
* Message
* Message type
* Status
* Timestamp

## Dashboard

The dashboard provides:

* WhatsApp session status
* Connected WhatsApp number
* OpenWA engine status
* Text message sending
* Media attachment through the `+` button
* Incoming/outgoing message history
* MySQL-backed inbox

The attachment menu supports:

```text
+ 
 ├── Image
 ├── Video
 ├── Audio
 └── Document
```

## Environment Configuration

Sensitive configuration should remain local.

Use:

```text
.env.example
```

as a template.

Never commit:

```text
.env
OpenWA API keys
MySQL passwords
Session credentials
OpenWA .api-key
Local uploads
```

## Demo Flow

The recommended demonstration sequence is:

```text
1. Start OpenWA
2. Show WhatsApp session as READY
3. Open SurfSync Dashboard
4. Show connected WhatsApp number
5. Send a WhatsApp text message
6. Verify message delivery
7. Send "Hello" from WhatsApp
8. Show automatic chatbot menu
9. Select options 1, 2, 3 and 4
10. Show incoming/outgoing messages in dashboard
11. Show MySQL message history
12. Demonstrate media attachment if required
```

## Error Handling

The application validates common failures including:

* Empty phone number
* Invalid phone number
* Empty text message
* Invalid media URL
* Unsupported media type
* OpenWA session problems
* Message send failures
* Media upload failures

## Security

API keys, database passwords and WhatsApp session credentials should never be committed to a public repository.

Keep secrets in environment variables or local configuration.

## Assignment Completion

The implementation demonstrates:

```text
WhatsApp
   ↓
OpenWA
   ↓
Spring Boot
   ↓
MySQL
   ↓
Webhook
   ↓
Rule-Based Chatbot
   ↓
OpenWA
   ↓
WhatsApp
```

The project also includes a browser-based dashboard for messaging and message history.
