import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRpmProject } from 'app/shared/model/rpm-project.model';
import { getEntities as getRpmProjects } from 'app/entities/rpm-project/rpm-project.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rpm-outcome.reducer';
import { IRpmOutcome } from 'app/shared/model/rpm-outcome.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmOutcomeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const rpmProjects = useAppSelector(state => state.rpmProject.entities);
  const rpmOutcomeEntity = useAppSelector(state => state.rpmOutcome.entity);
  const loading = useAppSelector(state => state.rpmOutcome.loading);
  const updating = useAppSelector(state => state.rpmOutcome.updating);
  const updateSuccess = useAppSelector(state => state.rpmOutcome.updateSuccess);

  const handleClose = () => {
    props.history.push('/rpm-outcome');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRpmProjects({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateTime = convertDateTimeToServer(values.dateTime);

    const entity = {
      ...rpmOutcomeEntity,
      ...values,
      project: rpmProjects.find(it => it.id.toString() === values.projectId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateTime: displayDefaultDateTime(),
        }
      : {
          ...rpmOutcomeEntity,
          dateTime: convertDateTimeFromServer(rpmOutcomeEntity.dateTime),
          projectId: rpmOutcomeEntity?.project?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rpmApp.rpmOutcome.home.createOrEditLabel" data-cy="RpmOutcomeCreateUpdateHeading">
            <Translate contentKey="rpmApp.rpmOutcome.home.createOrEditLabel">Create or edit a RpmOutcome</Translate>
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
                  id="rpm-outcome-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rpmApp.rpmOutcome.name')}
                id="rpm-outcome-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rpmApp.rpmOutcome.dateTime')}
                id="rpm-outcome-dateTime"
                name="dateTime"
                data-cy="dateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rpmApp.rpmOutcome.duration')}
                id="rpm-outcome-duration"
                name="duration"
                data-cy="duration"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="rpm-outcome-project"
                name="projectId"
                data-cy="project"
                label={translate('rpmApp.rpmOutcome.project')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rpm-outcome" replace color="info">
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

export default RpmOutcomeUpdate;
