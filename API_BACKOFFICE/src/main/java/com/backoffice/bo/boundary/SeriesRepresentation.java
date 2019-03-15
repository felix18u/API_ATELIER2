package com.backoffice.bo.boundary;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.bo.entity.Series;
import com.backoffice.bo.exception.NotFound;
import java.util.Date;
import java.util.UUID;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/series")
public class SeriesRepresentation {

    private final SeriesRepository sr;

    public SeriesRepresentation(SeriesRepository sr) {
            super();
            this.sr = sr;
    }

    @GetMapping()
    public ResponseEntity<?> getSeries(){
            Iterable<Series> categories = sr.findAll();
            return new ResponseEntity<>(categories,HttpStatus.OK);		
    }

    @GetMapping(value = "/{seriesId}")
    public ResponseEntity<?> getSeriesById(@PathVariable("seriesId") String id){		
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
