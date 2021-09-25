import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-practice.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmPracticeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmPracticeEntity = useAppSelector(state => state.rpmPractice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmPracticeDetailsHeading">
          <Translate contentKey="rpmApp.rpmPractice.detail.title">RpmPractice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmPracticeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmPractice.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmPracticeEntity.name}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmPractice.character">Character</Translate>
          </dt>
          <dd>{rpmPracticeEntity.character ? rpmPracticeEntity.character.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/rpm-practice" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-practice/${rpmPracticeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmPracticeDetail;
