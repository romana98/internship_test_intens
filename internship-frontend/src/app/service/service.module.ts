import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ViewEditCandidateService} from './candidate/view-edit-candidate/view-edit-candidate.service';
import {FormCandidateService} from './candidate/form-candidate/form-candidate.service';
import {HttpClientModule} from '@angular/common/http';
import {DataStorageService} from './data-storage/data-storage.service';
import {CrudSkillService} from './skill/crud-skill.service';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule
  ],
  providers: [
    FormCandidateService,
    ViewEditCandidateService,
    CrudSkillService,
    DataStorageService
  ]
})
export class ServiceModule {
}
