package com.backoffice.bo.boundary;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.bo.entity.Series;
import com.backoffice.bo.exception.NotFound;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/series")
public class SeriesRepresentation {

    private final PartieRepository pr;
    private final PhotoRepository fr;
    private final SeriesRepository sr;

    public SeriesRepresentation(PartieRepository pr,PhotoRepository fr,SeriesRepository sr) {
        this.pr = pr;
        this.fr = fr;
        this.sr = sr;
    }

    /*Partie series*/
    
    @GetMapping()
    public ResponseEntity<?> getSeries(){
            Iterable<Series> categories = sr.findAll();
            return new ResponseEntity<>(categories,HttpStatus.OK);		
    }

    @GetMapping(value = "/{seriesId}")
    public ResponseEntity<?> getSerieById(@PathVariable("seriesId") String id){		
            return Optional.ofNullable(sr.findById(id))
                            .filter(Optional::isPresent)
                            .map(series -> new ResponseEntity<>(series.get(),HttpStatus.OK))
                            .orElseThrow( () -> new NotFound("Series inexistant"));		
    }

    @PostMapping
    public ResponseEntity<?> postMethod(@RequestBody Series series) {
        series.setId(UUID.randomUUID().toString());
        Series saved = sr.save(series);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
    @PutMapping(value = "/{seriesId}")
    public ResponseEntity<?> updateSeries(@PathVariable("seriesId") String seriesId,
            @RequestBody Series seriesUpdated) {
        
        if (!sr.existsById(seriesId)) {
            throw new NotFound("Series inexistant");
        }
        return sr.findById(seriesId)
                .map(categorie -> {
                    seriesUpdated.setId(categorie.getId());
                    sr.save(seriesUpdated);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Series inexistant"));
    }
    
    @DeleteMapping(value = "/{seriesId}")
    public ResponseEntity<?> deleteProjet(@PathVariable("seriesId") String seriesId) {
        
        return sr.findById(seriesId)
                .map(projet -> {
                    sr.delete(projet);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Series inexistant"));
    }    
}