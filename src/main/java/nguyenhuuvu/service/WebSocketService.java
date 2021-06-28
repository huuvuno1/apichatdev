package nguyenhuuvu.service;

public interface WebSocketService {
    void sendNotice(String fromUser, String avatar, String toUser, String content, String link);
}
