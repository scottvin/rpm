import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmCapture from './rpm-capture';
import RpmCaptureDetail from './rpm-capture-detail';
import RpmCaptureUpdate from './rpm-capture-update';
import RpmCaptureDeleteDialog from './rpm-capture-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmCaptureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmCaptureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmCaptureDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmCapture} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmCaptureDeleteDialog} />
  </>
);

export default Routes;
