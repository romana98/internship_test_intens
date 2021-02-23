import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ViewEditSkillService} from './skill/view-edit-skill/view-edit-skill.service';
import {ViewEditCandidateService} from './candidate/view-edit-candidate/view-edit-candidate.service';
import {FormSkillService} from './skill/form-skill/form-skill.service';
import {FormCandidateService} from './candidate/form-candidate/form-candidate.service';
import {HttpClientModule} from '@angular/common/http';
import {DataStorageService} from './data-storage/data-storage.service';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule
  ],
  providers: [
    FormCandidateService,
    FormSkillService,
    ViewEditCandidateService,
    ViewEditSkillService,
    DataStorageService
  ]
})
export class ServiceModule {
}
