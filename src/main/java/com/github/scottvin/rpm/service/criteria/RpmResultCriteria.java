package com.github.scottvin.rpm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.github.scottvin.rpm.domain.RpmResult} entity. This class is used
 * in {@link com.github.scottvin.rpm.web.rest.RpmResultResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rpm-results?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RpmResultCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter categoryId;

    private LongFilter aspectId;

    private LongFilter visionId;

    private LongFilter purposeId;

    private LongFilter roleId;

    private Boolean distinct;

    public RpmResultCriteria() {}

    public RpmResultCriteria(RpmResultCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.aspectId = other.aspectId == null ? null : other.aspectId.copy();
        this.visionId = other.visionId == null ? null : other.visionId.copy();
        this.purposeId = other.purposeId == null ? null : other.purposeId.copy();
        this.roleId = other.roleId == null ? null : other.roleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RpmResultCriteria copy() {
        return new RpmResultCriteria(this);
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

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getAspectId() {
        return aspectId;
    }

    public LongFilter aspectId() {
        if (aspectId == null) {
            aspectId = new LongFilter();
        }
        return aspectId;
    }

    public void setAspectId(LongFilter aspectId) {
        this.aspectId = aspectId;
    }

    public LongFilter getVisionId() {
        return visionId;
    }

    public LongFilter visionId() {
        if (visionId == null) {
            visionId = new LongFilter();
        }
        return visionId;
    }

    public void setVisionId(LongFilter visionId) {
        this.visionId = visionId;
    }

    public LongFilter getPurposeId() {
        return purposeId;
    }

    public LongFilter purposeId() {
        if (purposeId == null) {
            purposeId = new LongFilter();
        }
        return purposeId;
    }

    public void setPurposeId(LongFilter purposeId) {
        this.purposeId = purposeId;
    }

    public LongFilter getRoleId() {
        return roleId;
    }

    public LongFilter roleId() {
        if (roleId == null) {
            roleId = new LongFilter();
        }
        return roleId;
    }

    public void setRoleId(LongFilter roleId) {
        this.roleId = roleId;
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
        final RpmResultCriteria that = (RpmResultCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(aspectId, that.aspectId) &&
            Objects.equals(visionId, that.visionId) &&
            Objects.equals(purposeId, that.purposeId) &&
            Objects.equals(roleId, that.roleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, categoryId, aspectId, visionId, purposeId, roleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpmResultCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (aspectId != null ? "aspectId=" + aspectId + ", " : "") +
            (visionId != null ? "visionId=" + visionId + ", " : "") +
            (purposeId != null ? "purposeId=" + purposeId + ", " : "") +
            (roleId != null ? "roleId=" + roleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
