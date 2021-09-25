import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmResult from './rpm-result';
import RpmResultDetail from './rpm-result-detail';
import RpmResultUpdate from './rpm-result-update';
import RpmResultDeleteDialog from './rpm-result-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmResultDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmResult} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmResultDeleteDialog} />
  </>
);

export default Routes;
