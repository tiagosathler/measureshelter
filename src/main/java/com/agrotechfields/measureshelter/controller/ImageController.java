package com.agrotechfields.measureshelter.controller;

import com.agrotechfields.measureshelter.domain.Image;
import com.agrotechfields.measureshelter.dto.response.ImageResponseDto;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.InvalidIdException;
import com.agrotechfields.measureshelter.service.IdService;
import com.agrotechfields.measureshelter.service.ImageService;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The Class ImageController.
 */
@RestController
@RequestMapping(value = "${endpoint.image}")
public class ImageController {

  /** The endpoint. */
  @Value("${endpoint.image}")
  private String endpoint;

  /** The id service. */
  @Autowired
  private IdService idService;

  /** The image service. */
  @Autowired
  private ImageService imageService;

  /**
   * Gets the all images.
   *
   * @return all images
   */
  @GetMapping
  public ResponseEntity<List<ImageResponseDto>> getAllImages() {
    List<Image> images = imageService.findAllImages();
    return ResponseEntity.ok().body(convertToDto(images));
  }

  /**
   * Gets the image by id.
   *
   * @param id the id
   * @return the image by id
   * @throws InvalidIdException the invalid id exception
   * @throws EntityNotFoundException the entity not found exception
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<byte[]> getImageById(@PathVariable("id") String id)
      throws InvalidIdException, EntityNotFoundException {
    ObjectId objectId = idService.getObjectId(id);
    byte[] data = imageService.findImageById(objectId).getImageData().getData();
    return ResponseEntity
        .ok()
        .contentType(MediaType.IMAGE_PNG)
        .body(data);
  }

  /**
   * Gets image by name.
   *
   * @param name the string name
   * @return the image by name
   * @throws EntityNotFoundException the entity not found exception
   */
  @GetMapping("/name/{name}")
  public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name)
      throws EntityNotFoundException {
    byte[] data = imageService.findImageByName(name).getImageData().getData();
    return ResponseEntity
        .ok()
        .contentType(MediaType.IMAGE_PNG)
        .body(data);
  }

  /**
   * Creates the new image.
   *
   * @param file the file
   * @return the image response dto entity
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws EntityAlreadyExistsException the entity already exists exception
   * @throws ServletException the servlet exception
   */
  @PostMapping
  public ResponseEntity<ImageResponseDto> createImage(
      @RequestParam(name = "file", required = true) MultipartFile file)
      throws IOException, EntityAlreadyExistsException, ServletException {
    Image image = imageService.createImage(file);
    return ResponseEntity
        .created(buildUri(image.getName()))
        .body(convertToDto(image));
  }

  /**
   * Delete image by id.
   *
   * @param id the string id 
   * @return the response entity
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteImageById(@PathVariable("id") String id)
      throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    imageService.deleteImageById(objectId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Builds the uri.
   *
   * @param name the name
   * @return the uri
   */
  private URI buildUri(String name) {
    return ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path(endpoint + "/name/{name}")
        .buildAndExpand(name)
        .toUri();
  }

  /**
   * Convert to dto.
   *
   * @param image the image
   * @return the image response dto
   */
  private ImageResponseDto convertToDto(Image image) {
    return new ImageResponseDto(image);
  }

  /**
   * Convert to dto.
   *
   * @param images the list of images
   * @return the impage response dto list
   */
  private List<ImageResponseDto> convertToDto(List<Image> images) {
    return images.stream().map(ImageResponseDto::new).toList();
  }
}
