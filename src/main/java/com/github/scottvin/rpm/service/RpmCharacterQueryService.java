package com.github.scottvin.rpm.service;

import com.github.scottvin.rpm.domain.*; // for static metamodels
import com.github.scottvin.rpm.domain.RpmCharacter;
import com.github.scottvin.rpm.repository.RpmCharacterRepository;
import com.github.scottvin.rpm.service.criteria.RpmCharacterCriteria;
import com.github.scottvin.rpm.service.dto.RpmCharacterDTO;
import com.github.scottvin.rpm.service.mapper.RpmCharacterMapper;
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
 * Service for executing complex queries for {@link RpmCharacter} entities in the database.
 * The main input is a {@link RpmCharacterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RpmCharacterDTO} or a {@link Page} of {@link RpmCharacterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RpmCharacterQueryService extends QueryService<RpmCharacter> {

    private final Logger log = LoggerFactory.getLogger(RpmCharacterQueryService.class);

    private final RpmCharacterRepository rpmCharacterRepository;

    private final RpmCharacterMapper rpmCharacterMapper;

    public RpmCharacterQueryService(RpmCharacterRepository rpmCharacterRepository, RpmCharacterMapper rpmCharacterMapper) {
        this.rpmCharacterRepository = rpmCharacterRepository;
        this.rpmCharacterMapper = rpmCharacterMapper;
    }

    /**
     * Return a {@link List} of {@link RpmCharacterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RpmCharacterDTO> findByCriteria(RpmCharacterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RpmCharacter> specification = createSpecification(criteria);
        return rpmCharacterMapper.toDto(rpmCharacterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RpmCharacterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RpmCharacterDTO> findByCriteria(RpmCharacterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RpmCharacter> specification = createSpecification(criteria);
        return rpmCharacterRepository.findAll(specification, page).map(rpmCharacterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RpmCharacterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RpmCharacter> specification = createSpecification(criteria);
        return rpmCharacterRepository.count(specification);
    }

    /**
     * Function to convert {@link RpmCharacterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RpmCharacter> createSpecification(RpmCharacterCriteria criteria) {
        Specification<RpmCharacter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RpmCharacter_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RpmCharacter_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), RpmCharacter_.description));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriority(), RpmCharacter_.priority));
            }
            if (criteria.getResultId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getResultId(),
                            root -> root.join(RpmCharacter_.result, JoinType.LEFT).get(RpmResult_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
