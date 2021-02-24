import {Routes} from '@angular/router';
import {FormCandidateComponent} from '../features/candidate/form-candidate/form-candidate.component';
import {ViewEditCandidateComponent} from '../features/candidate/view-edit-candidate/view-edit-candidate.component';
import {CrudSkillComponent} from '../features/skill/crud-skill/crud-skill.component';

export const routes: Routes = [
  {
    path: '',
    component: ViewEditCandidateComponent
  },
  {
    path: 'add-candidate',
    component: FormCandidateComponent
  },
  {
    path: 'edit-candidate',
    component: FormCandidateComponent
  },
  {
    path: 'skill',
    component: CrudSkillComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];
