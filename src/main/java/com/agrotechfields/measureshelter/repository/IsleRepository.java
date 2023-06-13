package com.agrotechfields.measureshelter.repository;

import com.agrotechfields.measureshelter.domain.Isle;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface IsleRepository.
 */
@Repository
public interface IsleRepository extends MongoRepository<Isle, ObjectId> {


  /**
   * Find by serial number.
   *
   * @param serialNumber the serial number
   * @return the optional
   */
  public Optional<Isle> findBySerialNumber(String serialNumber);
}
