import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewEditCandidateComponent } from './view-edit-candidate.component';

describe('ViewEditCandidateComponent', () => {
  let component: ViewEditCandidateComponent;
  let fixture: ComponentFixture<ViewEditCandidateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewEditCandidateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewEditCandidateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
