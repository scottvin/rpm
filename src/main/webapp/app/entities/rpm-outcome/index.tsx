import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmOutcome from './rpm-outcome';
import RpmOutcomeDetail from './rpm-outcome-detail';
import RpmOutcomeUpdate from './rpm-outcome-update';
import RpmOutcomeDeleteDialog from './rpm-outcome-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmOutcomeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmOutcomeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmOutcomeDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmOutcome} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmOutcomeDeleteDialog} />
  </>
);

export default Routes;
