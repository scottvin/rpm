package com.github.scottvin.rpm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RpmAction.
 */
@Entity
@Table(name = "rpm_action")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RpmAction implements Serializable {

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
    @Column(name = "priority", nullable = false)
    private Integer priority;

    @NotNull
    @Column(name = "date_time", nullable = false)
    private ZonedDateTime dateTime;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Duration duration;

    @JsonIgnoreProperties(value = { "project" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private RpmPlan plan;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private RpmReason reason;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private RpmCapture captures;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "category", "aspect", "vision", "purpose", "role" }, allowSetters = true)
    private RpmResult result;

    @ManyToOne(optional = false)
    @NotNull
    private RpmProject project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RpmAction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RpmAction name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public RpmAction priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public ZonedDateTime getDateTime() {
        return this.dateTime;
    }

    public RpmAction dateTime(ZonedDateTime dateTime) {
        this.setDateTime(dateTime);
        return this;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public RpmAction duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public RpmPlan getPlan() {
        return this.plan;
    }

    public void setPlan(RpmPlan rpmPlan) {
        this.plan = rpmPlan;
    }

    public RpmAction plan(RpmPlan rpmPlan) {
        this.setPlan(rpmPlan);
        return this;
    }

    public RpmReason getReason() {
        return this.reason;
    }

    public void setReason(RpmReason rpmReason) {
        this.reason = rpmReason;
    }

    public RpmAction reason(RpmReason rpmReason) {
        this.setReason(rpmReason);
        return this;
    }

    public RpmCapture getCaptures() {
        return this.captures;
    }

    public void setCaptures(RpmCapture rpmCapture) {
        this.captures = rpmCapture;
    }

    public RpmAction captures(RpmCapture rpmCapture) {
        this.setCaptures(rpmCapture);
        return this;
    }

    public RpmResult getResult() {
        return this.result;
    }

    public void setResult(RpmResult rpmResult) {
        this.result = rpmResult;
    }

    public RpmAction result(RpmResult rpmResult) {
        this.setResult(rpmResult);
        return this;
    }

    public RpmProject getProject() {
        return this.project;
    }

    public void setProject(RpmProject rpmProject) {
        this.project = rpmProject;
    }

    public RpmAction project(RpmProject rpmProject) {
        this.setProject(rpmProject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmAction)) {
            return false;
        }
        return id != null && id.equals(((RpmAction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmAction{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", priority=" + getPriority() +
            ", dateTime='" + getDateTime() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
