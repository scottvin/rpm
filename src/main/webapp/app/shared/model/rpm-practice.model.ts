import { IRpmCharacter } from 'app/shared/model/rpm-character.model';

export interface IRpmPractice {
  id?: number;
  name?: string;
  character?: IRpmCharacter;
}

export const defaultValue: Readonly<IRpmPractice> = {};
