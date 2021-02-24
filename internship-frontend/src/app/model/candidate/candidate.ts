export class Candidate {
  contactNumber: string;
  dateOfBirth: string;
  email: string;
  fullName: string;
  id: number;
  skills: string[];
  constructor(contactNumber: string, dateOfBirth: string, email: string, fullName: string, id: number, skills: string[]) {
    this.contactNumber = contactNumber;
    this.dateOfBirth = dateOfBirth;
    this.email = email;
    this.fullName = fullName;
    this.id = id;
    this.skills = skills;
  }
}
