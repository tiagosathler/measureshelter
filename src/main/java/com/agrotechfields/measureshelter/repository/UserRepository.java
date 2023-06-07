package com.agrotechfields.measureshelter.repository;

import com.agrotechfields.measureshelter.domain.User;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface UserRepository.
 */
@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

  /**
   * Find by username.
   *
   * @param username the username
   * @return the optional
   */
  public Optional<User> findByUsername(String username);
}
