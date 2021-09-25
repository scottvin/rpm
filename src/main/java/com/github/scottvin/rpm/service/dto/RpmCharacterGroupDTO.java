package com.github.scottvin.rpm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.github.scottvin.rpm.domain.RpmCharacterGroup} entity.
 */
public class RpmCharacterGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer priority;

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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmCharacterGroupDTO)) {
            return false;
        }

        RpmCharacterGroupDTO rpmCharacterGroupDTO = (RpmCharacterGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rpmCharacterGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmCharacterGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", priority=" + getPriority() +
            "}";
    }
}
