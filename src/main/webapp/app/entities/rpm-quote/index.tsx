import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmQuote from './rpm-quote';
import RpmQuoteDetail from './rpm-quote-detail';
import RpmQuoteUpdate from './rpm-quote-update';
import RpmQuoteDeleteDialog from './rpm-quote-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RpmQuoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RpmQuoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RpmQuoteDetail} />
      <ErrorBoundaryRoute path={match.url} component={RpmQuote} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RpmQuoteDeleteDialog} />
  </>
);

export default Routes;
