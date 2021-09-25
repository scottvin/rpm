import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmAction from './rpm-action';
import RpmActionDetail from './rpm-action-detail';
import RpmActionUpdate from './rpm-action-update';
import RpmActionDeleteDialog from './rpm-action-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmActionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmActionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmActionDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmAction} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmActionDeleteDialog} />
  </>
);

export default Routes;
