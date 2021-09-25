import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-outcome.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmOutcomeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmOutcomeEntity = useAppSelector(state => state.rpmOutcome.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmOutcomeDetailsHeading">
          <Translate contentKey="rpmApp.rpmOutcome.detail.title">RpmOutcome</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmOutcomeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmOutcome.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmOutcomeEntity.name}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="rpmApp.rpmOutcome.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>
            {rpmOutcomeEntity.dateTime ? <TextFormat value={rpmOutcomeEntity.dateTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="duration">
              <Translate contentKey="rpmApp.rpmOutcome.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {rpmOutcomeEntity.duration ? <DurationFormat value={rpmOutcomeEntity.duration} /> : null} ({rpmOutcomeEntity.duration})
          </dd>
          <dt>
            <Translate contentKey="rpmApp.rpmOutcome.project">Project</Translate>
          </dt>
          <dd>{rpmOutcomeEntity.project ? rpmOutcomeEntity.project.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/rpm-outcome" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-outcome/${rpmOutcomeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmOutcomeDetail;
