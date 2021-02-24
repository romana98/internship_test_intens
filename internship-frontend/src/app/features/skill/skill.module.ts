import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CrudSkillComponent} from './crud-skill/crud-skill.component';
import {MaterialModule} from '../../shared/material.module';


@NgModule({
  declarations: [CrudSkillComponent],
  imports: [
    CommonModule,
    MaterialModule
  ]
})
export class SkillModule {
}
