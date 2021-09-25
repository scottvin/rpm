package com.github.scottvin.rpm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.github.scottvin.rpm.domain.RpmResource} entity.
 */
public class RpmResourceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private RpmActionDTO action;

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

    public RpmActionDTO getAction() {
        return action;
    }

    public void setAction(RpmActionDTO action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmResourceDTO)) {
            return false;
        }

        RpmResourceDTO rpmResourceDTO = (RpmResourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rpmResourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmResourceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", action=" + getAction() +
            "}";
    }
}
