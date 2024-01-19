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

import { MyReposSideListComponent } from './components/organisms/repo-lists/my-repos-side-list/my-repos-side-list.component';
import { PublicRepoListComponent } from './components/organisms/repo-lists/public-repo-list/public-repo-list.component';

import { MAT_DIALOG_DATA, MatDialog, MatDialogModule } from '@angular/material/dialog';
import { NewRepoDialogComponent } from './components/molecules/dialogs/new-repo-dialog/new-repo-dialog.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

import { ButtonComponent } from './components/atoms/button/button.component';
import { InputComponent } from './components/atoms/input/input.component';
import { NavbarComponent } from './components/atoms/navbar/navbar.component';
import { SidebarComponent } from './components/atoms/sidebar/sidebar.component';
import { TabsComponent } from './components/atoms/tabs/tabs.component';
import { RepoItemComponent } from './components/molecules/repo-item/repo-item.component';
import { ProjectIssuesComponent } from './components/organisms/project-issues/project-issues.component';
import { ForgotPasswordPageComponent } from './pages/authentication/forgot-password-page/forgot-password-page.component';
import { LoginPageComponent } from './pages/authentication/login-page/login-page.component';
import { SignUpPageComponent } from './pages/authentication/sign-up-page/sign-up-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { RepositoryPageComponent } from './pages/repository-page/repository-page.component';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProjectLabelsComponent } from './components/organisms/project-labels/project-labels.component';
import { IssuesButtonGroupComponent } from './components/molecules/issues-button-group/issues-button-group.component';
import { NewLabelDialogComponent } from './components/molecules/dialogs/new-label-dialog/new-label-dialog.component';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';


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
    NewRepoDialogComponent,
    ProjectLabelsComponent,
    IssuesButtonGroupComponent,
    NewLabelDialogComponent
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
    MatTableModule,
    MatSortModule,
    ToastrModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
