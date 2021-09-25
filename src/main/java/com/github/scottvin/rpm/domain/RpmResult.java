package com.github.scottvin.rpm.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RpmResult.
 */
@Entity
@Table(name = "rpm_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RpmResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    private RpmCategory category;

    @ManyToOne(optional = false)
    @NotNull
    private RpmAspect aspect;

    @ManyToOne(optional = false)
    @NotNull
    private RpmVision vision;

    @ManyToOne(optional = false)
    @NotNull
    private RpmPurpose purpose;

    @ManyToOne(optional = false)
    @NotNull
    private RpmRole role;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RpmResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RpmResult name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RpmCategory getCategory() {
        return this.category;
    }

    public void setCategory(RpmCategory rpmCategory) {
        this.category = rpmCategory;
    }

    public RpmResult category(RpmCategory rpmCategory) {
        this.setCategory(rpmCategory);
        return this;
    }

    public RpmAspect getAspect() {
        return this.aspect;
    }

    public void setAspect(RpmAspect rpmAspect) {
        this.aspect = rpmAspect;
    }

    public RpmResult aspect(RpmAspect rpmAspect) {
        this.setAspect(rpmAspect);
        return this;
    }

    public RpmVision getVision() {
        return this.vision;
    }

    public void setVision(RpmVision rpmVision) {
        this.vision = rpmVision;
    }

    public RpmResult vision(RpmVision rpmVision) {
        this.setVision(rpmVision);
        return this;
    }

    public RpmPurpose getPurpose() {
        return this.purpose;
    }

    public void setPurpose(RpmPurpose rpmPurpose) {
        this.purpose = rpmPurpose;
    }

    public RpmResult purpose(RpmPurpose rpmPurpose) {
        this.setPurpose(rpmPurpose);
        return this;
    }

    public RpmRole getRole() {
        return this.role;
    }

    public void setRole(RpmRole rpmRole) {
        this.role = rpmRole;
    }

    public RpmResult role(RpmRole rpmRole) {
        this.setRole(rpmRole);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmResult)) {
            return false;
        }
        return id != null && id.equals(((RpmResult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmResult{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
