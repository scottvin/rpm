import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmAspect from './rpm-aspect';
import RpmAspectDetail from './rpm-aspect-detail';
import RpmAspectUpdate from './rpm-aspect-update';
import RpmAspectDeleteDialog from './rpm-aspect-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmAspectUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmAspectUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmAspectDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmAspect} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmAspectDeleteDialog} />
  </>
);

export default Routes;
