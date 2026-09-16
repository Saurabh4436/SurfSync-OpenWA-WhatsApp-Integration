package com.surfsync.whatsapp.Dto;

import jakarta.validation.constraints.NotBlank;

public class SendMediaRequest {

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Media type is required")
    private String mediaType;

    @NotBlank(message = "Media URL is required")
    private String url;

    private String caption;

    private String filename;

    private String mimetype;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
}