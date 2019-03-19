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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(description="API Pour la gestion des parties pour l'atelier 2")
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

    @ApiOperation(value = "Permet de récupérer toutes les parties en cours. Il faut indiquer la page et le nombre de résultats à afficher")
    @GetMapping()
    public ResponseEntity<?> getParties(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        Iterable<Partie> parties = pr.findAll();
        return new ResponseEntity<>(parties, HttpStatus.OK);
    }

    @ApiOperation(value = "Permet de récupérer une partie avec son id et son token associé dans le header")
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

    @ApiOperation(value = "Permet de créer une partie. Retourne l'id de la partie et son token")
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
        for(int i = 0; i < Integer.parseInt(partie.getNb_photos()); i++) {
            partie.addPhoto(shufflePhoto.get(i));
        }
        Partie saved = pr.save(partie);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(PartieRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>("{\"id\":\"" + saved.getId() + "\",\"token\":\"" + saved.getToken() + "\"}", responseHeaders, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Permet de modifier la partie avec l'id donnée")
    @PutMapping(value = "/{partieId}")
    public ResponseEntity<?> updatePartie(@PathVariable("partieId") String partieId,
            @RequestBody Partie partieUpdated, @RequestHeader(value = "token", required = false, defaultValue = "") String tokenHeader) {

        if (!pr.existsById(partieId)) {
            throw new NotFound("Partie inexistante");
        }
        Partie partie = pr.findById(partieId).get();
        if(partieUpdated.getNb_photos() != null) { partie.setNb_photos(partieUpdated.getNb_photos()); }
        if(partieUpdated.getStatus() != null) { partie.setStatus(partieUpdated.getStatus()); }
        if(partieUpdated.getScore() != null) { partie.setScore(partieUpdated.getScore()); }
        if(partieUpdated.getJoueur() != null) { partie.setJoueur(partieUpdated.getJoueur()); }
        Partie saved = pr.save(partie);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
        
    }

    @ApiOperation(value = "Permet de supprimer une partie")
    @DeleteMapping(value = "/{partieId}")
    public ResponseEntity<?> deletePartie(@PathVariable("partieId") String idpartie) throws NotFound {
        return pr.findById(idpartie).map(partie -> {
            pr.delete(partie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("Partie inexistant !"));
    }
}
