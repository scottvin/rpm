import { IRpmCharacter } from 'app/shared/model/rpm-character.model';

export interface IRpmQuote {
  id?: number;
  name?: string;
  character?: IRpmCharacter;
}

export const defaultValue: Readonly<IRpmQuote> = {};
