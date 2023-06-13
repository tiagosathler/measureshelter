package com.agrotechfields.measureshelter.controller;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.dto.request.IsleDto;
import com.agrotechfields.measureshelter.dto.response.IsleResponseDefaultDto;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.service.IsleService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private IsleService service;

  /**
   * Find all.
   *
   * @return the response entity with all found isles
   */
  @GetMapping
  public ResponseEntity<List<IsleResponseDefaultDto>> findAll() {
    List<Isle> isles = service.findAllIsles();
    return ResponseEntity.ok().body(convertToDto(isles));
  }

  /**
   * Find by id.
   *
   * @param id the id
   * @return the response entity with found isle
   * @throws EntityNotFoundException the entity not found exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<IsleResponseDefaultDto> findById(@PathVariable("id") String id)
      throws EntityNotFoundException {
    Isle isle = service.findIsleById(id);
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
    Isle isle = service.findIsleBySerialNumber(serialNumber);
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
    Isle isle = service.createIsle(isleDto);
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
   */
  @PutMapping("/{id}")
  public ResponseEntity<IsleResponseDefaultDto> update(@PathVariable("id") String id,
      @RequestBody @Valid IsleDto isleDto)
      throws EntityNotFoundException, EntityAlreadyExistsException {
    Isle isle = service.updateIsleById(id, isleDto);
    return ResponseEntity.accepted().body(convertToDto(isle));
  }

  /**
   * Toggle 'isItWorking' mode.
   *
   * @param id the id
   * @return the response entity with updated mode
   * @throws EntityNotFoundException the entity not found exception
   */
  @PatchMapping("/toggle/{id}")
  public ResponseEntity<Map<String, Boolean>> toggle(@PathVariable("id") String id)
      throws EntityNotFoundException {
    Boolean isItWorking = service.toogleWorkingMode(id);
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
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") String id)
      throws EntityNotFoundException {
    service.deleteIsleById(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Builds the uri.
   *
   * @param id the id
   * @return the uri
   */
  private URI buildUri(String id) {
    return ServletUriComponentsBuilder.fromCurrentContextPath().path(endpoint + "/{id}")
        .buildAndExpand(id).toUri();
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
