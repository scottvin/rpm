import { IRpmResult } from 'app/shared/model/rpm-result.model';

export interface IRpmCharacter {
  id?: number;
  name?: string;
  description?: string;
  priority?: number;
  result?: IRpmResult;
}

export const defaultValue: Readonly<IRpmCharacter> = {};
