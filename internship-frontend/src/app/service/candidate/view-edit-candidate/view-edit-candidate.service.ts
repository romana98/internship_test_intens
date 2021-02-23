import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Candidates} from '../../../model/candidate/candidates';
import {Skills} from '../../../model/skill/skills';

@Injectable({
  providedIn: 'root'
})
export class ViewEditCandidateService {

  private readonly pathCandidates = 'http://localhost:8080/api/candidates';
  private readonly pathSkill = 'http://localhost:8080/api/skills';

  constructor(private http: HttpClient) {
  }

  getAll(page: number): Observable<Candidates> {
    return this.http.get<Candidates>(this.pathCandidates + '/by-page?page=' + page + '&size=5');
  }

  search(data: object): Observable<Candidates> {
    return this.http.post<Candidates>(this.pathCandidates + '/search', data);
  }

  getSkillsByCandidate(candidateId: number, page: number): Observable<Skills> {
    return this.http.get<Skills>(this.pathSkill + '/' + candidateId + '/by-page?page=' + page + '&size=5');
  }

  delete(candidateId: number): Observable<any> {
    return this.http.delete(this.pathCandidates + `/${candidateId}`);
  }
}
