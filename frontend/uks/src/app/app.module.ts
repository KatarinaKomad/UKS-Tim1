import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './components/molecules/navbar/navbar.component';
import { PageTemplateComponent } from './components/organisms/page-template/page-template.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { RepositoryPageComponent } from './pages/repository-page/repository-page.component';
import { SidebarComponent } from './components/molecules/sidebar/sidebar.component';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar'; 
import { MatIconModule } from '@angular/material/icon'; 
import { MatTabsModule } from '@angular/material/tabs';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { TabsComponent } from './components/molecules/tabs/tabs.component';
import { ProjectIssuesComponent } from './components/organisms/project-issues/project-issues.component';

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
    ProjectIssuesComponent  
  ],
  exports: [
    MatSidenavModule,
    SidebarComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NoopAnimationsModule,
    MatSelectModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatTabsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
