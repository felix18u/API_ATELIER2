package com.player.pl.boundary;

import java.util.Optional;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.player.pl.entity.Partie;
import com.player.pl.entity.Photo;
import com.player.pl.entity.Series;
import com.player.pl.exception.NotFound;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/partie")
public class PartieRepresentation {

    private final PartieRepository pr;
    private final PhotoRepository fr;
    private final SeriesRepository sr;

    public PartieRepresentation(PartieRepository pr, PhotoRepository fr, SeriesRepository sr) {
        super();
        this.pr = pr;
        this.fr = fr;
        this.sr = sr;
    }

    public String generateToken(@RequestBody Partie partie) {
        return Jwts.builder().setSubject(partie.getId())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey")
                .compact();
    }

    @GetMapping()
    public ResponseEntity<?> getParties(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        Iterable<Partie> parties = pr.findAll();
        return new ResponseEntity<>(parties, HttpStatus.OK);
    }

    @GetMapping(value = "/{partieId}")
    public ResponseEntity<?> getPartieById(@PathVariable("partieId") String id,@RequestHeader(value = "token", required = false, defaultValue = "") String tokenHeader) {
        if (!pr.existsById(id)) {
            throw new NotFound("Partie inexistante !");
        }
        Optional<Partie> partie = pr.findById(id);
        if (partie.get().getToken().equals(tokenHeader)) {
            return new ResponseEntity<>(partie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"erreur\": \"Token invalide\"}", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value = "/{serieid}")
    public ResponseEntity<?> postPartie(@PathVariable("serieid") String serieid, @RequestBody Partie partie) {
        if (!sr.existsById(serieid)) {
            throw new NotFound("Serie inexistante !");
        }
        partie.setId(UUID.randomUUID().toString());
        partie.setToken(generateToken(partie));
        partie.setScore("0");
        partie.setStatus("start");
        Series serie = sr.findById(serieid).get();
        partie.setSerie(serie);
        List<Photo> shufflePhoto = new ArrayList<>(serie.getPhoto());
        Collections.shuffle(shufflePhoto);
        if (partie.getNb_photos() == null) {
            partie.setNb_photos("10");
        }
        Partie saved = pr.save(partie);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(PartieRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>("{\"id\":\"" + saved.getId() + "\",\"token\":\"" + saved.getToken() + "\"}", responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{partieId}")
    public ResponseEntity<?> updatePartie(@PathVariable("partieId") String partieId,
            @RequestBody Partie partieUpdated) {

        if (!pr.existsById(partieId)) {
            throw new NotFound("Partie inexistante");
        }
        return pr.findById(partieId)
                .map(Partie -> {
                    partieUpdated.setId(Partie.getId());
                    pr.save(partieUpdated);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Partie inexistante"));
    }

    @DeleteMapping(value = "/{partieId}")
    public ResponseEntity<?> deletePartie(@PathVariable("partieId") String idpartie) throws NotFound {
        return pr.findById(idpartie).map(partie -> {
            pr.delete(partie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("Partie inexistant !"));
    }
}
