import { ToastrModule } from 'ngx-toastr';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { ButtonComponent } from './components/molecules/button/button.component';
import { InputComponent } from './components/molecules/input/input.component';
import { NavbarComponent } from './components/molecules/navbar/navbar.component';
import { SidebarComponent } from './components/molecules/sidebar/sidebar.component';
import { TabsComponent } from './components/molecules/tabs/tabs.component';
import { PageTemplateComponent } from './components/organisms/page-template/page-template.component';
import { ProjectIssuesComponent } from './components/organisms/project-issues/project-issues.component';
import { ForgotPasswordPageComponent } from './pages/authentication/forgot-password-page/forgot-password-page.component';
import { LoginPageComponent } from './pages/authentication/login-page/login-page.component';
import { SignUpPageComponent } from './pages/authentication/sign-up-page/sign-up-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { RepositoryPageComponent } from './pages/repository-page/repository-page.component';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    PageTemplateComponent,
    LoginPageComponent,
    HomePageComponent,
    RepositoryPageComponent,
    SidebarComponent,
    PageNotFoundComponent,
    TabsComponent,
    ProjectIssuesComponent,
    ButtonComponent,
    InputComponent,
    SignUpPageComponent,
    ForgotPasswordPageComponent,
  ],
  exports: [MatSidenavModule, SidebarComponent, NavbarComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NoopAnimationsModule,
    MatSelectModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatTabsModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
