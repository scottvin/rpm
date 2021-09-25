import dayjs from 'dayjs';

export interface IRpmCapture {
  id?: number;
  name?: string;
  dateTime?: string;
  duration?: string;
}

export const defaultValue: Readonly<IRpmCapture> = {};
