package com.agrotechfields.measureshelter.service;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.dto.request.IsleDto;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.repository.IsleRepository;
import com.agrotechfields.measureshelter.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * The Class IsleService.
 */
@Service
public class IsleService {

  /** The repository. */
  @Autowired
  private IsleRepository isleRepository;

  @Autowired
  private UserRepository userRepository;

  /**
   * Find all isles.
   *
   * @return the list
   */
  public List<Isle> findAllIsles() {
    return isleRepository.findAll();
  }

  /**
   * Find isle by id.
   *
   * @param objectId the ObjectId
   * @return the isle
   * @throws EntityNotFoundException the entity not found exception
   */
  public Isle findIsleById(ObjectId objectId) throws EntityNotFoundException {
    Optional<Isle> foundIsle = isleRepository.findById(objectId);
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
    Optional<Isle> isle = isleRepository.findBySerialNumber(serialNumber);
    if (isle.isEmpty()) {
      throw new EntityNotFoundException("Isle");
    }
    return isle.get();
  }

  /**
   * Creates the isle.
   *
   * @param isleDto the isle dto
   * @return the isle
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  public Isle createIsle(IsleDto isleDto) throws EntityAlreadyExistsException {
    Optional<Isle> foundIsle = isleRepository.findBySerialNumber(isleDto.getSerialNumber());
    if (foundIsle.isPresent()) {
      throw new EntityAlreadyExistsException("Isle");
    }
    return isleRepository.insert(isleDto.isleFromDto());
  }

  /**
   * Update isle.
   *
   * @param objectId the ObjectId
   * @param isleDto the isle dto
   * @return the isle
   * @throws EntityNotFoundException the entity not found exception
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  public Isle updateIsleById(ObjectId objectId, IsleDto isleDto)
      throws EntityNotFoundException, EntityAlreadyExistsException {
    Isle isle = findIsleById(objectId);

    Optional<Isle> foundIsle = isleRepository.findBySerialNumber(isleDto.getSerialNumber());

    if (foundIsle.isPresent()) {
      throw new EntityAlreadyExistsException("Isle");
    }

    Optional<User> foundUser = userRepository.findByUsername(isle.getSerialNumber());

    if (foundUser.isPresent()) {
      User user = foundUser.get();
      user.setUsername(isleDto.getSerialNumber());
      userRepository.save(user);
    }

    Isle isleUptaded = isleDto.isleFromDto();
    isleUptaded.setId(objectId);

    return isleRepository.save(isleUptaded);
  }

  /**
   * Toogle working mode.
   *
   * @param objectId the ObjectId
   * @return true, if successful
   * @throws EntityNotFoundException the entity not found exception
   */
  public boolean toogleWorkingMode(ObjectId objectId) throws EntityNotFoundException {
    Isle isle = findIsleById(objectId);
    isle.setIsItWorking(!isle.getIsItWorking());
    isleRepository.save(isle);
    return isle.getIsItWorking();
  }

  /**
   * Delete isle by id.
   *
   * @param objectId the ObjectId
   * @throws EntityNotFoundException the entity not found exception
   */
  public void deleteIsleById(ObjectId objectId) throws EntityNotFoundException {
    Isle isle = findIsleById(objectId);
    isleRepository.delete(isle);

    Optional<User> foundUser = userRepository.findByUsername(isle.getSerialNumber());
    if (foundUser.isPresent()) {
      User user = foundUser.get();
      userRepository.delete(user);
    }
  }

  /**
   * Gets the isle from context.
   *
   * @return the isle found from context
   * @throws EntityNotFoundException the entity not found exception
   */
  public Isle getIsleFromContext() throws EntityNotFoundException {
    User contextUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<Isle> foundIsle = isleRepository.findBySerialNumber(contextUser.getUsername());
    if (foundIsle.isEmpty()) {
      throw new EntityNotFoundException("Isle");
    }
    return foundIsle.get();
  }
}
