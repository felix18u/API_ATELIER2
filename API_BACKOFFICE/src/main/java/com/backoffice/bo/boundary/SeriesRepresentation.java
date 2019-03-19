package com.backoffice.bo.boundary;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.bo.entity.Series;
import com.backoffice.bo.exception.NotFound;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(description="API Pour la getion des series pour l'atelier 2")
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

    
    @ApiOperation(value = "Permet de récupèrer toutes les series")
    @GetMapping()
    public ResponseEntity<?> getSeries(){
            Iterable<Series> categories = sr.findAll();
            return new ResponseEntity<>(categories,HttpStatus.OK);		
    }

    @ApiOperation(value = "Permet de récupèrer une série avec son id")
    @GetMapping(value = "/{seriesId}")
    public ResponseEntity<?> getSerieById(@PathVariable("seriesId") String id){		
            return Optional.ofNullable(sr.findById(id))
                            .filter(Optional::isPresent)
                            .map(series -> new ResponseEntity<>(series.get(),HttpStatus.OK))
                            .orElseThrow( () -> new NotFound("Series inexistant"));		
    }

    @ApiOperation(value = "Permet de créer une série")
    @PostMapping
    public ResponseEntity<?> postMethod(@RequestBody Series series) {
        series.setId(UUID.randomUUID().toString());
        Series saved = sr.save(series);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Permet de modifier une série")
    @PutMapping(value = "/{seriesId}")
    public ResponseEntity<?> updateSeries(@PathVariable("seriesId") String seriesId,
            @RequestBody Series seriesUpdated) {
        
        if (!sr.existsById(seriesId)) {
            throw new NotFound("Series inexistant");
        }
        Series series = sr.findById(seriesId).get();
        if(seriesUpdated.getVille() != null) { series.setVille(seriesUpdated.getVille()); }
        if(seriesUpdated.getLon1() != null) { series.setLon1(seriesUpdated.getLon1()); }
        if(seriesUpdated.getLat1() != null) { series.setLat1(seriesUpdated.getLat1()); }
        if(seriesUpdated.getLon2() != null) { series.setLon2(seriesUpdated.getLon2()); }
        if(seriesUpdated.getLat2() != null) { series.setLat2(seriesUpdated.getLat2()); }
        if(seriesUpdated.getDist() != null) { series.setDist(seriesUpdated.getDist()); }
        Series saved = sr.save(series);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Permet de supprimer une série")
    @DeleteMapping(value = "/{seriesId}")
    public ResponseEntity<?> deleteProjet(@PathVariable("seriesId") String seriesId) {
        
        return sr.findById(seriesId)
                .map(projet -> {
                    sr.delete(projet);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Series inexistant"));
    }    
}