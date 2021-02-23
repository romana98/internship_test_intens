import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormCandidateComponent } from './form-candidate.component';

describe('FormCandidateComponent', () => {
  let component: FormCandidateComponent;
  let fixture: ComponentFixture<FormCandidateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormCandidateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormCandidateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
