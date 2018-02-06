package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Ansprechpartner;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Ansprechpartner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnsprechpartnerRepository extends JpaRepository<Ansprechpartner, Long> {

}
