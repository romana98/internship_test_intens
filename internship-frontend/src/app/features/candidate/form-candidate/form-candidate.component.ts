import {Component, OnInit} from '@angular/core';
import {DataStorageService} from '../../../service/data-storage/data-storage.service';

@Component({
  selector: 'app-form-candidate',
  templateUrl: './form-candidate.component.html',
  styleUrls: ['./form-candidate.component.css']
})
export class FormCandidateComponent implements OnInit {

  constructor(private dataStorage: DataStorageService) {
  }

  ngOnInit(): void {
  }

}
