package com.backoffice.bo.boundary;

import com.backoffice.bo.entity.Photo;
import com.backoffice.bo.entity.Series;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photos")
public class PhotoRepresentation {

    private final PhotoRepository fr;
    private final SeriesRepository sr;

    private final Logger logger = LoggerFactory.getLogger(PhotoRepresentation.class);
    private static String UPLOADED_FOLDER = "./tmp/";

    public PhotoRepresentation(PhotoRepository fr, SeriesRepository sr) {
        this.fr = fr;
        this.sr = sr;
    }

    @GetMapping()
    public ResponseEntity<?> getPhotos() {
        Iterable<Photo> photo = fr.findAll();
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }
    
    /*Partie photo*/
    @GetMapping("/{photoid}")
    public ResponseEntity<?> uploadFile(@PathVariable("photoid") String photoid) throws IOException {
        Optional<Photo> photo = fr.findById(photoid);
        String lien = photo.get().getId();
        Path path = Paths.get(UPLOADED_FOLDER+lien);
        byte[] bytes=Files.readAllBytes(path);
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(bytes);
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {

        logger.debug("File upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("No file", HttpStatus.BAD_REQUEST);
        }
        try {
            saveUploadedFiles(Arrays.asList(uploadfile));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Uploaded - " + uploadfile.getOriginalFilename(),
                new HttpHeaders(), HttpStatus.OK);

    }

    @PostMapping(value = "/info/{serieid}")
    public ResponseEntity<?> uploadInfo(@PathVariable("serieid") String serieid, @RequestBody Photo photo) {
        photo.setId(UUID.randomUUID().toString());
        Series serie = sr.findById(serieid).get();
        photo.setSerie(serie);
        Photo saved = fr.save(photo);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        }
    }
}
