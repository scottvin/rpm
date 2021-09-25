package com.github.scottvin.rpm.service.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.github.scottvin.rpm.domain.RpmPlan} entity.
 */
public class RpmPlanDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private ZonedDateTime dateTime;

    @NotNull
    private Duration duration;

    private RpmProjectDTO project;

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

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public RpmProjectDTO getProject() {
        return project;
    }

    public void setProject(RpmProjectDTO project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmPlanDTO)) {
            return false;
        }

        RpmPlanDTO rpmPlanDTO = (RpmPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rpmPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmPlanDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateTime='" + getDateTime() + "'" +
            ", duration='" + getDuration() + "'" +
            ", project=" + getProject() +
            "}";
    }
}
