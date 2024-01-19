import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatTabChangeEvent, MatTabGroup } from '@angular/material/tabs';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.scss']
})
export class TabsComponent implements OnInit {


  activeTab: number = 0;
  repoName: string = '';

  view: string = 'issues';

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.repoName = this.route.snapshot.paramMap.get('repoName') as string;
    this.route.queryParams.subscribe(params => {
      if (params['tab']) {
        this.activeTab = params['tab'];
      }
      if (params['view']) {
        this.view = params['view'];
      }
    })
  }

  onTabChange(event: MatTabChangeEvent) {
    const index = event.index;
    this.router.navigate(
      [`repository/${this.repoName}`],
      {
        queryParams: { tab: index, view: (index !== 1) ? undefined : 'issues' }
      });
  }


}
