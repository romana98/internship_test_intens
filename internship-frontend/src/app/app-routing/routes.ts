import {Routes} from '@angular/router';
import {FormCandidateComponent} from '../features/candidate/form-candidate/form-candidate.component';
import {FormSkillComponent} from '../features/skill/form-skill/form-skill.component';
import {ViewEditCandidateComponent} from '../features/candidate/view-edit-candidate/view-edit-candidate.component';
import {ViewEditSkillComponent} from '../features/skill/view-edit-skill/view-edit-skill.component';

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
    path: 'add-skill',
    component: FormSkillComponent
  },
  {
    path: 'view-edit-skill',
    component: ViewEditSkillComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];
