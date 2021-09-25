package com.github.scottvin.rpm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.github.scottvin.rpm.domain.RpmCharacter} entity.
 */
public class RpmCharacterDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer priority;

    private RpmResultDTO result;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public RpmResultDTO getResult() {
        return result;
    }

    public void setResult(RpmResultDTO result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmCharacterDTO)) {
            return false;
        }

        RpmCharacterDTO rpmCharacterDTO = (RpmCharacterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rpmCharacterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmCharacterDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority=" + getPriority() +
            ", result=" + getResult() +
            "}";
    }
}
