package com.github.scottvin.rpm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.github.scottvin.rpm.domain.RpmReason} entity.
 */
public class RpmReasonDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmReasonDTO)) {
            return false;
        }

        RpmReasonDTO rpmReasonDTO = (RpmReasonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rpmReasonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmReasonDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
