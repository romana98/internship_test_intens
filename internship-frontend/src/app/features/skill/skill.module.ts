import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CrudSkillComponent} from './crud-skill/crud-skill.component';
import {MatDividerModule} from '@angular/material/divider';
import {ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {SharedModule} from '../../shared/shared.module';


@NgModule({
  declarations: [CrudSkillComponent],
  imports: [
    CommonModule,
    MatDividerModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    SharedModule
  ]
})
export class SkillModule {
}
