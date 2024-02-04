import { authGuard, repoGuard } from 'src/services/auth/guards/auth.guard';

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ForgotPasswordPageComponent } from './pages/authentication/forgot-password-page/forgot-password-page.component';
import { LoginPageComponent } from './pages/authentication/login-page/login-page.component';
import { SignUpPageComponent } from './pages/authentication/sign-up-page/sign-up-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { RepositoryPageComponent } from './pages/repository-page/repository-page.component';
import { NewIssueComponent } from './components/organisms/new-issue/new-issue.component';
import { IssueOverviewComponent } from './components/organisms/issue-overview/issue-overview.component';
import { ProjectIssuesComponent } from './components/organisms/project-issues/project-issues.component';
import { ProjectMilestonesComponent } from './components/organisms/project-milestones/project-milestones.component';
import { ProjectLabelsComponent } from './components/organisms/project-labels/project-labels.component';
import { MyIssuesPageComponent } from './pages/my-issues-page/my-issues-page.component';
import { NewForkPageComponent } from './pages/new-fork-page/new-fork-page.component';
import { ForksOverviewPageComponent } from './pages/forks-overview-page/forks-overview-page.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', canMatch: [], pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent, canMatch: [], data: {} },
  { path: 'signUp', component: SignUpPageComponent, canMatch: [], data: {} },
  { path: 'forgotPassword', component: ForgotPasswordPageComponent, canMatch: [], data: {} },
  {
    path: 'home',
    component: HomePageComponent,
    canMatch: [authGuard],
    data: {},
  },
  {
    path: 'issues',
    component: MyIssuesPageComponent,
    canMatch: [authGuard],
    data: {},
  },
  {
    path: 'repository/fork',
    component: NewForkPageComponent,
    canMatch: [],
  },
  {
    path: 'repository/forks-overview',
    component: ForksOverviewPageComponent,
    canMatch: [authGuard],
  },
  {
    path: 'repository/:repoName',
    component: RepositoryPageComponent,
    canMatch: [authGuard],
    data: {},
    children: [
      { path: 'issues', component: ProjectIssuesComponent, outlet: "project-view" },
      { path: 'milestones', component: ProjectMilestonesComponent, outlet: "project-view" },
      { path: 'labels', component: ProjectLabelsComponent, outlet: "project-view" },
      { path: 'issue/new', component: NewIssueComponent, outlet: "issue-view" },
      { path: 'issue/:issueId', component: IssueOverviewComponent, outlet: "issue-view" },
    ]
  },






  { path: 'not-found', component: PageNotFoundComponent, canMatch: [] },
  { path: '**', component: PageNotFoundComponent, canMatch: [] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
