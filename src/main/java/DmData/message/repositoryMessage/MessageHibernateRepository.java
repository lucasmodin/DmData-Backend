package DmData.message.repositoryMessage;


import DmData.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageHibernateRepository extends JpaRepository<Message, Integer> {
}
