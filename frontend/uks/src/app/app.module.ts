import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogModule,
} from '@angular/material/dialog';
import { ToastrModule } from 'ngx-toastr';
import { MY_DATE_FORMAT } from 'src/utils/dateUtil';

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
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatDatepickerModule } from '@angular/material/datepicker';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
  MatNativeDateModule,
} from '@angular/material/core';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { NewLabelDialogComponent } from './components/molecules/dialogs/new-label-dialog/new-label-dialog.component';
import { NewLabelDialogComponent } from './components/molecules/dialogs/new-label-dialog/new-label-dialog.component';
import { NewMilestoneDialogComponent } from './components/molecules/dialogs/new-milestone-dialog/new-milestone-dialog.component';
import { NewRepoDialogComponent } from './components/molecules/dialogs/new-repo-dialog/new-repo-dialog.component';
import { AssigneeFilterComponent } from './components/molecules/filter-overlays/assignee-filter/assignee-filter.component';
import { AuthorFilterComponent } from './components/molecules/filter-overlays/author-filter/author-filter.component';
import { LabelFilterComponent } from './components/molecules/filter-overlays/label-filter/label-filter.component';
import { MilestoneFilterComponent } from './components/molecules/filter-overlays/milestone-filter/milestone-filter.component';
import { MyReposSideListComponent } from './components/organisms/repo-lists/my-repos-side-list/my-repos-side-list.component';
import { PublicRepoListComponent } from './components/organisms/repo-lists/public-repo-list/public-repo-list.component';
import { ButtonComponent } from './components/atoms/button/button.component';
import { InputComponent } from './components/atoms/input/input.component';
import { NavbarComponent } from './components/atoms/navbar/navbar.component';
import { SidebarComponent } from './components/atoms/sidebar/sidebar.component';
import { TabsComponent } from './components/atoms/tabs/tabs.component';
import { IssueItemComponent } from './components/molecules/issue-item/issue-item.component';
import { IssuePropertiesSideViewComponent } from './components/molecules/issue-properties-side-view/issue-properties-side-view.component';
import { IssuesButtonGroupComponent } from './components/molecules/issues-button-group/issues-button-group.component';
import { IssuesButtonGroupComponent } from './components/molecules/issues-button-group/issues-button-group.component';
import { MilestoneItemComponent } from './components/molecules/milestone-item/milestone-item.component';
import { NewIssueFormComponent } from './components/molecules/new-issue-form/new-issue-form.component';
import { RepoItemComponent } from './components/molecules/repo-item/repo-item.component';
import { IssueOverviewComponent } from './components/organisms/issue-overview/issue-overview.component';
import { NewIssueComponent } from './components/organisms/new-issue/new-issue.component';
import { ProjectIssuesComponent } from './components/organisms/project-issues/project-issues.component';
import { ProjectLabelsComponent } from './components/organisms/project-labels/project-labels.component';
import { ProjectLabelsComponent } from './components/organisms/project-labels/project-labels.component';
import { ProjectMilestonesComponent } from './components/organisms/project-milestones/project-milestones.component';
import { RepoSettingsComponent } from './components/organisms/repo-settings/repo-settings.component';
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
    NewLabelDialogComponent,
    ProjectMilestonesComponent,
    NewMilestoneDialogComponent,
    MilestoneItemComponent,
    IssueItemComponent,
    NewIssueComponent,
    NewIssueFormComponent,
    IssueOverviewComponent,
    IssuePropertiesSideViewComponent,
    AssigneeFilterComponent,
    AuthorFilterComponent,
    LabelFilterComponent,
    MilestoneFilterComponent,
    RepoSettingsComponent,
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
    MatCheckboxModule,
    MatSortModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatProgressBarModule,
    ToastrModule.forRoot(),
  ],
  providers: [
    MatDatepickerModule,
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE],
    },
    { provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMAT },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
