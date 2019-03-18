package com.backoffice.bo.boundary;

import com.backoffice.bo.entity.Photo;
import com.backoffice.bo.entity.Series;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    /*Partie photo*/
    @GetMapping("/{photoid}")
    public @ResponseBody
    byte[] getFile() {
        try {
            // Retrieve image from the classpath.
            InputStream is = this.getClass().getResourceAsStream("/test.jpg");

            // Prepare buffered image.
            BufferedImage img = ImageIO.read(is);

            // Create a byte array output stream.
            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            // Write to output stream
            ImageIO.write(img, "jpg", bao);

            return bao.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
