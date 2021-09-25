import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmVision from './rpm-vision';
import RpmVisionDetail from './rpm-vision-detail';
import RpmVisionUpdate from './rpm-vision-update';
import RpmVisionDeleteDialog from './rpm-vision-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmVisionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmVisionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmVisionDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmVision} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmVisionDeleteDialog} />
  </>
);

export default Routes;
