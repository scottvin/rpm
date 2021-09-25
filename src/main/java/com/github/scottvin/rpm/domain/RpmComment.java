package com.github.scottvin.rpm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RpmComment.
 */
@Entity
@Table(name = "rpm_comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RpmComment implements Serializable {

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
    @JsonIgnoreProperties(value = { "category", "aspect", "vision", "purpose", "role" }, allowSetters = true)
    private RpmResult result;

    @ManyToOne(optional = false)
    @NotNull
    private RpmProject project;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "plan", "reason", "captures", "result", "project" }, allowSetters = true)
    private RpmAction action;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "result" }, allowSetters = true)
    private RpmCharacter character;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RpmComment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RpmComment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RpmResult getResult() {
        return this.result;
    }

    public void setResult(RpmResult rpmResult) {
        this.result = rpmResult;
    }

    public RpmComment result(RpmResult rpmResult) {
        this.setResult(rpmResult);
        return this;
    }

    public RpmProject getProject() {
        return this.project;
    }

    public void setProject(RpmProject rpmProject) {
        this.project = rpmProject;
    }

    public RpmComment project(RpmProject rpmProject) {
        this.setProject(rpmProject);
        return this;
    }

    public RpmAction getAction() {
        return this.action;
    }

    public void setAction(RpmAction rpmAction) {
        this.action = rpmAction;
    }

    public RpmComment action(RpmAction rpmAction) {
        this.setAction(rpmAction);
        return this;
    }

    public RpmCharacter getCharacter() {
        return this.character;
    }

    public void setCharacter(RpmCharacter rpmCharacter) {
        this.character = rpmCharacter;
    }

    public RpmComment character(RpmCharacter rpmCharacter) {
        this.setCharacter(rpmCharacter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmComment)) {
            return false;
        }
        return id != null && id.equals(((RpmComment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmComment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
