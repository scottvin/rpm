package com.github.scottvin.rpm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.github.scottvin.rpm.domain.RpmComment} entity.
 */
public class RpmCommentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private RpmResultDTO result;

    private RpmProjectDTO project;

    private RpmActionDTO action;

    private RpmCharacterDTO character;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RpmResultDTO getResult() {
        return result;
    }

    public void setResult(RpmResultDTO result) {
        this.result = result;
    }

    public RpmProjectDTO getProject() {
        return project;
    }

    public void setProject(RpmProjectDTO project) {
        this.project = project;
    }

    public RpmActionDTO getAction() {
        return action;
    }

    public void setAction(RpmActionDTO action) {
        this.action = action;
    }

    public RpmCharacterDTO getCharacter() {
        return character;
    }

    public void setCharacter(RpmCharacterDTO character) {
        this.character = character;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmCommentDTO)) {
            return false;
        }

        RpmCommentDTO rpmCommentDTO = (RpmCommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rpmCommentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmCommentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", result=" + getResult() +
            ", project=" + getProject() +
            ", action=" + getAction() +
            ", character=" + getCharacter() +
            "}";
    }
}
