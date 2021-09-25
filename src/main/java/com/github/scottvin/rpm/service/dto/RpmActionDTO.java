package com.github.scottvin.rpm.service.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.github.scottvin.rpm.domain.RpmAction} entity.
 */
public class RpmActionDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer priority;

    @NotNull
    private ZonedDateTime dateTime;

    @NotNull
    private Duration duration;

    private RpmPlanDTO plan;

    private RpmReasonDTO reason;

    private RpmCaptureDTO captures;

    private RpmResultDTO result;

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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    public RpmPlanDTO getPlan() {
        return plan;
    }

    public void setPlan(RpmPlanDTO plan) {
        this.plan = plan;
    }

    public RpmReasonDTO getReason() {
        return reason;
    }

    public void setReason(RpmReasonDTO reason) {
        this.reason = reason;
    }

    public RpmCaptureDTO getCaptures() {
        return captures;
    }

    public void setCaptures(RpmCaptureDTO captures) {
        this.captures = captures;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpmActionDTO)) {
            return false;
        }

        RpmActionDTO rpmActionDTO = (RpmActionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rpmActionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmActionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", priority=" + getPriority() +
            ", dateTime='" + getDateTime() + "'" +
            ", duration='" + getDuration() + "'" +
            ", plan=" + getPlan() +
            ", reason=" + getReason() +
            ", captures=" + getCaptures() +
            ", result=" + getResult() +
            ", project=" + getProject() +
            "}";
    }
}
