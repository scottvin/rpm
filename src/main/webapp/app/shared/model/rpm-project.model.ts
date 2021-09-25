import dayjs from 'dayjs';

export interface IRpmProject {
  id?: number;
  name?: string;
  dateTime?: string;
  duration?: string;
}

export const defaultValue: Readonly<IRpmProject> = {};
