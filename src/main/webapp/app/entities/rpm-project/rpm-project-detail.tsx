import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-project.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmProjectDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmProjectEntity = useAppSelector(state => state.rpmProject.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmProjectDetailsHeading">
          <Translate contentKey="rpmApp.rpmProject.detail.title">RpmProject</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmProjectEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmProject.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmProjectEntity.name}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="rpmApp.rpmProject.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>
            {rpmProjectEntity.dateTime ? <TextFormat value={rpmProjectEntity.dateTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="duration">
              <Translate contentKey="rpmApp.rpmProject.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {rpmProjectEntity.duration ? <DurationFormat value={rpmProjectEntity.duration} /> : null} ({rpmProjectEntity.duration})
          </dd>
        </dl>
        <Button tag={Link} to="/rpm-project" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-project/${rpmProjectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmProjectDetail;
