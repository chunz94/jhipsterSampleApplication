package io.github.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.application.domain.Ansprechpartner;

import io.github.jhipster.application.repository.AnsprechpartnerRepository;
import io.github.jhipster.application.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.application.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Ansprechpartner.
 */
@RestController
@RequestMapping("/api")
public class AnsprechpartnerResource {

    private final Logger log = LoggerFactory.getLogger(AnsprechpartnerResource.class);

    private static final String ENTITY_NAME = "ansprechpartner";

    private final AnsprechpartnerRepository ansprechpartnerRepository;

    public AnsprechpartnerResource(AnsprechpartnerRepository ansprechpartnerRepository) {
        this.ansprechpartnerRepository = ansprechpartnerRepository;
    }

    /**
     * POST  /ansprechpartners : Create a new ansprechpartner.
     *
     * @param ansprechpartner the ansprechpartner to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ansprechpartner, or with status 400 (Bad Request) if the ansprechpartner has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ansprechpartners")
    @Timed
    public ResponseEntity<Ansprechpartner> createAnsprechpartner(@RequestBody Ansprechpartner ansprechpartner) throws URISyntaxException {
        log.debug("REST request to save Ansprechpartner : {}", ansprechpartner);
        if (ansprechpartner.getId() != null) {
            throw new BadRequestAlertException("A new ansprechpartner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ansprechpartner result = ansprechpartnerRepository.save(ansprechpartner);
        return ResponseEntity.created(new URI("/api/ansprechpartners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ansprechpartners : Updates an existing ansprechpartner.
     *
     * @param ansprechpartner the ansprechpartner to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ansprechpartner,
     * or with status 400 (Bad Request) if the ansprechpartner is not valid,
     * or with status 500 (Internal Server Error) if the ansprechpartner couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ansprechpartners")
    @Timed
    public ResponseEntity<Ansprechpartner> updateAnsprechpartner(@RequestBody Ansprechpartner ansprechpartner) throws URISyntaxException {
        log.debug("REST request to update Ansprechpartner : {}", ansprechpartner);
        if (ansprechpartner.getId() == null) {
            return createAnsprechpartner(ansprechpartner);
        }
        Ansprechpartner result = ansprechpartnerRepository.save(ansprechpartner);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ansprechpartner.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ansprechpartners : get all the ansprechpartners.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ansprechpartners in body
     */
    @GetMapping("/ansprechpartners")
    @Timed
    public List<Ansprechpartner> getAllAnsprechpartners() {
        log.debug("REST request to get all Ansprechpartners");
        return ansprechpartnerRepository.findAll();
        }

    /**
     * GET  /ansprechpartners/:id : get the "id" ansprechpartner.
     *
     * @param id the id of the ansprechpartner to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ansprechpartner, or with status 404 (Not Found)
     */
    @GetMapping("/ansprechpartners/{id}")
    @Timed
    public ResponseEntity<Ansprechpartner> getAnsprechpartner(@PathVariable Long id) {
        log.debug("REST request to get Ansprechpartner : {}", id);
        Ansprechpartner ansprechpartner = ansprechpartnerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ansprechpartner));
    }

    /**
     * DELETE  /ansprechpartners/:id : delete the "id" ansprechpartner.
     *
     * @param id the id of the ansprechpartner to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ansprechpartners/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnsprechpartner(@PathVariable Long id) {
        log.debug("REST request to delete Ansprechpartner : {}", id);
        ansprechpartnerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
