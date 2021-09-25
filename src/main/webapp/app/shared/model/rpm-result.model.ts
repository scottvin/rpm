import { IRpmCategory } from 'app/shared/model/rpm-category.model';
import { IRpmAspect } from 'app/shared/model/rpm-aspect.model';
import { IRpmVision } from 'app/shared/model/rpm-vision.model';
import { IRpmPurpose } from 'app/shared/model/rpm-purpose.model';
import { IRpmRole } from 'app/shared/model/rpm-role.model';

export interface IRpmResult {
  id?: number;
  name?: string;
  category?: IRpmCategory;
  aspect?: IRpmAspect;
  vision?: IRpmVision;
  purpose?: IRpmPurpose;
  role?: IRpmRole;
}

export const defaultValue: Readonly<IRpmResult> = {};
