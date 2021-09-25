import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-capture.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmCaptureDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmCaptureEntity = useAppSelector(state => state.rpmCapture.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmCaptureDetailsHeading">
          <Translate contentKey="rpmApp.rpmCapture.detail.title">RpmCapture</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmCaptureEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmCapture.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmCaptureEntity.name}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="rpmApp.rpmCapture.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>
            {rpmCaptureEntity.dateTime ? <TextFormat value={rpmCaptureEntity.dateTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="duration">
              <Translate contentKey="rpmApp.rpmCapture.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {rpmCaptureEntity.duration ? <DurationFormat value={rpmCaptureEntity.duration} /> : null} ({rpmCaptureEntity.duration})
          </dd>
        </dl>
        <Button tag={Link} to="/rpm-capture" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-capture/${rpmCaptureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmCaptureDetail;
