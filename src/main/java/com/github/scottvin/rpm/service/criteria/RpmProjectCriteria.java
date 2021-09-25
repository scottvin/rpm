package com.github.scottvin.rpm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.DurationFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.github.scottvin.rpm.domain.RpmProject} entity. This class is used
 * in {@link com.github.scottvin.rpm.web.rest.RpmProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rpm-projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RpmProjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ZonedDateTimeFilter dateTime;

    private DurationFilter duration;

    private Boolean distinct;

    public RpmProjectCriteria() {}

    public RpmProjectCriteria(RpmProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dateTime = other.dateTime == null ? null : other.dateTime.copy();
        this.duration = other.duration == null ? null : other.duration.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RpmProjectCriteria copy() {
        return new RpmProjectCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ZonedDateTimeFilter getDateTime() {
        return dateTime;
    }

    public ZonedDateTimeFilter dateTime() {
        if (dateTime == null) {
            dateTime = new ZonedDateTimeFilter();
        }
        return dateTime;
    }

    public void setDateTime(ZonedDateTimeFilter dateTime) {
        this.dateTime = dateTime;
    }

    public DurationFilter getDuration() {
        return duration;
    }

    public DurationFilter duration() {
        if (duration == null) {
            duration = new DurationFilter();
        }
        return duration;
    }

    public void setDuration(DurationFilter duration) {
        this.duration = duration;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RpmProjectCriteria that = (RpmProjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dateTime, that.dateTime) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateTime, duration, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmProjectCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dateTime != null ? "dateTime=" + dateTime + ", " : "") +
            (duration != null ? "duration=" + duration + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
