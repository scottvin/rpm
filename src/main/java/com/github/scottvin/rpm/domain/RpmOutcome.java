package com.github.scottvin.rpm.domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RpmOutcome.
 */
@Entity
@Table(name = "rpm_outcome")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RpmOutcome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "date_time", nullable = false)
    private ZonedDateTime dateTime;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Duration duration;

    @ManyToOne(optional = false)
    @NotNull
    private RpmProject project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RpmOutcome id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RpmOutcome name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDateTime() {
        return this.dateTime;
    }

    public RpmOutcome dateTime(ZonedDateTime dateTime) {
        this.setDateTime(dateTime);
        return this;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public RpmOutcome duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public RpmProject getProject() {
        return this.project;
    }

    public void setProject(RpmProject rpmProject) {
        this.project = rpmProject;
    }

    public RpmOutcome project(RpmProject rpmProject) {
        this.setProject(rpmProject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmOutcome)) {
            return false;
        }
        return id != null && id.equals(((RpmOutcome) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmOutcome{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateTime='" + getDateTime() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
