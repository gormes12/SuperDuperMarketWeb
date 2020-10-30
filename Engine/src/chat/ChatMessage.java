package chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final String chatString;
    private final String username;
    private final String time;

    public ChatMessage(String chatString, String username) {
        this.chatString = chatString;
        this.username = username;
        this.time = dtf.format(LocalDateTime.now());
    }

    public String getChatString() {
        return chatString;
    }

    public String getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return (username != null ? username + ": " : "") + chatString;
    }
}
