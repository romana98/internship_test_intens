import {Component, OnInit, ViewChild} from '@angular/core';
import {DataStorageService} from '../../../service/data-storage/data-storage.service';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Skills} from '../../../model/skill/skills';
import {FormCandidateService} from '../../../service/candidate/form-candidate/form-candidate.service';
import {Candidate} from '../../../model/candidate/candidate';
import {Skill} from '../../../model/skill/skill';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';

@Component({
  selector: 'app-form-candidate',
  templateUrl: './form-candidate.component.html',
  styleUrls: ['./form-candidate.component.css']
})
export class FormCandidateComponent implements OnInit {
  @ViewChild(FormGroupDirective) formGroupDirective: FormGroupDirective;

  form: FormGroup;
  formSkill: FormGroup;

  formControl = new FormControl();
  filteredSkillsName: Observable<string[]>;

  minDate: Date;
  maxDate: Date;

  pageSkill: number;
  pageSizeSkill: number;
  skills: Skills;
  skillsList: Skill[];
  skillsNameList: string[];
  actionCandidate: string;
  candidateId: number;

  constructor(private formCandidateService: FormCandidateService,
              private dataStorage: DataStorageService,
              private fb: FormBuilder,
              public snackBar: MatSnackBar) {
    this.actionCandidate = dataStorage.data === -1 ? 'Add ' : 'Edit ';
    this.pageSkill = 0;
    this.pageSizeSkill = 5;
    this.skills = new Skills();
    this.skillsList = [];
    this.candidateId = this.dataStorage.data;

    const today = new Date();
    this.maxDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDay());
    this.minDate = new Date(today.getFullYear() - 65, today.getMonth(), today.getDay());

    this.form = this.fb.group({
      fullName: [null, [Validators.required, Validators.pattern('[A-Z][a-z]+ [A-Z][a-z]+')]],
      email: [null, [Validators.required, Validators.email]],
      contactNumber: [null, [Validators.required, Validators.pattern('[0-9]{9,10}')]],
      dateOfBirth: [null, Validators.required]
    });

    this.formSkill = this.fb.group({
      skill: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    if (this.dataStorage.data !== -1) {
      this.formCandidateService.getCandidate(this.candidateId).subscribe(
        result => {
          this.form.controls.fullName.setValue(result.fullName);
          this.form.controls.email.setValue(result.email);
          this.form.controls.contactNumber.setValue(result.contactNumber);
          this.form.controls.dateOfBirth.setValue(result.dateOfBirth);
        },
        error => {
          this.snackBar.open(error.error, 'Ok', {duration: 2000});
        }
      );

      this.formCandidateService.getSkillsByCandidate(this.candidateId, this.pageSkill).subscribe(
        result => {
          this.skills = result;
        }
      );

      this.dataStorage.data = -1;
    }
  }

  submit(): void {
    const candidate = new Candidate(
      this.form.value.contactNumber,
      this.form.value.dateOfBirth,
      this.form.value.email,
      this.form.value.fullName,
      this.candidateId,
      this.skills.content.map(skill => skill.name)
    );

    if (this.actionCandidate === 'Add ') {
      this.addCandidate(candidate);

    } else {
      this.editCandidate(candidate);
    }
  }

  submitSkill(): void {
    const skillName = this.formSkill.value.skill;
    const skill = new Skill(skillName);

    if (this.actionCandidate === 'Add ') {
      this.addSkillAdd(skill);

    } else {
      this.addSkillEdit(skill);
    }
  }

  onDelete(skillId: number): void {
    if (this.actionCandidate === 'Add ') {
      this.deleteSkillAdd(skillId);

    } else {
      this.deleteSkillEdit(skillId);
    }
  }

  onPaginationSkill(page: number): void {
    this.pageSkill = page;

    if (this.actionCandidate === 'Add ') {
      this.paginateAdd();

    } else {
      this.paginateEdit();
    }
  }

  editCandidate(candidate: Candidate): void {
    this.formCandidateService.updateCandidate(candidate).subscribe(
      () => {
        this.snackBar.open('Candidate successfully updated.', 'Ok', {duration: 2000});
      },
      error => {
        this.snackBar.open(error.error, 'Ok', {duration: 2000});
      }
    );
  }

  addCandidate(candidate: Candidate): void {
    candidate.skills = this.skillsList.map(skill => skill.name);
    this.formCandidateService.addCandidate(candidate).subscribe(
      () => {
        this.snackBar.open('Candidate successfully added.', 'Ok', {duration: 2000});
        setTimeout(() => this.formGroupDirective.resetForm(), 0);
        this.skills = new Skills();
      },
      error => {
        this.snackBar.open(error.error, 'Ok', {duration: 2000});
      }
    );
  }

  paginateEdit(): void {
    this.formCandidateService.getSkillsByCandidate(this.candidateId, this.pageSkill).subscribe(
      result => {
        this.skills = result;
      }
    );
  }

  paginateAdd(): void {
    const start = this.pageSkill * 5;
    const end = start + 5 > this.skillsList.length ? this.skillsList.length : start + 5;
    this.skills.content = this.skillsList.slice(start, end);
  }

  deleteSkillAdd(skillId: number): void {
    this.skillsList = this.skillsList.filter(skill => skill.id !== skillId);
    this.skills.totalElements = this.skillsList.length;

    if (this.skills.totalElements % 5 === 1) {
      this.skills.totalPages += 1;
    }
    this.paginateAdd();
  }

  deleteSkillEdit(skillId: number): void {
    this.formCandidateService.removeSkillFromCandidate(this.candidateId, skillId).subscribe(
      () => {
        this.snackBar.open('Successfully removed skill.', 'Ok', {duration: 2000});
        this.skills.content = this.skills.content.filter(skill => skill.id !== skillId);
      },
      error => {
        this.snackBar.open(error.error, 'Ok', {duration: 2000});
      }
    );
  }

  addSkillAdd(skill: Skill): void {
    this.skillsList.push(skill);

    const distinctSkillsList = this.skillsList.map(item => item.name).filter((value, index, self) => self.indexOf(value) === index);
    this.skillsList = distinctSkillsList.map((name, index) => {
      const newSkill = new Skill(name);

      newSkill.id = index;
      return newSkill;
    });

    this.skills.totalElements = this.skillsList.length;

    if (this.skills.totalElements % 5 === 1) {
      this.skills.totalPages += 1;
    }

    this.paginateAdd();
  }

  addSkillEdit(skill: Skill): void {
    this.formCandidateService.addSkillToCandidate(this.candidateId, skill).subscribe(
      () => {
        this.snackBar.open('Successfully added skill.', 'Ok', {duration: 2000});

        this.formCandidateService.getSkillsByCandidate(this.candidateId, this.pageSkill).subscribe(
          result => {
            this.skills = result;
          });
      },
      error => {
        this.snackBar.open(error.error, 'Ok', {duration: 2000});
      }
    );
  }

  onKeyPress(input: any): void {
    if (input.value.length > 2) {
      this.formCandidateService.getAutocomplete(input.value).subscribe(
        result => {
          this.skillsNameList = result.map(skill => skill.name);
          this.filteredSkillsName = this.formControl.valueChanges.pipe(
            startWith(''),
            map(value => this.skillsNameList.filter(option => option.toLowerCase().indexOf(value.toLowerCase()) === 0))
          );
        }
      );
    }
  }
}
