import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmPractice from './rpm-practice';
import RpmPracticeDetail from './rpm-practice-detail';
import RpmPracticeUpdate from './rpm-practice-update';
import RpmPracticeDeleteDialog from './rpm-practice-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmPracticeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmPracticeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmPracticeDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmPractice} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmPracticeDeleteDialog} />
  </>
);

export default Routes;
