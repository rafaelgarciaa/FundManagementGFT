package com.fondosGFT.fondosGFT.repository.fund;

import com.fondosGFT.fondosGFT.model.fund.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepository extends MongoRepository<Fund, String> {
}
