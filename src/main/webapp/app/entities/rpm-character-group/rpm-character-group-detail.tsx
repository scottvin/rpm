import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-character-group.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmCharacterGroupDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmCharacterGroupEntity = useAppSelector(state => state.rpmCharacterGroup.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmCharacterGroupDetailsHeading">
          <Translate contentKey="rpmApp.rpmCharacterGroup.detail.title">RpmCharacterGroup</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmCharacterGroupEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmCharacterGroup.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmCharacterGroupEntity.name}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="rpmApp.rpmCharacterGroup.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{rpmCharacterGroupEntity.priority}</dd>
        </dl>
        <Button tag={Link} to="/rpm-character-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-character-group/${rpmCharacterGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmCharacterGroupDetail;
