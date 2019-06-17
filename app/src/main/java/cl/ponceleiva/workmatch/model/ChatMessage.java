package cl.ponceleiva.workmatch.model;

import com.google.firebase.Timestamp;

public class ChatMessage {
    private String messageId;
    private String userId;
    private Timestamp date;
    private String content;

    public ChatMessage(String messageId, String userId, Timestamp date, String content) {
        this.messageId = messageId;
        this.userId = userId;
        this.date = date;
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
