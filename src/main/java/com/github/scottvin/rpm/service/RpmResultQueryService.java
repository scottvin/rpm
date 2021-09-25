package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.*; // for static metamodels
import com.github.scottvin.rpm.domain.RpmResult;
import com.github.scottvin.rpm.repository.RpmResultRepository;
import com.github.scottvin.rpm.service.criteria.RpmResultCriteria;
import com.github.scottvin.rpm.service.dto.RpmResultDTO;
import com.github.scottvin.rpm.service.mapper.RpmResultMapper;
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
 * Service for executing complex queries for {@link RpmResult} entities in the database.
 * The main input is a {@link RpmResultCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RpmResultDTO} or a {@link Page} of {@link RpmResultDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RpmResultQueryService extends QueryService<RpmResult> {

    private final Logger log = LoggerFactory.getLogger(RpmResultQueryService.class);

    private final RpmResultRepository rpmResultRepository;

    private final RpmResultMapper rpmResultMapper;

    public RpmResultQueryService(RpmResultRepository rpmResultRepository, RpmResultMapper rpmResultMapper) {
        this.rpmResultRepository = rpmResultRepository;
        this.rpmResultMapper = rpmResultMapper;
    }

    /**
     * Return a {@link List} of {@link RpmResultDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RpmResultDTO> findByCriteria(RpmResultCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RpmResult> specification = createSpecification(criteria);
        return rpmResultMapper.toDto(rpmResultRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RpmResultDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmResultDTO> findByCriteria(RpmResultCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RpmResult> specification = createSpecification(criteria);
        return rpmResultRepository.findAll(specification, page).map(rpmResultMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RpmResultCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RpmResult> specification = createSpecification(criteria);
        return rpmResultRepository.count(specification);
    }

    /**
     * Function to convert {@link RpmResultCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RpmResult> createSpecification(RpmResultCriteria criteria) {
        Specification<RpmResult> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RpmResult_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RpmResult_.name));
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(RpmResult_.category, JoinType.LEFT).get(RpmCategory_.id)
                        )
                    );
            }
            if (criteria.getAspectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAspectId(), root -> root.join(RpmResult_.aspect, JoinType.LEFT).get(RpmAspect_.id))
                    );
            }
            if (criteria.getVisionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVisionId(), root -> root.join(RpmResult_.vision, JoinType.LEFT).get(RpmVision_.id))
                    );
            }
            if (criteria.getPurposeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPurposeId(),
                            root -> root.join(RpmResult_.purpose, JoinType.LEFT).get(RpmPurpose_.id)
                        )
                    );
            }
            if (criteria.getRoleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRoleId(), root -> root.join(RpmResult_.role, JoinType.LEFT).get(RpmRole_.id))
                    );
            }
        }
        return specification;
    }
}
