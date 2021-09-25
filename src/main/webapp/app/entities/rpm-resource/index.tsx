import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmResource from './rpm-resource';
import RpmResourceDetail from './rpm-resource-detail';
import RpmResourceUpdate from './rpm-resource-update';
import RpmResourceDeleteDialog from './rpm-resource-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmResourceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmResourceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmResourceDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmResource} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmResourceDeleteDialog} />
  </>
);

export default Routes;
