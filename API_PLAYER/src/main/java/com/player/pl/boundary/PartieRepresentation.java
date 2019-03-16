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
import com.player.pl.exception.NotFound;
import java.util.Date;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/partie")
public class PartieRepresentation {

    private final PartieRepository pr;
    private final PhotoRepository fr;
    private final SerieRepository sr;

    public PartieRepresentation(PartieRepository pr,PhotoRepository fr,SerieRepository sr) {
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
        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit){
            Iterable<Partie> parties = pr.findAll();
            return new ResponseEntity<>(parties,HttpStatus.OK);		
    }

    @GetMapping(value = "/{partieId}")
    public ResponseEntity<?> getPartieById(@PathVariable("partieId") String id){		
            return Optional.ofNullable(pr.findById(id))
                            .filter(Optional::isPresent)
                            .map(partie -> new ResponseEntity<>(partie.get(),HttpStatus.OK))
                            .orElseThrow( () -> new NotFound("Partie inexistant"));		
    }
	
    @PostMapping
    public ResponseEntity<?> postMethod(@RequestBody Partie partie) {
        partie.setId(UUID.randomUUID().toString());
        partie.setToken(generateToken(partie));
        Partie saved = pr.save(partie);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
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
}
