import { NavigationService } from 'src/services/navigation/navigation.service';

import { Component, OnInit } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.scss'],
})
export class TabsComponent implements OnInit {
  activeTab: number = 0;
  repoName: string = '';

  constructor(
    private route: ActivatedRoute,
    private navigationService: NavigationService
  ) {}

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
    index === 1
      ? this.navigationService.navigateToProjectIssues()
      : this.navigationService.navigateToTab(index);
  }
}
