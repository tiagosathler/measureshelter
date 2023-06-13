package com.agrotechfields.measureshelter.repository;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.domain.Measure;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The Interface MeasureRepository.
 */
public interface MeasureRepository extends MongoRepository<Measure, ObjectId> {
  
  /**
   * Find by isle.
   *
   * @param isle the isle
   * @return the list
   */
  public List<Measure> findByIsle(Isle isle);
}
