import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-result.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmResultDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmResultEntity = useAppSelector(state => state.rpmResult.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmResultDetailsHeading">
          <Translate contentKey="rpmApp.rpmResult.detail.title">RpmResult</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmResultEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmResult.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmResultEntity.name}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmResult.category">Category</Translate>
          </dt>
          <dd>{rpmResultEntity.category ? rpmResultEntity.category.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmResult.aspect">Aspect</Translate>
          </dt>
          <dd>{rpmResultEntity.aspect ? rpmResultEntity.aspect.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmResult.vision">Vision</Translate>
          </dt>
          <dd>{rpmResultEntity.vision ? rpmResultEntity.vision.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmResult.purpose">Purpose</Translate>
          </dt>
          <dd>{rpmResultEntity.purpose ? rpmResultEntity.purpose.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmResult.role">Role</Translate>
          </dt>
          <dd>{rpmResultEntity.role ? rpmResultEntity.role.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/rpm-result" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-result/${rpmResultEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmResultDetail;
