import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRpmCharacter } from 'app/shared/model/rpm-character.model';
import { getEntities as getRpmCharacters } from 'app/entities/rpm-character/rpm-character.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rpm-practice.reducer';
import { IRpmPractice } from 'app/shared/model/rpm-practice.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmPracticeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const rpmCharacters = useAppSelector(state => state.rpmCharacter.entities);
  const rpmPracticeEntity = useAppSelector(state => state.rpmPractice.entity);
  const loading = useAppSelector(state => state.rpmPractice.loading);
  const updating = useAppSelector(state => state.rpmPractice.updating);
  const updateSuccess = useAppSelector(state => state.rpmPractice.updateSuccess);

  const handleClose = () => {
    props.history.push('/rpm-practice');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRpmCharacters({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...rpmPracticeEntity,
      ...values,
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
          ...rpmPracticeEntity,
          characterId: rpmPracticeEntity?.character?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rpmApp.rpmPractice.home.createOrEditLabel" data-cy="RpmPracticeCreateUpdateHeading">
            <Translate contentKey="rpmApp.rpmPractice.home.createOrEditLabel">Create or edit a RpmPractice</Translate>
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
                  id="rpm-practice-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rpmApp.rpmPractice.name')}
                id="rpm-practice-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="rpm-practice-character"
                name="characterId"
                data-cy="character"
                label={translate('rpmApp.rpmPractice.character')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rpm-practice" replace color="info">
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

export default RpmPracticeUpdate;
