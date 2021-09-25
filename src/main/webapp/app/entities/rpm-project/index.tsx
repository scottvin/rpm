import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmProject from './rpm-project';
import RpmProjectDetail from './rpm-project-detail';
import RpmProjectUpdate from './rpm-project-update';
import RpmProjectDeleteDialog from './rpm-project-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmProjectUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmProjectUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmProjectDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmProject} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmProjectDeleteDialog} />
  </>
);

export default Routes;
