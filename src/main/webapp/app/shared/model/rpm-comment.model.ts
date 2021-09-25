import { IRpmResult } from 'app/shared/model/rpm-result.model';
import { IRpmProject } from 'app/shared/model/rpm-project.model';
import { IRpmAction } from 'app/shared/model/rpm-action.model';
import { IRpmCharacter } from 'app/shared/model/rpm-character.model';

export interface IRpmComment {
  id?: number;
  name?: string;
  result?: IRpmResult;
  project?: IRpmProject;
  action?: IRpmAction;
  character?: IRpmCharacter;
}

export const defaultValue: Readonly<IRpmComment> = {};
