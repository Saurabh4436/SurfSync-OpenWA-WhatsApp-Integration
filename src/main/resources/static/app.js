const API_BASE = "/api/whatsapp";

document.addEventListener("DOMContentLoaded", () => {
    loadDashboard();

    document
        .getElementById("sendMessageForm")
        .addEventListener("submit", sendMessage);


});


async function loadDashboard() {
    await loadStatus();
    await loadMessages();
}


async function loadStatus() {

    const statusElement =
        document.getElementById("sessionStatus");

    const phoneElement =
        document.getElementById("phoneNumber");

    const engineElement =
        document.getElementById("engineStatus");

    try {

        const response = await fetch(
            `${API_BASE}/status`
        );

        if (!response.ok) {
            throw new Error(
                `HTTP ${response.status}`
            );
        }

        const data = await response.json();

        statusElement.textContent =
            data.status || "Unknown";

        phoneElement.textContent =
            data.phone || "-";

        engineElement.textContent =
            data.engineLoaded
                ? "Running"
                : "Stopped";

        if (data.status === "ready") {
            statusElement.style.color = "#16a34a";
        } else {
            statusElement.style.color = "#dc2626";
        }

        if (data.engineLoaded) {
            engineElement.style.color = "#16a34a";
        } else {
            engineElement.style.color = "#dc2626";
        }

    } catch (error) {

        statusElement.textContent =
            "Unavailable";

        phoneElement.textContent =
            "-";

        engineElement.textContent =
            "Unavailable";

        statusElement.style.color =
            "#dc2626";

        console.error(
            "Status error:",
            error
        );
    }
}


async function sendMessage(event) {

    event.preventDefault();

    const phone =
        document.getElementById("phone")
            .value
            .trim();

    const message =
        document.getElementById("message")
            .value
            .trim();

    const result =
        document.getElementById("sendResult");

    const sendButton =
        document.getElementById("sendBtn");


    if (!phone) {
        showResult(
            "Please enter a phone number.",
            "error"
        );

        return;
    }

    if (!/^\d{10,15}$/.test(phone)) {
        showResult(
            "Enter a valid phone number with country code.",
            "error"
        );

        return;
    }

    if (!message) {
        showResult(
            "Message cannot be empty.",
            "error"
        );

        return;
    }


    sendButton.disabled = true;
    sendButton.textContent =
        "Sending...";


    try {

        const response = await fetch(
            `${API_BASE}/send-message`,
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body: JSON.stringify({
                    phone,
                    message
                })
            }
        );

        const text =
            await response.text();

        if (!response.ok) {

            throw new Error(
                text ||
                `HTTP ${response.status}`
            );
        }


        showResult(
            "Message sent successfully ✅",
            "success"
        );

        document.getElementById(
            "message"
        ).value = "";

        await loadMessages();

    } catch (error) {

        console.error(
            "Send message error:",
            error
        );

        showResult(
            `Failed to send message: ${error.message}`,
            "error"
        );

    } finally {

        sendButton.disabled = false;

        sendButton.textContent =
            "Send Message";
    }
}


async function loadMessages() {

    const container =
        document.getElementById("messagesContainer");

    container.innerHTML =
        `<div class="loading">
            Loading messages...
        </div>`;

    try {

        const response = await fetch(
            `${API_BASE}/inbox?limit=50`
        );

        if (!response.ok) {
            throw new Error(
                `HTTP ${response.status}`
            );
        }

        const messages =
            await response.json();

        if (!messages.length) {

            container.innerHTML =
                `<div class="empty">
                    No messages found.
                </div>`;

            return;
        }

        container.innerHTML =
            messages
                .map(createDatabaseMessageHtml)
                .join("");

    } catch (error) {

        console.error(
            "Inbox error:",
            error
        );

        container.innerHTML =
            `<div class="empty">
                Failed to load inbox.
            </div>`;
    }
}


function createDatabaseMessageHtml(message) {

    const phone =
        escapeHtml(
            message.phone_number || "-"
        );

    const body =
        escapeHtml(
            message.message || ""
        );

    const direction =
        String(message.direction || "")
            .toUpperCase();

    const directionClass =
        direction === "OUTGOING"
            ? "outgoing"
            : "incoming";

    const timestamp =
        message.created_at
            ? formatTimestamp(
                message.created_at
            )
            : "-";

    const status =
        escapeHtml(
            message.status || ""
        );

    return `
        <div class="message">

            <div class="message-top">

                <span class="message-phone">
                    ${phone}
                </span>

                <span class="message-time">
                    ${timestamp}
                </span>

            </div>

            <div class="message-body">
                ${body}
            </div>

            <span class="direction ${directionClass}">
                ${direction}
            </span>

            <span class="direction">
                ${status}
            </span>

        </div>
    `;
}

function showResult(text, type) {

    const result =
        document.getElementById(
            "sendResult"
        );

    result.textContent = text;

    result.className =
        `result ${type}`;
}


function formatTimestamp(timestamp) {

    let date;

    if (
        typeof timestamp === "number"
        && timestamp < 10000000000
    ) {
        date =
            new Date(timestamp * 1000);
    } else {
        date =
            new Date(timestamp);
    }

    if (isNaN(date.getTime())) {
        return "-";
    }

    return date.toLocaleString();
}


function escapeHtml(value) {

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}