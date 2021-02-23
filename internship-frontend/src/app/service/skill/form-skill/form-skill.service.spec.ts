import { TestBed } from '@angular/core/testing';

import { FormSkillService } from './form-skill.service';

describe('FormSkillService', () => {
  let service: FormSkillService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormSkillService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
