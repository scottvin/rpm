import { IRpmAction } from 'app/shared/model/rpm-action.model';

export interface IRpmResource {
  id?: number;
  name?: string;
  action?: IRpmAction;
}

export const defaultValue: Readonly<IRpmResource> = {};
