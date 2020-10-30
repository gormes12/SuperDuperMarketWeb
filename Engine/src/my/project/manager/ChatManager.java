package my.project.manager;

import chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    private final List<ChatMessage> chatDataList;

    public ChatManager() {
        chatDataList = new ArrayList<>();
    }

    public void addChatMessage(String chatString, String username) {
        chatDataList.add(new ChatMessage(chatString, username));
    }

    public List<ChatMessage> getChatEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > chatDataList.size()) {
            fromIndex = 0;
        }
        return chatDataList.subList(fromIndex, chatDataList.size());
    }

    public int getVersion() {
        return chatDataList.size();
    }
}
