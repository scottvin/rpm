import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RpmCategory from './rpm-category';
import RpmAspect from './rpm-aspect';
import RpmVision from './rpm-vision';
import RpmPurpose from './rpm-purpose';
import RpmRole from './rpm-role';
import RpmResult from './rpm-result';
import RpmPlan from './rpm-plan';
import RpmProject from './rpm-project';
import RpmOutcome from './rpm-outcome';
import RpmAction from './rpm-action';
import RpmReason from './rpm-reason';
import RpmResource from './rpm-resource';
import RpmCapture from './rpm-capture';
import RpmCharacter from './rpm-character';
import RpmCharacterGroup from './rpm-character-group';
import RpmPractice from './rpm-practice';
import RpmQuote from './rpm-quote';
import RpmComment from './rpm-comment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}rpm-category`} component={RpmCategory} />
      <ErrorBoundaryRoute path={`${match.url}rpm-aspect`} component={RpmAspect} />
      <ErrorBoundaryRoute path={`${match.url}rpm-vision`} component={RpmVision} />
      <ErrorBoundaryRoute path={`${match.url}rpm-purpose`} component={RpmPurpose} />
      <ErrorBoundaryRoute path={`${match.url}rpm-role`} component={RpmRole} />
      <ErrorBoundaryRoute path={`${match.url}rpm-result`} component={RpmResult} />
      <ErrorBoundaryRoute path={`${match.url}rpm-plan`} component={RpmPlan} />
      <ErrorBoundaryRoute path={`${match.url}rpm-project`} component={RpmProject} />
      <ErrorBoundaryRoute path={`${match.url}rpm-outcome`} component={RpmOutcome} />
      <ErrorBoundaryRoute path={`${match.url}rpm-action`} component={RpmAction} />
      <ErrorBoundaryRoute path={`${match.url}rpm-reason`} component={RpmReason} />
      <ErrorBoundaryRoute path={`${match.url}rpm-resource`} component={RpmResource} />
      <ErrorBoundaryRoute path={`${match.url}rpm-capture`} component={RpmCapture} />
      <ErrorBoundaryRoute path={`${match.url}rpm-character`} component={RpmCharacter} />
      <ErrorBoundaryRoute path={`${match.url}rpm-character-group`} component={RpmCharacterGroup} />
      <ErrorBoundaryRoute path={`${match.url}rpm-practice`} component={RpmPractice} />
      <ErrorBoundaryRoute path={`${match.url}rpm-quote`} component={RpmQuote} />
      <ErrorBoundaryRoute path={`${match.url}rpm-comment`} component={RpmComment} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
