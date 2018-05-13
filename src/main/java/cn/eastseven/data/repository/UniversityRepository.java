package cn.eastseven.data.repository;

import cn.eastseven.data.model.University;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author eastseven
 */
public interface UniversityRepository extends MongoRepository<University, String> {
}
