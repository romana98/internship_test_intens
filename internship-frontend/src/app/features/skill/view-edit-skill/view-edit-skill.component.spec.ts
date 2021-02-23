import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewEditSkillComponent } from './view-edit-skill.component';

describe('ViewEditSkillComponent', () => {
  let component: ViewEditSkillComponent;
  let fixture: ComponentFixture<ViewEditSkillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewEditSkillComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewEditSkillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
