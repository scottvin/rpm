package com.github.scottvin.rpm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.github.scottvin.rpm.domain.RpmResult} entity.
 */
public class RpmResultDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private RpmCategoryDTO category;

    private RpmAspectDTO aspect;

    private RpmVisionDTO vision;

    private RpmPurposeDTO purpose;

    private RpmRoleDTO role;

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

    public RpmCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(RpmCategoryDTO category) {
        this.category = category;
    }

    public RpmAspectDTO getAspect() {
        return aspect;
    }

    public void setAspect(RpmAspectDTO aspect) {
        this.aspect = aspect;
    }

    public RpmVisionDTO getVision() {
        return vision;
    }

    public void setVision(RpmVisionDTO vision) {
        this.vision = vision;
    }

    public RpmPurposeDTO getPurpose() {
        return purpose;
    }

    public void setPurpose(RpmPurposeDTO purpose) {
        this.purpose = purpose;
    }

    public RpmRoleDTO getRole() {
        return role;
    }

    public void setRole(RpmRoleDTO role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmResultDTO)) {
            return false;
        }

        RpmResultDTO rpmResultDTO = (RpmResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rpmResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmResultDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", category=" + getCategory() +
            ", aspect=" + getAspect() +
            ", vision=" + getVision() +
            ", purpose=" + getPurpose() +
            ", role=" + getRole() +
            "}";
    }
}
