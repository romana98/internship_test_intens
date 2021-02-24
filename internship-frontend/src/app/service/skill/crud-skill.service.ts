import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Skills} from '../../model/skill/skills';
import {Skill} from '../../model/skill/skill';

@Injectable({
  providedIn: 'root'
})
export class CrudSkillService {

  private readonly pathSkill = 'http://localhost:8080/api/skills';

  constructor(private http: HttpClient) {
  }

  getAll(page: number): Observable<Skills> {
    return this.http.get<Skills>(this.pathSkill + '/by-page?page=' + page + '&size=5');
  }

  delete(skillId: number): Observable<any> {
    return this.http.delete(this.pathSkill + `/${skillId}`);
  }

  add(skill: Skill): Observable<Skill> {
    return this.http.post<Skill>(this.pathSkill, skill);
  }

  update(skill: Skill): Observable<Skill> {
    return this.http.put<Skill>(this.pathSkill, skill);
  }

  getSkill(skillId: number): Observable<Skill> {
    return this.http.get<Skill>(this.pathSkill + '/' + skillId);
  }
}
