import { authGuard, repoGuard } from 'src/services/auth/guards/auth.guard';

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ForgotPasswordPageComponent } from './pages/authentication/forgot-password-page/forgot-password-page.component';
import { LoginPageComponent } from './pages/authentication/login-page/login-page.component';
import { SignUpPageComponent } from './pages/authentication/sign-up-page/sign-up-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { RepositoryPageComponent } from './pages/repository-page/repository-page.component';

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
    path: 'repository/:repoName',
    component: RepositoryPageComponent,
    canMatch: [repoGuard],
    data: {},
  },

  { path: 'not-found', component: PageNotFoundComponent, canMatch: [] },
  { path: '**', component: PageNotFoundComponent, canMatch: [] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
