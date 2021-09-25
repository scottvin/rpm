import dayjs from 'dayjs';
import { IRpmProject } from 'app/shared/model/rpm-project.model';

export interface IRpmOutcome {
  id?: number;
  name?: string;
  dateTime?: string;
  duration?: string;
  project?: IRpmProject;
}

export const defaultValue: Readonly<IRpmOutcome> = {};
