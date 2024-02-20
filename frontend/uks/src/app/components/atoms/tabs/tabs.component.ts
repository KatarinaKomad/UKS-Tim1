import { NavigationService } from 'src/services/navigation/navigation.service';

import { AfterViewInit, Component, OnInit } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { ActivatedRoute } from '@angular/router';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.scss'],
})
export class TabsComponent implements OnInit, AfterViewInit {
  activeTab: number = 0;
  repoName: string = '';
  canEdit: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private navigationService: NavigationService,
    private repoService: RepoService
  ) { }

  ngAfterViewInit(): void {
    // this.repoService.getCanEditRepoItems().subscribe({
    //   next: (canEdit: boolean) => {
    //     this.canEdit = canEdit;
    //   }, error: (e: any) => {
    //     console.log(e);
    //   }
    // })
  }

  ngOnInit(): void {
    this.repoName = localStorage.getItem('repoName') as string;
    this.route.queryParams.subscribe((params) => {
      if (params['tab']) {
        this.activeTab = params['tab'];
      }
    });
  }

  onTabChange(event: MatTabChangeEvent) {
    const index = event.index;
    // issue tab == 1
    if (index === 1) {
      this.navigationService.navigateToProjectIssues();
    } else if (index === 2) {
      this.navigationService.navigateToProjectPRs();
    } else {
      this.navigationService.navigateToTab(index);
    }
  }
}
