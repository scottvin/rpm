import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmPlan from './rpm-plan';
import RpmPlanDetail from './rpm-plan-detail';
import RpmPlanUpdate from './rpm-plan-update';
import RpmPlanDeleteDialog from './rpm-plan-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmPlanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmPlanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmPlanDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmPlan} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmPlanDeleteDialog} />
  </>
);

export default Routes;
