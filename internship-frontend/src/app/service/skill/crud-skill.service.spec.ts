import { TestBed } from '@angular/core/testing';

import { CrudSkillService } from './crud-skill.service';

describe('CrudSkillService', () => {
  let service: CrudSkillService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CrudSkillService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
