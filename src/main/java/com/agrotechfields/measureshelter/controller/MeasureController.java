package com.agrotechfields.measureshelter.controller;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.domain.Measure;
import com.agrotechfields.measureshelter.dto.MeasureDto;
import com.agrotechfields.measureshelter.dto.MeasureResponseDefaultDto;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.NotPermittedException;
import com.agrotechfields.measureshelter.service.IsleService;
import com.agrotechfields.measureshelter.service.MeasureService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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

  /**
   * Gets the all measures.
   *
   * @return the all found measures
   */
  @GetMapping
  public ResponseEntity<List<MeasureResponseDefaultDto>> getAll() {
    List<Measure> measures = measureService.findAllMeasures();
    return ResponseEntity.ok().body(convertToDto(measures));
  }

  /**
   * Gets the all measure by isle id.
   *
   * @param id the id
   * @return the all measure found by isle id
   * @throws EntityNotFoundException the entity not found exception
   */
  @GetMapping("/isle/{id}")
  public ResponseEntity<List<MeasureResponseDefaultDto>> getAllMeasureByIsleId(
      @PathVariable("id") String id) throws EntityNotFoundException {
    Isle isle = isleService.findIsleById(id);
    List<Measure> measures = measureService.findAllMeasuresByIsle(isle);
    return ResponseEntity.ok().body(convertToDto(measures));
  }

  /**
   * Gets the measure id.
   *
   * @param id the id
   * @return the measure found by id
   * @throws EntityNotFoundException the entity not found exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<MeasureResponseDefaultDto> getByMeasureId(@PathVariable("id") String id)
      throws EntityNotFoundException {
    Measure measure = measureService.findMeasureById(id);
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
  public ResponseEntity<MeasureResponseDefaultDto> create(@RequestBody @Valid MeasureDto measureDto)
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
   */
  @PutMapping("/{id}")
  public ResponseEntity<MeasureResponseDefaultDto> updateByMeasureId(@PathVariable("id") String id,
      @RequestBody @Valid MeasureDto measureDto) throws EntityNotFoundException {
    Measure measure = measureService.updateByMeasureId(id, measureDto);
    return ResponseEntity.ok().body(convertToDto(measure));
  }

  /**
   * Delete measure by id.
   *
   * @param id the id
   * @return the response entity without content
   * @throws EntityNotFoundException the entity not found exception
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") String id)
      throws EntityNotFoundException {
    measureService.deleteMeasureById(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Builds the uri.
   *
   * @param id the id
   * @return the uri
   */
  private URI buildUri(String id) {
    return ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path(endpoint + "/{id}")
        .buildAndExpand(id)
        .toUri();
  }

  /**
   * Convert to dto.
   *
   * @param measures the measures
   * @return the list
   */
  private List<MeasureResponseDefaultDto> convertToDto(List<Measure> measures) {
    return measures.stream().map(MeasureResponseDefaultDto::new).toList();
  }

  /**
   * Convert to dto.
   *
   * @param measure the measure
   * @return the measure response default dto
   */
  private MeasureResponseDefaultDto convertToDto(Measure measure) {
    return new MeasureResponseDefaultDto(measure);
  }
}
