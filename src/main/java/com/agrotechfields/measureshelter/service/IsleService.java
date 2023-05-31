package com.agrotechfields.measureshelter.service;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.dto.IsleDto;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.repository.IsleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class IsleService.
 */
@Service
public class IsleService {

  /** The repository. */
  @Autowired
  private IsleRepository repository;

  /**
   * Find all isles.
   *
   * @return the list
   */
  public List<Isle> findAllIsles() {
    return repository.findAll();
  }

  /**
   * Find isle by id.
   *
   * @param id the id
   * @return the isle
   * @throws EntityNotFoundException the entity not found exception
   */
  public Isle findIsleById(String id) throws EntityNotFoundException {
    Optional<Isle> foundIsle = repository.findById(id);
    if (foundIsle.isEmpty()) {
      throw new EntityNotFoundException("Isle");
    }
    return foundIsle.get();
  }

  /**
   * Find isle by serial number.
   *
   * @param serialNumber the serial number
   * @return the isle
   * @throws EntityNotFoundException the entity not found exception
   */
  public Isle findIsleBySerialNumber(String serialNumber) throws EntityNotFoundException {
    Isle isle = repository.findBySerialNumber(serialNumber);
    if (isle == null) {
      throw new EntityNotFoundException("Isle");
    }
    return isle;
  }

  /**
   * Creates the isle.
   *
   * @param isleDto the isle dto
   * @return the isle
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  public Isle createIsle(IsleDto isleDto) throws EntityAlreadyExistsException {
    Isle foundIsle = repository.findBySerialNumber(isleDto.getSerialNumber());
    if (foundIsle != null) {
      throw new EntityAlreadyExistsException("Isle");
    }
    return repository.save(isleDto.isleFromDto());
  }

  /**
   * Update isle.
   *
   * @param isleDto the isle dto
   * @return the isle
   * @throws EntityNotFoundException the entity not found exception
   */
  public Isle updateIsleById(String id, IsleDto isleDto) throws EntityNotFoundException {
    findIsleById(id);

    Isle isle = isleDto.isleFromDto();
    isle.setId(id);

    return repository.save(isle);
  }

  /**
   * Delete isle by id.
   *
   * @param id the id
   * @throws EntityNotFoundException the entity not found exception
   */
  public void deleteIsleById(String id) throws EntityNotFoundException {
    Isle isle = findIsleById(id);
    repository.delete(isle);
  }

}
