import { TestBed } from '@angular/core/testing';

import { ViewEditSkillService } from './view-edit-skill.service';

describe('ViewEditSkillService', () => {
  let service: ViewEditSkillService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ViewEditSkillService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
