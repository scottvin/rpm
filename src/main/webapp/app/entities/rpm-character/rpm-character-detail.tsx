import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-character.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmCharacterDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmCharacterEntity = useAppSelector(state => state.rpmCharacter.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmCharacterDetailsHeading">
          <Translate contentKey="rpmApp.rpmCharacter.detail.title">RpmCharacter</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmCharacterEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmCharacter.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmCharacterEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="rpmApp.rpmCharacter.description">Description</Translate>
            </span>
          </dt>
          <dd>{rpmCharacterEntity.description}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="rpmApp.rpmCharacter.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{rpmCharacterEntity.priority}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmCharacter.result">Result</Translate>
          </dt>
          <dd>{rpmCharacterEntity.result ? rpmCharacterEntity.result.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/rpm-character" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-character/${rpmCharacterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmCharacterDetail;
