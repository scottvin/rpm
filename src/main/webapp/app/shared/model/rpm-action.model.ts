import dayjs from 'dayjs';
import { IRpmPlan } from 'app/shared/model/rpm-plan.model';
import { IRpmReason } from 'app/shared/model/rpm-reason.model';
import { IRpmCapture } from 'app/shared/model/rpm-capture.model';
import { IRpmResult } from 'app/shared/model/rpm-result.model';
import { IRpmProject } from 'app/shared/model/rpm-project.model';

export interface IRpmAction {
  id?: number;
  name?: string;
  priority?: number;
  dateTime?: string;
  duration?: string;
  plan?: IRpmPlan;
  reason?: IRpmReason;
  captures?: IRpmCapture;
  result?: IRpmResult;
  project?: IRpmProject;
}

export const defaultValue: Readonly<IRpmAction> = {};
