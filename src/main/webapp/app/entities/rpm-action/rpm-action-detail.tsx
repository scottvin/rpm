import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-action.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmActionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmActionEntity = useAppSelector(state => state.rpmAction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmActionDetailsHeading">
          <Translate contentKey="rpmApp.rpmAction.detail.title">RpmAction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmActionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmAction.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmActionEntity.name}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="rpmApp.rpmAction.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{rpmActionEntity.priority}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="rpmApp.rpmAction.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>{rpmActionEntity.dateTime ? <TextFormat value={rpmActionEntity.dateTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="rpmApp.rpmAction.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {rpmActionEntity.duration ? <DurationFormat value={rpmActionEntity.duration} /> : null} ({rpmActionEntity.duration})
          </dd>
          <dt>
            <Translate contentKey="rpmApp.rpmAction.plan">Plan</Translate>
          </dt>
          <dd>{rpmActionEntity.plan ? rpmActionEntity.plan.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmAction.reason">Reason</Translate>
          </dt>
          <dd>{rpmActionEntity.reason ? rpmActionEntity.reason.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmAction.captures">Captures</Translate>
          </dt>
          <dd>{rpmActionEntity.captures ? rpmActionEntity.captures.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmAction.result">Result</Translate>
          </dt>
          <dd>{rpmActionEntity.result ? rpmActionEntity.result.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmAction.project">Project</Translate>
          </dt>
          <dd>{rpmActionEntity.project ? rpmActionEntity.project.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/rpm-action" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-action/${rpmActionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmActionDetail;
