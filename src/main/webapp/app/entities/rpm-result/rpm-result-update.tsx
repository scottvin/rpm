import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRpmCategory } from 'app/shared/model/rpm-category.model';
import { getEntities as getRpmCategories } from 'app/entities/rpm-category/rpm-category.reducer';
import { IRpmAspect } from 'app/shared/model/rpm-aspect.model';
import { getEntities as getRpmAspects } from 'app/entities/rpm-aspect/rpm-aspect.reducer';
import { IRpmVision } from 'app/shared/model/rpm-vision.model';
import { getEntities as getRpmVisions } from 'app/entities/rpm-vision/rpm-vision.reducer';
import { IRpmPurpose } from 'app/shared/model/rpm-purpose.model';
import { getEntities as getRpmPurposes } from 'app/entities/rpm-purpose/rpm-purpose.reducer';
import { IRpmRole } from 'app/shared/model/rpm-role.model';
import { getEntities as getRpmRoles } from 'app/entities/rpm-role/rpm-role.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rpm-result.reducer';
import { IRpmResult } from 'app/shared/model/rpm-result.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmResultUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const rpmCategories = useAppSelector(state => state.rpmCategory.entities);
  const rpmAspects = useAppSelector(state => state.rpmAspect.entities);
  const rpmVisions = useAppSelector(state => state.rpmVision.entities);
  const rpmPurposes = useAppSelector(state => state.rpmPurpose.entities);
  const rpmRoles = useAppSelector(state => state.rpmRole.entities);
  const rpmResultEntity = useAppSelector(state => state.rpmResult.entity);
  const loading = useAppSelector(state => state.rpmResult.loading);
  const updating = useAppSelector(state => state.rpmResult.updating);
  const updateSuccess = useAppSelector(state => state.rpmResult.updateSuccess);

  const handleClose = () => {
    props.history.push('/rpm-result');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRpmCategories({}));
    dispatch(getRpmAspects({}));
    dispatch(getRpmVisions({}));
    dispatch(getRpmPurposes({}));
    dispatch(getRpmRoles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...rpmResultEntity,
      ...values,
      category: rpmCategories.find(it => it.id.toString() === values.categoryId.toString()),
      aspect: rpmAspects.find(it => it.id.toString() === values.aspectId.toString()),
      vision: rpmVisions.find(it => it.id.toString() === values.visionId.toString()),
      purpose: rpmPurposes.find(it => it.id.toString() === values.purposeId.toString()),
      role: rpmRoles.find(it => it.id.toString() === values.roleId.toString()),
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
          ...rpmResultEntity,
          categoryId: rpmResultEntity?.category?.id,
          aspectId: rpmResultEntity?.aspect?.id,
          visionId: rpmResultEntity?.vision?.id,
          purposeId: rpmResultEntity?.purpose?.id,
          roleId: rpmResultEntity?.role?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rpmApp.rpmResult.home.createOrEditLabel" data-cy="RpmResultCreateUpdateHeading">
            <Translate contentKey="rpmApp.rpmResult.home.createOrEditLabel">Create or edit a RpmResult</Translate>
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
                  id="rpm-result-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rpmApp.rpmResult.name')}
                id="rpm-result-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="rpm-result-category"
                name="categoryId"
                data-cy="category"
                label={translate('rpmApp.rpmResult.category')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmCategories
                  ? rpmCategories.map(otherEntity => (
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
                id="rpm-result-aspect"
                name="aspectId"
                data-cy="aspect"
                label={translate('rpmApp.rpmResult.aspect')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmAspects
                  ? rpmAspects.map(otherEntity => (
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
                id="rpm-result-vision"
                name="visionId"
                data-cy="vision"
                label={translate('rpmApp.rpmResult.vision')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmVisions
                  ? rpmVisions.map(otherEntity => (
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
                id="rpm-result-purpose"
                name="purposeId"
                data-cy="purpose"
                label={translate('rpmApp.rpmResult.purpose')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmPurposes
                  ? rpmPurposes.map(otherEntity => (
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
                id="rpm-result-role"
                name="roleId"
                data-cy="role"
                label={translate('rpmApp.rpmResult.role')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rpmRoles
                  ? rpmRoles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rpm-result" replace color="info">
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

export default RpmResultUpdate;
