import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmPurpose from './rpm-purpose';
import RpmPurposeDetail from './rpm-purpose-detail';
import RpmPurposeUpdate from './rpm-purpose-update';
import RpmPurposeDeleteDialog from './rpm-purpose-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmPurposeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmPurposeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmPurposeDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmPurpose} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmPurposeDeleteDialog} />
  </>
);

export default Routes;
