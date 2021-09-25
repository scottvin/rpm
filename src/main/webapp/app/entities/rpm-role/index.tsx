import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmRole from './rpm-role';
import RpmRoleDetail from './rpm-role-detail';
import RpmRoleUpdate from './rpm-role-update';
import RpmRoleDeleteDialog from './rpm-role-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmRoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmRoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmRoleDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmRole} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmRoleDeleteDialog} />
  </>
);

export default Routes;
