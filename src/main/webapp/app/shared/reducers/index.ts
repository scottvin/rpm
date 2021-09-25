import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import rpmCategory from 'app/entities/rpm-category/rpm-category.reducer';
// prettier-ignore
import rpmAspect from 'app/entities/rpm-aspect/rpm-aspect.reducer';
// prettier-ignore
import rpmVision from 'app/entities/rpm-vision/rpm-vision.reducer';
// prettier-ignore
import rpmPurpose from 'app/entities/rpm-purpose/rpm-purpose.reducer';
// prettier-ignore
import rpmRole from 'app/entities/rpm-role/rpm-role.reducer';
// prettier-ignore
import rpmResult from 'app/entities/rpm-result/rpm-result.reducer';
// prettier-ignore
import rpmPlan from 'app/entities/rpm-plan/rpm-plan.reducer';
// prettier-ignore
import rpmProject from 'app/entities/rpm-project/rpm-project.reducer';
// prettier-ignore
import rpmOutcome from 'app/entities/rpm-outcome/rpm-outcome.reducer';
// prettier-ignore
import rpmAction from 'app/entities/rpm-action/rpm-action.reducer';
// prettier-ignore
import rpmReason from 'app/entities/rpm-reason/rpm-reason.reducer';
// prettier-ignore
import rpmResource from 'app/entities/rpm-resource/rpm-resource.reducer';
// prettier-ignore
import rpmCapture from 'app/entities/rpm-capture/rpm-capture.reducer';
// prettier-ignore
import rpmCharacter from 'app/entities/rpm-character/rpm-character.reducer';
// prettier-ignore
import rpmCharacterGroup from 'app/entities/rpm-character-group/rpm-character-group.reducer';
// prettier-ignore
import rpmPractice from 'app/entities/rpm-practice/rpm-practice.reducer';
// prettier-ignore
import rpmQuote from 'app/entities/rpm-quote/rpm-quote.reducer';
// prettier-ignore
import rpmComment from 'app/entities/rpm-comment/rpm-comment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  rpmCategory,
  rpmAspect,
  rpmVision,
  rpmPurpose,
  rpmRole,
  rpmResult,
  rpmPlan,
  rpmProject,
  rpmOutcome,
  rpmAction,
  rpmReason,
  rpmResource,
  rpmCapture,
  rpmCharacter,
  rpmCharacterGroup,
  rpmPractice,
  rpmQuote,
  rpmComment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
