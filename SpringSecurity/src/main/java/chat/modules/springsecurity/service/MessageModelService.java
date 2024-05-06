package chat.modules.springsecurity.service;

import chat.modules.springsecurity.model.MessageModel;

public interface MessageModelService {
    void save(MessageModel message);
    void send(MessageModel message);
    void consume(MessageModel message);

}
