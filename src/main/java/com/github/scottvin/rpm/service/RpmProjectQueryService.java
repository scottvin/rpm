package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.*; // for static metamodels
import com.github.scottvin.rpm.domain.RpmProject;
import com.github.scottvin.rpm.repository.RpmProjectRepository;
import com.github.scottvin.rpm.service.criteria.RpmProjectCriteria;
import com.github.scottvin.rpm.service.dto.RpmProjectDTO;
import com.github.scottvin.rpm.service.mapper.RpmProjectMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link RpmProject} entities in the database.
 * The main input is a {@link RpmProjectCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RpmProjectDTO} or a {@link Page} of {@link RpmProjectDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RpmProjectQueryService extends QueryService<RpmProject> {

    private final Logger log = LoggerFactory.getLogger(RpmProjectQueryService.class);

    private final RpmProjectRepository rpmProjectRepository;

    private final RpmProjectMapper rpmProjectMapper;

    public RpmProjectQueryService(RpmProjectRepository rpmProjectRepository, RpmProjectMapper rpmProjectMapper) {
        this.rpmProjectRepository = rpmProjectRepository;
        this.rpmProjectMapper = rpmProjectMapper;
    }

    /**
     * Return a {@link List} of {@link RpmProjectDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RpmProjectDTO> findByCriteria(RpmProjectCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RpmProject> specification = createSpecification(criteria);
        return rpmProjectMapper.toDto(rpmProjectRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RpmProjectDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmProjectDTO> findByCriteria(RpmProjectCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RpmProject> specification = createSpecification(criteria);
        return rpmProjectRepository.findAll(specification, page).map(rpmProjectMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RpmProjectCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RpmProject> specification = createSpecification(criteria);
        return rpmProjectRepository.count(specification);
    }

    /**
     * Function to convert {@link RpmProjectCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RpmProject> createSpecification(RpmProjectCriteria criteria) {
        Specification<RpmProject> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RpmProject_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RpmProject_.name));
            }
            if (criteria.getDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTime(), RpmProject_.dateTime));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), RpmProject_.duration));
            }
        }
        return specification;
    }
}
