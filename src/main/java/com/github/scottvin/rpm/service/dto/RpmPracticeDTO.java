package com.github.scottvin.rpm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.github.scottvin.rpm.domain.RpmPractice} entity.
 */
public class RpmPracticeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

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
        if (!(o instanceof RpmPracticeDTO)) {
            return false;
        }

        RpmPracticeDTO rpmPracticeDTO = (RpmPracticeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rpmPracticeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmPracticeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", character=" + getCharacter() +
            "}";
    }
}
