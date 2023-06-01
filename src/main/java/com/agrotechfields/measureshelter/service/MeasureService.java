package com.agrotechfields.measureshelter.service;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.domain.Measure;
import com.agrotechfields.measureshelter.dto.MeasureDto;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.NotPermittedException;
import com.agrotechfields.measureshelter.repository.MeasureRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class MeasureService.
 */
@Service
public class MeasureService {

  /** The repository. */
  @Autowired
  private MeasureRepository repository;

  /**
   * Creates the measure.
   *
   * @param isle the isle
   * @param measureDto the measure dto
   * @return the measure
   * @throws NotPermittedException the not permitted exception
   */
  public Measure createMeasure(Isle isle, MeasureDto measureDto) throws NotPermittedException {
    if (Boolean.FALSE.equals(isle.getIsItWorking())) {
      throw new NotPermittedException("Isle");
    }
    Measure measure = measureDto.measureFromDto();
    measure.setIsle(isle);
    return repository.save(measure);
  }

  /**
   * Find all measures.
   *
   * @return the list
   */
  public List<Measure> findAllMeasures() {
    return repository.findAll();
  }

  /**
   * Find all measures by isle.
   *
   * @param isle the isle
   * @return the list
   */
  public List<Measure> findAllMeasuresByIsle(Isle isle) {
    return repository.findByIsle(isle);
  }

  /**
   * Find measure by id.
   *
   * @param id the id
   * @return the measure
   * @throws EntityNotFoundException the entity not found exception
   */
  public Measure findMeasureById(String id) throws EntityNotFoundException {
    Optional<Measure> foundMeasure = repository.findById(id);
    if (foundMeasure.isEmpty()) {
      throw new EntityNotFoundException("Measure");
    }
    return foundMeasure.get();
  }

  /**
   * Update by measure id.
   *
   * @param id the id
   * @param measureDto the measure dto
   * @return the measure
   * @throws EntityNotFoundException the entity not found exception
   */
  public Measure updateByMeasureId(String id, MeasureDto measureDto)
      throws EntityNotFoundException {
    Measure foundMeasure = findMeasureById(id);
    Measure measure = measureDto.measureFromDto();
    measure.setId(foundMeasure.getId());
    measure.setIsle(foundMeasure.getIsle());
    return repository.save(measure);
  }

  /**
   * Update by isle by measure id.
   *
   * @param isle the isle
   * @param measureId the measure id
   * @param measureDto the measure dto
   * @return the measure
   * @throws EntityNotFoundException the entity not found exception
   * @throws NotPermittedException the not permitted update exception
   */
  public Measure updateByIsleByMeasureId(Isle isle, String measureId, MeasureDto measureDto)
      throws EntityNotFoundException, NotPermittedException {
    Measure foundMeasure = findMeasureById(measureId);
    if (!(isle.equals(foundMeasure.getIsle()) && isle.getIsItWorking())) {
      throw new NotPermittedException("Isle");
    }
    Measure measure = measureDto.measureFromDto();
    measure.setId(foundMeasure.getId());
    measure.setIsle(foundMeasure.getIsle());
    return repository.save(measure);
  }

  /**
   * Delete measure by id.
   *
   * @param id the id
   * @throws EntityNotFoundException the entity not found exception
   */
  public void deleteMeasureById(String id) throws EntityNotFoundException {
    Measure foundMeasure = findMeasureById(id);
    repository.delete(foundMeasure);
  }
}
