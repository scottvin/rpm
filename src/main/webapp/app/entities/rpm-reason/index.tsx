import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmReason from './rpm-reason';
import RpmReasonDetail from './rpm-reason-detail';
import RpmReasonUpdate from './rpm-reason-update';
import RpmReasonDeleteDialog from './rpm-reason-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmReasonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmReasonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmReasonDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmReason} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmReasonDeleteDialog} />
  </>
);

export default Routes;
