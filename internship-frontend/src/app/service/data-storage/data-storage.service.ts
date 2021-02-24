import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataStorageService {
  public data: number;

  constructor() {
    this.data = -1;
  }
}
