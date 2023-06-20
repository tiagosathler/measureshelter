package com.agrotechfields.measureshelter.repository;

import com.agrotechfields.measureshelter.domain.Image;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The Interface ImageRepository.
 */
public interface ImageRepository extends MongoRepository<Image, ObjectId> {

  /**
   * Find by name.
   *
   * @param name the name
   * @return the optional
   */
  public Optional<Image> findByName(String name);
}
