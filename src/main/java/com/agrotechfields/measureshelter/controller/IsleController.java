package com.agrotechfields.measureshelter.controller;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.dto.IsleDto;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.service.IsleService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(value = "/isle")
public class IsleController {

  /** The service. */
  @Autowired
  private IsleService service;

  /**
   * Find all.
   *
   * @return the response entity
   */
  @GetMapping
  public ResponseEntity<List<Isle>> findAll() {
    List<Isle> isles = service.findAllIsles();
    return ResponseEntity.ok().body(isles);
  }

  /**
   * Find by id.
   *
   * @param id the id
   * @return the response entity
   * @throws EntityNotFoundException the entity not found exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<Isle> findById(@PathVariable("id") String id)
      throws EntityNotFoundException {
    System.out.println(id);
    Isle isle = service.findIsleById(id);
    return ResponseEntity.ok().body(isle);
  }

  /**
   * Find by serial number.
   *
   * @param serialNumber the serial number
   * @return the response entity
   * @throws EntityNotFoundException the entity not found exception
   */
  @GetMapping("/serial/{serialNumber}")
  public ResponseEntity<Isle> findBySerialNumber(@PathVariable("serialNumber") String serialNumber)
      throws EntityNotFoundException {
    Isle isle = service.findIsleBySerialNumber(serialNumber);
    return ResponseEntity.ok().body(isle);
  }

  /**
   * Creates the.
   *
   * @param isleDto the isle dto
   * @return the response entity
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  @PostMapping
  public ResponseEntity<Isle> create(@RequestBody @Valid IsleDto isleDto)
      throws EntityAlreadyExistsException {
    Isle isle = service.createIsle(isleDto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(isle.getId()).toUri();
    return ResponseEntity.created(uri).body(isle);
  }

  /**
   * Update.
   *
   * @param id the id
   * @param isleDto the isle dto
   * @return the response entity
   * @throws EntityNotFoundException the entity not found exception
   */
  @PutMapping("/{id}")
  public ResponseEntity<Isle> update(@PathVariable("id") String id,
      @RequestBody @Valid IsleDto isleDto) throws EntityNotFoundException {
    Isle isle = service.updateIsleById(id, isleDto);
    return ResponseEntity.accepted().body(isle);
  }

  /**
   * Delete.
   *
   * @param id the id
   * @return the response entity
   * @throws EntityNotFoundException the entity not found exception
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") String id)
      throws EntityNotFoundException {
    service.deleteIsleById(id);
    return ResponseEntity.noContent().build();
  }
}
