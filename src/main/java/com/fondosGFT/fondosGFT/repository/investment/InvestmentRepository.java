package com.fondosGFT.fondosGFT.repository.investment;

import com.fondosGFT.fondosGFT.model.investment.Investment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvestmentRepository extends MongoRepository<Investment, String> {
}
