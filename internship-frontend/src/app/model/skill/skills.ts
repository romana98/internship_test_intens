import {Skill} from './skill';

export class Skills {
  content: Skill[];
  numberOfElements: number;
  totalElements: number;
  totalPages: number;
  number: number;


  constructor() {
    this.content = [];
    this.number = 0;
    this.totalPages = 0;
    this.totalElements = 0;
    this.numberOfElements = 0;
  }
}
