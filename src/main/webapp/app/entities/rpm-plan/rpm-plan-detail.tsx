import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-plan.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmPlanDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmPlanEntity = useAppSelector(state => state.rpmPlan.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmPlanDetailsHeading">
          <Translate contentKey="rpmApp.rpmPlan.detail.title">RpmPlan</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmPlanEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmPlan.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmPlanEntity.name}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="rpmApp.rpmPlan.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>{rpmPlanEntity.dateTime ? <TextFormat value={rpmPlanEntity.dateTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="rpmApp.rpmPlan.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {rpmPlanEntity.duration ? <DurationFormat value={rpmPlanEntity.duration} /> : null} ({rpmPlanEntity.duration})
          </dd>
          <dt>
            <Translate contentKey="rpmApp.rpmPlan.project">Project</Translate>
          </dt>
          <dd>{rpmPlanEntity.project ? rpmPlanEntity.project.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/rpm-plan" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-plan/${rpmPlanEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmPlanDetail;
