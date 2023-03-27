package org.ac.cst8277.islam.moin.repository;

import org.ac.cst8277.islam.moin.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message,String> {
}
