import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRpmPlan } from 'app/shared/model/rpm-plan.model';
import { getEntities as getRpmPlans } from 'app/entities/rpm-plan/rpm-plan.reducer';
import { IRpmReason } from 'app/shared/model/rpm-reason.model';
import { getEntities as getRpmReasons } from 'app/entities/rpm-reason/rpm-reason.reducer';
import { IRpmCapture } from 'app/shared/model/rpm-capture.model';
import { getEntities as getRpmCaptures } from 'app/entities/rpm-capture/rpm-capture.reducer';
import { IRpmResult } from 'app/shared/model/rpm-result.model';
import { getEntities as getRpmResults } from 'app/entities/rpm-result/rpm-result.reducer';
import { IRpmProject } from 'app/shared/model/rpm-project.model';
import { getEntities as getRpmProjects } from 'app/entities/rpm-project/rpm-project.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rpm-action.reducer';
import { IRpmAction } from 'app/shared/model/rpm-action.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmActionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const rpmPlans = useAppSelector(state => state.rpmPlan.entities);
  const rpmReasons = useAppSelector(state => state.rpmReason.entities);
  const rpmCaptures = useAppSelector(state => state.rpmCapture.entities);
  const rpmResults = useAppSelector(state => state.rpmResult.entities);
  const rpmProjects = useAppSelector(state => state.rpmProject.entities);
  const rpmActionEntity = useAppSelector(state => state.rpmAction.entity);
  const loading = useAppSelector(state => state.rpmAction.loading);
  const updating = useAppSelector(state => state.rpmAction.updating);
  const updateSuccess = useAppSelector(state => state.rpmAction.updateSuccess);

  const handleClose = () => {
    props.history.push('/rpm-action');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRpmPlans({}));
    dispatch(getRpmReasons({}));
    dispatch(getRpmCaptures({}));
    dispatch(getRpmResults({}));
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
      ...rpmActionEntity,
      ...values,
      plan: rpmPlans.find(it => it.id.toString() === values.planId.toString()),
      reason: rpmReasons.find(it => it.id.toString() === values.reasonId.toString()),
      captures: rpmCaptures.find(it => it.id.toString() === values.capturesId.toString()),
      result: rpmResults.find(it => it.id.toString() === values.resultId.toString()),
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
          ...rpmActionEntity,
          dateTime: convertDateTimeFromServer(rpmActionEntity.dateTime),
          planId: rpmActionEntity?.plan?.id,
          reasonId: rpmActionEntity?.reason?.id,
          capturesId: rpmActionEntity?.captures?.id,
          resultId: rpmActionEntity?.result?.id,
          projectId: rpmActionEntity?.project?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rpmApp.rpmAction.home.createOrEditLabel" data-cy="RpmActionCreateUpdateHeading">
            <Translate contentKey="rpmApp.rpmAction.home.createOrEditLabel">Create or edit a RpmAction</Translate>
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
                  id="rpm-action-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rpmApp.rpmAction.name')}
                id="rpm-action-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rpmApp.rpmAction.priority')}
                id="rpm-action-priority"
                name="priority"
                data-cy="priority"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('rpmApp.rpmAction.dateTime')}
                id="rpm-action-dateTime"
                name="dateTime"
                data-cy="dateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rpmApp.rpmAction.duration')}
                id="rpm-action-duration"
                name="duration"
                data-cy="duration"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="rpm-action-plan"
                name="planId"
                data-cy="plan"
                label={translate('rpmApp.rpmAction.plan')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmPlans
                  ? rpmPlans.map(otherEntity => (
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
                id="rpm-action-reason"
                name="reasonId"
                data-cy="reason"
                label={translate('rpmApp.rpmAction.reason')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmReasons
                  ? rpmReasons.map(otherEntity => (
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
                id="rpm-action-captures"
                name="capturesId"
                data-cy="captures"
                label={translate('rpmApp.rpmAction.captures')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmCaptures
                  ? rpmCaptures.map(otherEntity => (
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
                id="rpm-action-result"
                name="resultId"
                data-cy="result"
                label={translate('rpmApp.rpmAction.result')}
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
                id="rpm-action-project"
                name="projectId"
                data-cy="project"
                label={translate('rpmApp.rpmAction.project')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rpm-action" replace color="info">
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

export default RpmActionUpdate;
