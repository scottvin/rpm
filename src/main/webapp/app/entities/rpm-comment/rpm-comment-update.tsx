import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRpmResult } from 'app/shared/model/rpm-result.model';
import { getEntities as getRpmResults } from 'app/entities/rpm-result/rpm-result.reducer';
import { IRpmProject } from 'app/shared/model/rpm-project.model';
import { getEntities as getRpmProjects } from 'app/entities/rpm-project/rpm-project.reducer';
import { IRpmAction } from 'app/shared/model/rpm-action.model';
import { getEntities as getRpmActions } from 'app/entities/rpm-action/rpm-action.reducer';
import { IRpmCharacter } from 'app/shared/model/rpm-character.model';
import { getEntities as getRpmCharacters } from 'app/entities/rpm-character/rpm-character.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rpm-comment.reducer';
import { IRpmComment } from 'app/shared/model/rpm-comment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmCommentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const rpmResults = useAppSelector(state => state.rpmResult.entities);
  const rpmProjects = useAppSelector(state => state.rpmProject.entities);
  const rpmActions = useAppSelector(state => state.rpmAction.entities);
  const rpmCharacters = useAppSelector(state => state.rpmCharacter.entities);
  const rpmCommentEntity = useAppSelector(state => state.rpmComment.entity);
  const loading = useAppSelector(state => state.rpmComment.loading);
  const updating = useAppSelector(state => state.rpmComment.updating);
  const updateSuccess = useAppSelector(state => state.rpmComment.updateSuccess);

  const handleClose = () => {
    props.history.push('/rpm-comment');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRpmResults({}));
    dispatch(getRpmProjects({}));
    dispatch(getRpmActions({}));
    dispatch(getRpmCharacters({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...rpmCommentEntity,
      ...values,
      result: rpmResults.find(it => it.id.toString() === values.resultId.toString()),
      project: rpmProjects.find(it => it.id.toString() === values.projectId.toString()),
      action: rpmActions.find(it => it.id.toString() === values.actionId.toString()),
      character: rpmCharacters.find(it => it.id.toString() === values.characterId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...rpmCommentEntity,
          resultId: rpmCommentEntity?.result?.id,
          projectId: rpmCommentEntity?.project?.id,
          actionId: rpmCommentEntity?.action?.id,
          characterId: rpmCommentEntity?.character?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rpmApp.rpmComment.home.createOrEditLabel" data-cy="RpmCommentCreateUpdateHeading">
            <Translate contentKey="rpmApp.rpmComment.home.createOrEditLabel">Create or edit a RpmComment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="rpm-comment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rpmApp.rpmComment.name')}
                id="rpm-comment-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="rpm-comment-result"
                name="resultId"
                data-cy="result"
                label={translate('rpmApp.rpmComment.result')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmResults
                  ? rpmResults.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="rpm-comment-project"
                name="projectId"
                data-cy="project"
                label={translate('rpmApp.rpmComment.project')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmProjects
                  ? rpmProjects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="rpm-comment-action"
                name="actionId"
                data-cy="action"
                label={translate('rpmApp.rpmComment.action')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmActions
                  ? rpmActions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="rpm-comment-character"
                name="characterId"
                data-cy="character"
                label={translate('rpmApp.rpmComment.character')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmCharacters
                  ? rpmCharacters.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rpm-comment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default RpmCommentUpdate;
