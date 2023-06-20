package com.agrotechfields.measureshelter.service;

import com.agrotechfields.measureshelter.domain.Image;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.repository.ImageRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The Class ImageService.
 */
@Service
public class ImageService {

  /** The image repository. */
  @Autowired
  private ImageRepository imageRepository;

  /**
   * Creates the image.
   *
   * @param name the name
   * @param file the file
   * @return the image
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  public Image createImage(String name, MultipartFile file)
      throws IOException, EntityAlreadyExistsException {
    Optional<Image> foundImage = imageRepository.findByName(name);
    if (foundImage.isPresent()) {
      throw new EntityAlreadyExistsException("Image with name' " + name + "'");
    }
    Image image = new Image();
    image.setName(name);
    image.setImageData(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
    return imageRepository.save(image);
  }

  /**
   * Find image by name.
   *
   * @param name the name
   * @return the image
   * @throws EntityNotFoundException the entity not found exception
   */
  public Image findImageByName(String name) throws EntityNotFoundException {
    Optional<Image> foundImage = imageRepository.findByName(name);
    if (foundImage.isEmpty()) {
      throw new EntityNotFoundException("Image");
    }
    return foundImage.get();
  }

  /**
   * Find image by id.
   *
   * @param id the id
   * @return the image
   * @throws EntityNotFoundException the entity not found exception
   */
  public Image findImageById(ObjectId id) throws EntityNotFoundException {
    Optional<Image> foundImage = imageRepository.findById(id);
    if (foundImage.isEmpty()) {
      throw new EntityNotFoundException("Image");
    }
    return foundImage.get();
  }

  /**
   * Find all images.
   *
   * @return the list
   */
  public List<Image> findAllImages() {
    return imageRepository.findAll();
  }

  /**
   * Delete image by id.
   *
   * @param objectId the object id
   * @throws EntityNotFoundException the entity not found exception
   */
  public void deleteImageById(ObjectId objectId) throws EntityNotFoundException {
    Image image = findImageById(objectId);
    imageRepository.delete(image);
  }
}
