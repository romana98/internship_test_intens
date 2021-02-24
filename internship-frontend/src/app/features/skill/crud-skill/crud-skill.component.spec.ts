import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudSkillComponent } from './crud-skill.component';

describe('CrudSkillComponent', () => {
  let component: CrudSkillComponent;
  let fixture: ComponentFixture<CrudSkillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrudSkillComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrudSkillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
