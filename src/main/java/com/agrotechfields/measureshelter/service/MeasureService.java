package com.agrotechfields.measureshelter.service;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.domain.Measure;
import com.agrotechfields.measureshelter.dto.request.MeasureDto;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.NotPermittedException;
import com.agrotechfields.measureshelter.repository.MeasureRepository;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class MeasureService.
 */
@Service
public class MeasureService {

  /** The repository. */
  @Autowired
  private MeasureRepository measureRepository;

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
      throw new NotPermittedException("This Isle doesn't working. It");
    }
    Measure measure = measureDto.measureFromDto();
    measure.setIsleId(isle.getId());
    return measureRepository.insert(measure);
  }

  /**
   * Find all measures.
   *
   * @return the list
   */
  public List<Measure> findAllMeasures() {
    return measureRepository.findAll();
  }

  /**
   * Find all measures by isle.
   *
   * @param isle the isle
   * @return the list
   */
  public List<Measure> findAllMeasuresByIsle(Isle isle) {
    return measureRepository.findByIsleId(isle.getId());
  }

  /**
   * Find measure by id.
   *
   * @param objectId the ObjectId
   * @return the measure
   * @throws EntityNotFoundException the entity not found exception
   */
  public Measure findMeasureById(ObjectId objectId) throws EntityNotFoundException {
    Optional<Measure> foundMeasure = measureRepository.findById(objectId);
    if (foundMeasure.isEmpty()) {
      throw new EntityNotFoundException("Measure");
    }
    return foundMeasure.get();
  }

  /**
   * Update by measure id.
   *
   * @param objectId the ObjectId
   * @param measureDto the measure dto
   * @return the measure
   * @throws EntityNotFoundException the entity not found exception
   */
  public Measure updateByMeasureId(ObjectId objectId, MeasureDto measureDto)
      throws EntityNotFoundException {
    Measure foundMeasure = findMeasureById(objectId);
    Measure measure = measureDto.measureFromDto();
    measure.setId(foundMeasure.getId());
    measure.setIsleId(foundMeasure.getIsleId());
    return measureRepository.save(measure);
  }

  /**
   * Delete measure by id.
   *
   * @param objectId the ObjectId
   * @throws EntityNotFoundException the entity not found exception
   */
  public void deleteMeasureById(ObjectId objectId) throws EntityNotFoundException {
    Measure foundMeasure = findMeasureById(objectId);
    measureRepository.delete(foundMeasure);
  }
}
