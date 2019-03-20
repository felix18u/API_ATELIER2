package com.backoffice.bo.boundary;

import com.backoffice.bo.entity.Photo;
import com.backoffice.bo.entity.Series;
import com.backoffice.bo.exception.NotFound;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Base64;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@Api(description="API Pour la gestion des photos pour l'atelier 2")
@RequestMapping("/photos")
public class PhotoRepresentation {

    private final PhotoRepository fr;
    private final SeriesRepository sr;

    private final Logger logger = LoggerFactory.getLogger(PhotoRepresentation.class);
    private static String UPLOADED_FOLDER = "./images/";

    public PhotoRepresentation(PhotoRepository fr, SeriesRepository sr) {
        this.fr = fr;
        this.sr = sr;
    }

    @ApiOperation(value = "Permet de récupérer toutes les photos enregistrées")
    @GetMapping()
    public ResponseEntity<?> getPhotos() {
        Iterable<Photo> photo = fr.findAll();
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Permet de une photo avec l'id donnée")
    @GetMapping("/{photoid}")
    public ResponseEntity<?> uploadFile(@PathVariable("photoid") String photoid) throws IOException {
        Optional<Photo> photo = fr.findById(photoid);
        String lien = photo.get().getUrl();
        Path path = Paths.get(lien);
        byte[] bytes=Files.readAllBytes(path);
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(bytes);
    }

    @ApiOperation(value = "Permet d'enregistrer une photo dans la table et sur le disque")
    @PostMapping(value = "/upload/{serieid}")
    public ResponseEntity<?> uploadFile(@PathVariable("serieid") String serieid, @RequestParam("file") MultipartFile uploadfile) {

        logger.debug("File upload!");
        if (uploadfile.isEmpty()) {
            return new ResponseEntity("No file", HttpStatus.BAD_REQUEST);
        }
        try {
            saveUploadedFiles(Arrays.asList(uploadfile));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Series> serie = sr.findById(serieid);
        Photo photo = new Photo(null, null, null, UPLOADED_FOLDER + uploadfile.getOriginalFilename(), serie.get());
        photo.setId(UUID.randomUUID().toString());
        Photo saved = fr.save(photo);
        return new ResponseEntity("{\"id\":\"" + saved.getId() + "\",\"file\":\"" + uploadfile.getOriginalFilename() + "\"}",
                new HttpHeaders(), HttpStatus.OK);
    }

    @ApiOperation(value = "Permet d'enregistrer les informations d'une photo dans la table")
    @PutMapping(value = "/info/{photoid}")
    public ResponseEntity<?> uploadInfo(@PathVariable("photoid") String photoid, @RequestBody Photo photoUpdated) {
        if (!fr.existsById(photoid)) {
            throw new NotFound("Photo inexistante");
        }
        Photo photo = fr.findById(photoid).get();
        if(photoUpdated.getDescr() != null) { photo.setDescr(photoUpdated.getDescr()); }
        if(photoUpdated.getLon() != null) { photo.setLon(photoUpdated.getLon()); }
        if(photoUpdated.getLat() != null) { photo.setLat(photoUpdated.getLat()); }
        if(photoUpdated.getUrl() != null) { photo.setUrl(photoUpdated.getUrl()); }
        Photo saved = fr.save(photo);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Permet d'enregistrer une photo dans la table et sur le disque a partir d'une smartphone")
    @PostMapping(value = "/mobile/{serieid}")
    public ResponseEntity<?> uploadMobile(@PathVariable("serieid") String serieid, @RequestBody Photo sendphoto) {

        Optional<Series> serie = sr.findById(serieid);
        try {
            saveBase64Files(sendphoto.getUrl());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(PhotoRepresentation.class.getName()).log(Level.SEVERE, null, ex);
        }
        Photo photo = new Photo(null, null, null, UPLOADED_FOLDER + sendphoto.getUrl(), serie.get());
        photo.setId(UUID.randomUUID().toString());
        Photo saved = fr.save(photo);
        return new ResponseEntity("{\"id\":\"" + saved.getId() + "\",\"file\":\"" + sendphoto.getUrl() + "\"}",
                new HttpHeaders(), HttpStatus.OK);
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
    
    private void saveBase64Files(String base64) throws IOException {

        String[] parts = base64.split("-");
        String[] parts2 = parts[0].split("/");
        String[] parts3 = parts2[1].split(";");
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        String path = UPLOADED_FOLDER + parts3[1] + parts3[0];
        FileUtils.writeByteArrayToFile(new File(path), decodedBytes);
    }
}

