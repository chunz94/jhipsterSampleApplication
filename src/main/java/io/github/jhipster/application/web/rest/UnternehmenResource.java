package io.github.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.application.domain.Unternehmen;

import io.github.jhipster.application.repository.UnternehmenRepository;
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
 * REST controller for managing Unternehmen.
 */
@RestController
@RequestMapping("/api")
public class UnternehmenResource {

    private final Logger log = LoggerFactory.getLogger(UnternehmenResource.class);

    private static final String ENTITY_NAME = "unternehmen";

    private final UnternehmenRepository unternehmenRepository;

    public UnternehmenResource(UnternehmenRepository unternehmenRepository) {
        this.unternehmenRepository = unternehmenRepository;
    }

    /**
     * POST  /unternehmen : Create a new unternehmen.
     *
     * @param unternehmen the unternehmen to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unternehmen, or with status 400 (Bad Request) if the unternehmen has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/unternehmen")
    @Timed
    public ResponseEntity<Unternehmen> createUnternehmen(@RequestBody Unternehmen unternehmen) throws URISyntaxException {
        log.debug("REST request to save Unternehmen : {}", unternehmen);
        if (unternehmen.getId() != null) {
            throw new BadRequestAlertException("A new unternehmen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Unternehmen result = unternehmenRepository.save(unternehmen);
        return ResponseEntity.created(new URI("/api/unternehmen/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /unternehmen : Updates an existing unternehmen.
     *
     * @param unternehmen the unternehmen to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unternehmen,
     * or with status 400 (Bad Request) if the unternehmen is not valid,
     * or with status 500 (Internal Server Error) if the unternehmen couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/unternehmen")
    @Timed
    public ResponseEntity<Unternehmen> updateUnternehmen(@RequestBody Unternehmen unternehmen) throws URISyntaxException {
        log.debug("REST request to update Unternehmen : {}", unternehmen);
        if (unternehmen.getId() == null) {
            return createUnternehmen(unternehmen);
        }
        Unternehmen result = unternehmenRepository.save(unternehmen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, unternehmen.getId().toString()))
            .body(result);
    }

    /**
     * GET  /unternehmen : get all the unternehmen.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of unternehmen in body
     */
    @GetMapping("/unternehmen")
    @Timed
    public List<Unternehmen> getAllUnternehmen() {
        log.debug("REST request to get all Unternehmen");
        return unternehmenRepository.findAll();
        }

    /**
     * GET  /unternehmen/:id : get the "id" unternehmen.
     *
     * @param id the id of the unternehmen to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unternehmen, or with status 404 (Not Found)
     */
    @GetMapping("/unternehmen/{id}")
    @Timed
    public ResponseEntity<Unternehmen> getUnternehmen(@PathVariable Long id) {
        log.debug("REST request to get Unternehmen : {}", id);
        Unternehmen unternehmen = unternehmenRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(unternehmen));
    }

    /**
     * DELETE  /unternehmen/:id : delete the "id" unternehmen.
     *
     * @param id the id of the unternehmen to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/unternehmen/{id}")
    @Timed
    public ResponseEntity<Void> deleteUnternehmen(@PathVariable Long id) {
        log.debug("REST request to delete Unternehmen : {}", id);
        unternehmenRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
