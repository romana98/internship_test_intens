import {Component, OnInit, ViewChild} from '@angular/core';
import {CrudSkillService} from '../../../service/skill/crud-skill.service';
import {FormBuilder, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Skills} from '../../../model/skill/skills';
import {Skill} from '../../../model/skill/skill';

@Component({
  selector: 'app-crud-skill',
  templateUrl: './crud-skill.component.html',
  styleUrls: ['./crud-skill.component.css']
})
export class CrudSkillComponent implements OnInit {
  @ViewChild(FormGroupDirective) formGroupDirective: FormGroupDirective;

  formAdd: FormGroup;
  formEdit: FormGroup;

  skills: Skills;
  pageSkill: number;
  pageSizeSkill: number;
  hideEdit: boolean;

  constructor(private crudSkillService: CrudSkillService,
              private fb: FormBuilder,
              private snackBar: MatSnackBar) {

    this.pageSkill = 0;
    this.pageSizeSkill = 5;
    this.skills = new Skills();
    this.hideEdit = true;

    this.formAdd = this.fb.group({
      name: [null, Validators.required]
    });

    this.formEdit = this.fb.group({
      name: [null, Validators.required],
      id: [null]
    });
  }

  ngOnInit(): void {
    this.crudSkillService.getAll(this.pageSkill).subscribe(
      result => {
        this.skills = result;
      }
    );
  }

  onDelete(skillId: number): void {
    this.crudSkillService.delete(skillId).subscribe(
      () => {
        this.snackBar.open('Successfully deleted skill.', 'Ok', {duration: 2000});
        this.skills.content = this.skills.content.filter(skill => skill.id !== skillId);
        this.skills.totalElements -= 1;
        if (this.skills.totalElements % 5 === 0) {
          this.onPaginationSkill(this.pageSkill - 1);
        }
      },
      error => {
        this.snackBar.open(error.error, 'Ok', {duration: 2000});
      }
    );
  }

  onClick(skillId: number): void {
    this.crudSkillService.getSkill(skillId).subscribe(
      result => {
        this.hideEdit = false;
        this.formEdit.controls.name.setValue(result.name);
        this.formEdit.controls.id.setValue(skillId);
      },
      error => {
        this.snackBar.open(error.error, 'Ok', {duration: 2000});
      }
    );
  }

  onPaginationSkill(page: number): void {
    this.pageSkill = page;
    this.crudSkillService.getAll(this.pageSkill).subscribe(
      result => {
        this.skills = result;
      }
    );
  }

  submitEdit(): void {
    const skill = new Skill(this.formEdit.value.name);
    skill.id = this.formEdit.value.id;
    this.crudSkillService.update(skill).subscribe(
      () => {
        this.snackBar.open('Skill successfully updated.', 'Ok', {duration: 2000});
        this.formEdit.controls.name.setValue('');
        this.formEdit.controls.id.setValue('');
        this.hideEdit = true;

        this.crudSkillService.getAll(this.pageSkill).subscribe(
          result => {
            this.skills = result;
          }
        );
      },
      error => {
        this.snackBar.open(error.error, 'Ok', {duration: 2000});
      }
    );
  }

  submitAdd(): void {
    this.crudSkillService.add(new Skill(this.formAdd.value.name)).subscribe(
      () => {
        this.snackBar.open('Successfully added skill.', 'Ok', {duration: 2000});
        setTimeout(() => this.formGroupDirective.resetForm(), 0);

        this.crudSkillService.getAll(this.pageSkill).subscribe(
          result => {
            this.skills = result;
          }
        );
      },
      error => {
        this.snackBar.open(error.error, 'Ok', {duration: 2000});
      }
    );
  }
}
