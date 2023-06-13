package com.agrotechfields.measureshelter.controller;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.domain.Measure;
import com.agrotechfields.measureshelter.dto.request.MeasureDto;
import com.agrotechfields.measureshelter.dto.response.MeasureResponseDto;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.InvalidIdException;
import com.agrotechfields.measureshelter.exception.NotPermittedException;
import com.agrotechfields.measureshelter.service.IdService;
import com.agrotechfields.measureshelter.service.IsleService;
import com.agrotechfields.measureshelter.service.MeasureService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * The Class MeasureController.
 */
@RestController
@RequestMapping("${measure.endpoint}")
public class MeasureController {

  @Value("${measure.endpoint}")
  private String endpoint;

  /** The isle service. */
  @Autowired
  private IsleService isleService;

  /** The measure service. */
  @Autowired
  private MeasureService measureService;

  /** The id service. */
  @Autowired
  private IdService idService;

  /**
   * Gets the all measures.
   *
   * @return the all found measures
   */
  @GetMapping
  public ResponseEntity<List<MeasureResponseDto>> getAll() {
    List<Measure> measures = measureService.findAllMeasures();
    return ResponseEntity.ok().body(convertToDto(measures));
  }

  /**
   * Gets the all measure by isle id.
   *
   * @param id the id
   * @return the all measure found by isle id
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @GetMapping("/isle/{id}")
  public ResponseEntity<List<MeasureResponseDto>> getAllMeasureByIsleId(
      @PathVariable("id") String id) throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    Isle isle = isleService.findIsleById(objectId);
    List<Measure> measures = measureService.findAllMeasuresByIsle(isle);
    return ResponseEntity.ok().body(convertToDto(measures));
  }

  /**
   * Gets the measure id.
   *
   * @param id the id
   * @return the measure found by id
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<MeasureResponseDto> getByMeasureId(@PathVariable("id") String id)
      throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    Measure measure = measureService.findMeasureById(objectId);
    return ResponseEntity.ok().body(convertToDto(measure));
  }

  /**
   * Creates the measure.
   *
   * @param measureDto the measure dto
   * @return the response entity with new measure
   * @throws EntityNotFoundException the entity not found exception
   * @throws NotPermittedException the not permitted exception
   */
  @PostMapping()
  public ResponseEntity<MeasureResponseDto> create(@RequestBody @Valid MeasureDto measureDto)
      throws EntityNotFoundException, NotPermittedException {
    Isle isle = isleService.getIsleFromContext();
    Measure measure = measureService.createMeasure(isle, measureDto);
    return ResponseEntity.created(buildUri(measure.getId())).body(convertToDto(measure));
  }

  /**
   * Update by measure id.
   *
   * @param id the id
   * @param measureDto the measure dto
   * @return the response entity with updated measure
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @PutMapping("/{id}")
  public ResponseEntity<MeasureResponseDto> updateByMeasureId(@PathVariable("id") String id,
      @RequestBody @Valid MeasureDto measureDto)
      throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    Measure measure = measureService.updateByMeasureId(objectId, measureDto);
    return ResponseEntity.ok().body(convertToDto(measure));
  }

  /**
   * Delete measure by id.
   *
   * @param id the id
   * @return the response entity without content
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") String id)
      throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    measureService.deleteMeasureById(objectId);
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
   * @param measures the measures
   * @return the list
   */
  private List<MeasureResponseDto> convertToDto(List<Measure> measures) {
    return measures.stream().map(MeasureResponseDto::new).toList();
  }

  /**
   * Convert to dto.
   *
   * @param measure the measure
   * @return the measure response default dto
   */
  private MeasureResponseDto convertToDto(Measure measure) {
    return new MeasureResponseDto(measure);
  }
}
