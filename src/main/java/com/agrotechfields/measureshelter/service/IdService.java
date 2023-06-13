package com.agrotechfields.measureshelter.service;

import com.agrotechfields.measureshelter.exception.InvalidIdException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * The Class IdService.
 */
@Service
public class IdService {

  /**
   * Gets the object id.
   *
   * @param id the id
   * @return the object id
   * @throws InvalidIdException the invalid id exception
   */
  public ObjectId getObjectId(String id) throws InvalidIdException {
    try {
      return new ObjectId(id);
    } catch (IllegalArgumentException e) {
      throw new InvalidIdException(id);
    }
  }
}
