package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Unternehmen;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Unternehmen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnternehmenRepository extends JpaRepository<Unternehmen, Long> {

}
