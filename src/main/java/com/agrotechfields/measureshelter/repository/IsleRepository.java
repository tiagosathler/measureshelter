package com.agrotechfields.measureshelter.repository;

import com.agrotechfields.measureshelter.domain.Isle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface IsleRepository.
 */
@Repository
public interface IsleRepository extends MongoRepository<Isle, String> {

  /**
   * Find by serial number.
   *
   * @param serialNumber the serial number
   * @return the isle
   */
  public Isle findBySerialNumber(String serialNumber);
}
