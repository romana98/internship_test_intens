import { TestBed } from '@angular/core/testing';

import { ViewEditCandidateService } from './view-edit-candidate.service';

describe('ViewEditCandidateService', () => {
  let service: ViewEditCandidateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ViewEditCandidateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
