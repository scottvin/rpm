import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-quote.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmQuoteDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmQuoteEntity = useAppSelector(state => state.rpmQuote.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmQuoteDetailsHeading">
          <Translate contentKey="rpmApp.rpmQuote.detail.title">RpmQuote</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmQuoteEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmQuote.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmQuoteEntity.name}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmQuote.character">Character</Translate>
          </dt>
          <dd>{rpmQuoteEntity.character ? rpmQuoteEntity.character.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/rpm-quote" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-quote/${rpmQuoteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmQuoteDetail;
