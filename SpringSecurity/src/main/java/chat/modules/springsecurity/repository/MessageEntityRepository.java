package chat.modules.springsecurity.repository;

import chat.modules.springsecurity.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageEntityRepository extends JpaRepository<MessageEntity, Long> {
}
