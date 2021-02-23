import { TestBed } from '@angular/core/testing';

import { FormCandidateService } from './form-candidate.service';

describe('FormCandidateService', () => {
  let service: FormCandidateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormCandidateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
