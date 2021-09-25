import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/rpm-category">
      <Translate contentKey="global.menu.entities.rpmCategory" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-aspect">
      <Translate contentKey="global.menu.entities.rpmAspect" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-vision">
      <Translate contentKey="global.menu.entities.rpmVision" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-purpose">
      <Translate contentKey="global.menu.entities.rpmPurpose" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-role">
      <Translate contentKey="global.menu.entities.rpmRole" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-result">
      <Translate contentKey="global.menu.entities.rpmResult" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-plan">
      <Translate contentKey="global.menu.entities.rpmPlan" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-project">
      <Translate contentKey="global.menu.entities.rpmProject" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-outcome">
      <Translate contentKey="global.menu.entities.rpmOutcome" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-action">
      <Translate contentKey="global.menu.entities.rpmAction" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-reason">
      <Translate contentKey="global.menu.entities.rpmReason" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-resource">
      <Translate contentKey="global.menu.entities.rpmResource" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-capture">
      <Translate contentKey="global.menu.entities.rpmCapture" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-character">
      <Translate contentKey="global.menu.entities.rpmCharacter" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-character-group">
      <Translate contentKey="global.menu.entities.rpmCharacterGroup" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-practice">
      <Translate contentKey="global.menu.entities.rpmPractice" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-quote">
      <Translate contentKey="global.menu.entities.rpmQuote" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rpm-comment">
      <Translate contentKey="global.menu.entities.rpmComment" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
