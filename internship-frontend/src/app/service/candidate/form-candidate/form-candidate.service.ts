import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Candidate} from '../../../model/candidate/candidate';
import {Skills} from '../../../model/skill/skills';
import {Candidates} from '../../../model/candidate/candidates';
import {Skill} from '../../../model/skill/skill';

@Injectable({
  providedIn: 'root'
})
export class FormCandidateService {

  private readonly pathCandidates = 'http://localhost:8080/api/candidates';
  private readonly pathSkill = 'http://localhost:8080/api/skills';

  constructor(private http: HttpClient) {
  }

  getCandidate(candidateId: number): Observable<Candidate> {
    return this.http.get<Candidate>(this.pathCandidates + '/' + candidateId);
  }

  getSkillsByCandidate(candidateId: number, page: number): Observable<Skills> {
    return this.http.get<Skills>(this.pathSkill + '/' + candidateId + '/by-page?page=' + page + '&size=5');
  }

  addCandidate(candidate: Candidate): Observable<Candidates> {
    return this.http.post<Candidates>(this.pathCandidates, candidate);
  }

  updateCandidate(candidate: Candidate): Observable<Candidates> {
    return this.http.put<Candidates>(this.pathCandidates, candidate);
  }

  addSkillToCandidate(candidateId: number, skill: Skill): Observable<Skill> {
    return this.http.put<Skill>(this.pathCandidates + '/' + candidateId, skill);
  }

  removeSkillFromCandidate(candidateId: number, skillId: number): Observable<any> {
    return this.http.delete(this.pathCandidates + '/' + candidateId + '/' + skillId);
  }
}
