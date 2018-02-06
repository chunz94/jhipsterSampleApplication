package io.github.jhipster.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Unternehmen.
 */
@Entity
@Table(name = "unternehmen")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Unternehmen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "steuer_id")
    private String steuerID;

    @OneToMany(mappedBy = "unternehmen")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ansprechpartner> ansprechpartners = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Unternehmen name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteuerID() {
        return steuerID;
    }

    public Unternehmen steuerID(String steuerID) {
        this.steuerID = steuerID;
        return this;
    }

    public void setSteuerID(String steuerID) {
        this.steuerID = steuerID;
    }

    public Set<Ansprechpartner> getAnsprechpartners() {
        return ansprechpartners;
    }

    public Unternehmen ansprechpartners(Set<Ansprechpartner> ansprechpartners) {
        this.ansprechpartners = ansprechpartners;
        return this;
    }

    public Unternehmen addAnsprechpartner(Ansprechpartner ansprechpartner) {
        this.ansprechpartners.add(ansprechpartner);
        ansprechpartner.setUnternehmen(this);
        return this;
    }

    public Unternehmen removeAnsprechpartner(Ansprechpartner ansprechpartner) {
        this.ansprechpartners.remove(ansprechpartner);
        ansprechpartner.setUnternehmen(null);
        return this;
    }

    public void setAnsprechpartners(Set<Ansprechpartner> ansprechpartners) {
        this.ansprechpartners = ansprechpartners;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Unternehmen unternehmen = (Unternehmen) o;
        if (unternehmen.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), unternehmen.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Unternehmen{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", steuerID='" + getSteuerID() + "'" +
            "}";
    }
}
