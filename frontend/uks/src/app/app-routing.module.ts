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
import { SearchPageComponent } from './pages/search-page/search-page.component';
import { PrPageComponent } from './pages/pr-page/pr-page.component';
import { PrOverviewComponent } from './components/organisms/pr-overview/pr-overview.component';
import { ProjectPrsComponent } from './components/organisms/project-prs/project-prs.component';
import { NewPrFormComponent } from './components/molecules/new-pr-form/new-pr-form.component';
import { ProfilePageComponent } from './pages/profile-page/profile-page.component';
import { AddSshKeyPageComponent } from './pages/add-ssh-key-page/add-ssh-key-page.component';
import { UsersOverviewPageComponent } from './pages/users-overview-page/users-overview-page.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'signUp', component: SignUpPageComponent },
  { path: 'forgotPassword', component: ForgotPasswordPageComponent },
  { path: 'home', component: HomePageComponent, canActivate: [authGuard] },
  { path: 'issues', component: MyIssuesPageComponent, canActivate: [authGuard] },
  { path: 'repository/fork', component: NewForkPageComponent },
  { path: 'repository/forks-overview', component: ForksOverviewPageComponent, canActivate: [authGuard] },
  { path: 'repository/watchers-overview', component: UsersOverviewPageComponent, canActivate: [authGuard] },
  { path: 'repository/stars-overview', component: UsersOverviewPageComponent, canActivate: [authGuard] },
  {
    path: 'repository/:repoName',
    component: RepositoryPageComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'issues',
        children: [
          { path: 'issues-view', component: ProjectIssuesComponent, outlet: 'issues-tab' },
          { path: 'milestones-view', component: ProjectMilestonesComponent, outlet: 'issues-tab' },
          { path: 'labels-view', component: ProjectLabelsComponent, outlet: 'issues-tab' },
          { path: 'new', component: NewIssueComponent, outlet: 'issues-tab' },
          { path: ':issueId', component: IssueOverviewComponent, outlet: 'issues-tab' },
        ]
      },

      {
        path: 'pull-requests',
        children: [
          { path: 'pr-view', component: ProjectPrsComponent, outlet: 'pr-tab' },
          { path: 'milestones-view', component: ProjectMilestonesComponent, outlet: 'pr-tab' },
          { path: 'labels-view', component: ProjectLabelsComponent, outlet: 'pr-tab' },
          { path: 'new', component: NewPrFormComponent, outlet: 'pr-tab' },
          { path: ':prId', component: PrOverviewComponent, outlet: 'pr-tab' },
        ]
      },
    ]
  },

  { path: 'profile', component: ProfilePageComponent, canMatch: [authGuard] },
  { path: 'profile/:userId', component: ProfilePageComponent, canMatch: [authGuard] },
  { path: 'sshkey', component: AddSshKeyPageComponent, canMatch: [authGuard] },
  { path: 'pull-requests', component: PrPageComponent, canActivate: [authGuard] },
  { path: 'search', component: SearchPageComponent },




  { path: 'not-found', component: PageNotFoundComponent, canMatch: [] },
  { path: '**', component: PageNotFoundComponent, canMatch: [] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
