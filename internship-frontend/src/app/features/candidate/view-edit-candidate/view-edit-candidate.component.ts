import {Component, OnInit} from '@angular/core';
import {ViewEditCandidateService} from '../../../service/candidate/view-edit-candidate/view-edit-candidate.service';
import {Candidates} from '../../../model/candidate/candidates';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Skills} from '../../../model/skill/skills';
import {Router} from '@angular/router';
import {DataStorageService} from '../../../service/data-storage/data-storage.service';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-view-edit-candidate',
  templateUrl: './view-edit-candidate.component.html',
  styleUrls: ['./view-edit-candidate.component.css']
})
export class ViewEditCandidateComponent implements OnInit {

  pageCandidate: number;
  pageSizeCandidate: number;
  pageSkill: number;
  pageSizeSkill: number;

  candidates: Candidates;
  skills: Skills;
  showSkillsValue: boolean;
  showSkillsCurrentCandidate: number;
  skillsSelect: string[];

  form: FormGroup;


  constructor(private viewEditCandidateService: ViewEditCandidateService,
              private dataStorage: DataStorageService,
              private fb: FormBuilder,
              private snackBar: MatSnackBar,
              private router: Router) {
    this.pageCandidate = 0;
    this.pageSizeCandidate = 5;
    this.pageSkill = 0;
    this.pageSizeSkill = 5;
    this.candidates = new Candidates();
    this.skills = new Skills();
    this.showSkillsValue = true;

    this.form = this.fb.group({
      name: [''],
      skill: ['']
    });
  }

  ngOnInit(): void {
    this.viewEditCandidateService.getAll(this.pageCandidate).subscribe(
      result => {
        this.candidates = result;
      }
    );

    this.viewEditCandidateService.getSkillsForSelect().subscribe(
      result => {
        this.skillsSelect = result.map(skill => skill.name);
      }
    );
  }

  submit(): void {
    const sendData = {
      byName: this.form.value.name,
      bySkillName: this.form.value.skill.length === 0 ? '' : this.form.value.skill.join('|')
    };

    this.viewEditCandidateService.search(sendData).subscribe(
      result => {
        this.candidates = result;
        this.form.controls.name.setValue('');
        this.form.controls.skill.setValue('');
      }
    );
  }

  onDelete(candidateId: number): void {
    this.showSkillsValue = true;
    this.viewEditCandidateService.delete(candidateId).subscribe(
      () => {
        this.snackBar.open('Successfully deleted candidate.', 'Ok', {duration: 2000});
        this.candidates.content = this.candidates.content.filter(candidate => candidate.id !== candidateId);
      },
      error => {
        this.snackBar.open(error.error, 'Ok', {duration: 2000});
      }
    );
  }

  goToEditCandidate(candidateId: number): void {
    this.dataStorage.data = candidateId;
    this.router.navigate(['edit-candidate']).then(() => {
    });
  }

  showSkills(candidateId: number): void {
    this.showSkillsValue = false;
    this.showSkillsCurrentCandidate = candidateId;
    this.viewEditCandidateService.getSkillsByCandidate(candidateId, this.pageSkill).subscribe(
      result => {
        this.skills = result;
      }
    );
  }

  onPaginationCandidate(page: number): void {
    this.pageCandidate = page;
    this.viewEditCandidateService.getAll(this.pageCandidate).subscribe(
      result => {
        this.candidates = result;
      }
    );
  }

  onPaginationSkill(page: number): void {
    this.pageSkill = page;
    this.viewEditCandidateService.getSkillsByCandidate(this.showSkillsCurrentCandidate, this.pageSkill).subscribe(
      result => {
        this.skills = result;
      }
    );
  }

}
