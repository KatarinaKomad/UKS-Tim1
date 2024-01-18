import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './components/atoms/navbar/navbar.component';
import { LoginPageComponent } from './pages/authentication/login-page/login-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { RepositoryPageComponent } from './pages/repository-page/repository-page.component';
import { SidebarComponent } from './components/atoms/sidebar/sidebar.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { TabsComponent } from './components/atoms/tabs/tabs.component';
import { ProjectIssuesComponent } from './components/organisms/project-issues/project-issues.component';
import { ButtonComponent } from './components/atoms/button/button.component';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputComponent } from './components/atoms/input/input.component';
import { SignUpPageComponent } from './pages/authentication/sign-up-page/sign-up-page.component';
import { ForgotPasswordPageComponent } from './pages/authentication/forgot-password-page/forgot-password-page.component';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { ToastrModule } from 'ngx-toastr';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RepoItemComponent } from './components/molecules/repo-item/repo-item.component';
import { MyReposSideListComponent } from './components/organisms/repo-lists/my-repos-side-list/my-repos-side-list.component';
import { PublicRepoListComponent } from './components/organisms/repo-lists/public-repo-list/public-repo-list.component';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule } from '@angular/material/dialog';
import { NewRepoDialogComponent } from './components/molecules/new-repo-dialog/new-repo-dialog.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
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
    PublicRepoListComponent,
    RepoItemComponent,
    MyReposSideListComponent,
    NewRepoDialogComponent
  ],
  exports: [],
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
    MatDialogModule,
    MatSlideToggleModule,
    ToastrModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
