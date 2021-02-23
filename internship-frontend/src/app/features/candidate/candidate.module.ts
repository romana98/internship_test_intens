import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormCandidateComponent} from './form-candidate/form-candidate.component';
import {ViewEditCandidateComponent} from './view-edit-candidate/view-edit-candidate.component';
import {MatDividerModule} from '@angular/material/divider';
import {SharedModule} from '../../shared/shared.module';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {ReactiveFormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule} from '@angular/material/input';


@NgModule({
  declarations: [FormCandidateComponent, ViewEditCandidateComponent],
  imports: [
    CommonModule,
    MatDividerModule,
    SharedModule,
    MatSnackBarModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
  ]
})
export class CandidateModule {
}
