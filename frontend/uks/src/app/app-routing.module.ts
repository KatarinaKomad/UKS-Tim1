import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './pages/authentication/login-page/login-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { SignUpPageComponent } from './pages/authentication/sign-up-page/sign-up-page.component';
import { ForgotPasswordPageComponent } from './pages/authentication/forgot-password-page/forgot-password-page.component';

const routes: Routes = [
  { path: 'login', component: LoginPageComponent, data: {} },
  { path: 'signUp', component: SignUpPageComponent, data: {} },
  { path: 'forgotPassword', component: ForgotPasswordPageComponent, data: {} },
  { path: 'home', component: HomePageComponent, canActivate: [], data: {} },

  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
