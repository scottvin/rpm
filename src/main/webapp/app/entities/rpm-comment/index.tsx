import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmComment from './rpm-comment';
import RpmCommentDetail from './rpm-comment-detail';
import RpmCommentUpdate from './rpm-comment-update';
import RpmCommentDeleteDialog from './rpm-comment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmCommentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmCommentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmCommentDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmComment} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmCommentDeleteDialog} />
  </>
);

export default Routes;
