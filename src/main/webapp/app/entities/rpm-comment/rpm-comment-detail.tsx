import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rpm-comment.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmCommentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rpmCommentEntity = useAppSelector(state => state.rpmComment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rpmCommentDetailsHeading">
          <Translate contentKey="rpmApp.rpmComment.detail.title">RpmComment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rpmCommentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rpmApp.rpmComment.name">Name</Translate>
            </span>
          </dt>
          <dd>{rpmCommentEntity.name}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmComment.result">Result</Translate>
          </dt>
          <dd>{rpmCommentEntity.result ? rpmCommentEntity.result.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmComment.project">Project</Translate>
          </dt>
          <dd>{rpmCommentEntity.project ? rpmCommentEntity.project.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmComment.action">Action</Translate>
          </dt>
          <dd>{rpmCommentEntity.action ? rpmCommentEntity.action.name : ''}</dd>
          <dt>
            <Translate contentKey="rpmApp.rpmComment.character">Character</Translate>
          </dt>
          <dd>{rpmCommentEntity.character ? rpmCommentEntity.character.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/rpm-comment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rpm-comment/${rpmCommentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RpmCommentDetail;
