import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRpmCharacter } from 'app/shared/model/rpm-character.model';
import { getEntities as getRpmCharacters } from 'app/entities/rpm-character/rpm-character.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rpm-quote.reducer';
import { IRpmQuote } from 'app/shared/model/rpm-quote.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RpmQuoteUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const rpmCharacters = useAppSelector(state => state.rpmCharacter.entities);
  const rpmQuoteEntity = useAppSelector(state => state.rpmQuote.entity);
  const loading = useAppSelector(state => state.rpmQuote.loading);
  const updating = useAppSelector(state => state.rpmQuote.updating);
  const updateSuccess = useAppSelector(state => state.rpmQuote.updateSuccess);

  const handleClose = () => {
    props.history.push('/rpm-quote');
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
      ...rpmQuoteEntity,
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
          ...rpmQuoteEntity,
          characterId: rpmQuoteEntity?.character?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rpmApp.rpmQuote.home.createOrEditLabel" data-cy="RpmQuoteCreateUpdateHeading">
            <Translate contentKey="rpmApp.rpmQuote.home.createOrEditLabel">Create or edit a RpmQuote</Translate>
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
                  id="rpm-quote-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rpmApp.rpmQuote.name')}
                id="rpm-quote-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="rpm-quote-character"
                name="characterId"
                data-cy="character"
                label={translate('rpmApp.rpmQuote.character')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rpm-quote" replace color="info">
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

export default RpmQuoteUpdate;
