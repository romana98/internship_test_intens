import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormCandidateComponent} from './form-candidate/form-candidate.component';
import {ViewEditCandidateComponent} from './view-edit-candidate/view-edit-candidate.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule, MatOptionModule} from '@angular/material/core';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MaterialModule} from '../../shared/material.module';


@NgModule({
  declarations: [FormCandidateComponent, ViewEditCandidateComponent],
  imports: [
    CommonModule,
    MaterialModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatAutocompleteModule,
    MatOptionModule
  ]
})
export class CandidateModule {
}
