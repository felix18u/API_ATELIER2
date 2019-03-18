package com.backoffice.bo.boundary;

import com.backoffice.bo.entity.Photo;
import com.backoffice.bo.service.FileStorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/series")
public class PhotoRepresentation {

    private static final Logger logger = LoggerFactory.getLogger(PhotoRepresentation.class);
    private final PhotoRepository fr;

    @Autowired
    private FileStorageService fileStorageService;

    public PhotoRepresentation(PhotoRepository fr) {
        this.fr = fr;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestBody Photo photo, @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        
        photo.setUrl(fileName);
        Photo saved = fr.save(photo);
        
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

}
