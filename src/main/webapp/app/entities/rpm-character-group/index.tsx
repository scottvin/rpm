import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmCharacterGroup from './rpm-character-group';
import RpmCharacterGroupDetail from './rpm-character-group-detail';
import RpmCharacterGroupUpdate from './rpm-character-group-update';
import RpmCharacterGroupDeleteDialog from './rpm-character-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmCharacterGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmCharacterGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmCharacterGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmCharacterGroup} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmCharacterGroupDeleteDialog} />
  </>
);

export default Routes;
