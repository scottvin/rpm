import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmCharacter from './rpm-character';
import RpmCharacterDetail from './rpm-character-detail';
import RpmCharacterUpdate from './rpm-character-update';
import RpmCharacterDeleteDialog from './rpm-character-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmCharacterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmCharacterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmCharacterDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmCharacter} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmCharacterDeleteDialog} />
  </>
);

export default Routes;
