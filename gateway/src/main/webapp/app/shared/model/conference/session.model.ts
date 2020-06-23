import { Moment } from 'moment';
import { ISpeaker } from 'app/shared/model/conference/speaker.model';

export interface ISession {
  id?: number;
  title?: string;
  descriptionContentType?: string;
  description?: any;
  startDateTime?: Moment;
  endtDateTime?: Moment;
  speakers?: ISpeaker[];
}

export class Session implements ISession {
  constructor(
    public id?: number,
    public title?: string,
    public descriptionContentType?: string,
    public description?: any,
    public startDateTime?: Moment,
    public endtDateTime?: Moment,
    public speakers?: ISpeaker[]
  ) {}
}
