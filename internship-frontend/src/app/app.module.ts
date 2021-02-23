import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NavigationModule} from './navigation/navigation.module';
import {RouterModule} from '@angular/router';
import {SharedModule} from './shared/shared.module';
import {CandidateModule} from './features/candidate/candidate.module';
import {SkillModule} from './features/skill/skill.module';
import {ServiceModule} from './service/service.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule,
    NavigationModule,
    SharedModule,
    CandidateModule,
    SkillModule,
    ServiceModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
