package com.fondosGFT.fondosGFT.repository.client;
import com.fondosGFT.fondosGFT.model.client.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
}
