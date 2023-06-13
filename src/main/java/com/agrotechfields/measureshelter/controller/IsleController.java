package com.agrotechfields.measureshelter.controller;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.dto.request.IsleDto;
import com.agrotechfields.measureshelter.dto.response.IsleResponseDefaultDto;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.InvalidIdException;
import com.agrotechfields.measureshelter.service.IdService;
import com.agrotechfields.measureshelter.service.IsleService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The Class IsleController.
 */
@RestController
@RequestMapping(value = "${isle.endpoint}")
public class IsleController {

  @Value("${isle.endpoint}")
  private String endpoint;

  /** The service. */
  @Autowired
  private IsleService isleService;

  /** The id service. */
  @Autowired
  private IdService idService;

  /**
   * Find all.
   *
   * @return the response entity with all found isles
   */
  @GetMapping
  public ResponseEntity<List<IsleResponseDefaultDto>> findAll() {
    List<Isle> isles = isleService.findAllIsles();
    return ResponseEntity.ok().body(convertToDto(isles));
  }

  /**
   * Find by id.
   *
   * @param id the id
   * @return the response entity with found isle
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<IsleResponseDefaultDto> findById(@PathVariable("id") String id)
      throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    Isle isle = isleService.findIsleById(objectId);
    return ResponseEntity.ok().body(convertToDto(isle));
  }

  /**
   * Find by serial number.
   *
   * @param serialNumber the serial number
   * @return the response entity with found isle
   * @throws EntityNotFoundException the entity not found exception
   */
  @GetMapping("/serial/{serialNumber}")
  public ResponseEntity<IsleResponseDefaultDto> findBySerialNumber(
      @PathVariable("serialNumber") String serialNumber) throws EntityNotFoundException {
    Isle isle = isleService.findIsleBySerialNumber(serialNumber);
    return ResponseEntity.ok().body(convertToDto(isle));
  }

  /**
   * Creates the isle.
   *
   * @param isleDto the isle dto
   * @return the response entity with new isle
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  @PostMapping
  public ResponseEntity<IsleResponseDefaultDto> create(@RequestBody @Valid IsleDto isleDto)
      throws EntityAlreadyExistsException {
    Isle isle = isleService.createIsle(isleDto);
    return ResponseEntity.created(buildUri(isle.getId())).body(convertToDto(isle));
  }

  /**
   * Update the isle.
   *
   * @param id the id
   * @param isleDto the isle dto
   * @return the response entity with updated isle
   * @throws EntityNotFoundException the entity not found exception
   * @throws EntityAlreadyExistsException the entity already exists exception
   * @throws InvalidIdException the invalid id exception
   */
  @PutMapping("/{id}")
  public ResponseEntity<IsleResponseDefaultDto> update(@PathVariable("id") String id,
      @RequestBody @Valid IsleDto isleDto)
      throws EntityNotFoundException, EntityAlreadyExistsException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    Isle isle = isleService.updateIsleById(objectId, isleDto);
    return ResponseEntity.accepted().body(convertToDto(isle));
  }

  /**
   * Toggle 'isItWorking' mode.
   *
   * @param id the id
   * @return the response entity with updated mode
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @PatchMapping("/toggle/{id}")
  public ResponseEntity<Map<String, Boolean>> toggle(@PathVariable("id") String id)
      throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    Boolean isItWorking = isleService.toogleWorkingMode(objectId);
    Map<String, Boolean> response = new HashMap<>();
    response.put("isItWorking", isItWorking);
    return ResponseEntity.accepted().body(response);
  }

  /**
   * Delete the isle.
   *
   * @param id the id
   * @return the response entity without content.
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") String id)
      throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    isleService.deleteIsleById(objectId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Builds the uri.
   *
   * @param objectId the ObjectId
   * @return the uri
   */
  private URI buildUri(ObjectId objectId) {
    return ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path(endpoint + "/{id}")
        .buildAndExpand(objectId.toHexString())
        .toUri();
  }

  /**
   * Convert to dto.
   *
   * @param isles the isles
   * @return the list
   */
  private List<IsleResponseDefaultDto> convertToDto(List<Isle> isles) {
    return isles.stream().map(IsleResponseDefaultDto::new).toList();
  }

  /**
   * Convert to dto.
   *
   * @param isle the isle
   * @return the isle response default dto
   */
  private IsleResponseDefaultDto convertToDto(Isle isle) {
    return new IsleResponseDefaultDto(isle);
  }
}
