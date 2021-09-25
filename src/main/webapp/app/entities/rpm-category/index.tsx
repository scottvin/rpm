import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmCategory from './rpm-category';
import RpmCategoryDetail from './rpm-category-detail';
import RpmCategoryUpdate from './rpm-category-update';
import RpmCategoryDeleteDialog from './rpm-category-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmCategoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmCategory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmCategoryDeleteDialog} />
  </>
);

export default Routes;
