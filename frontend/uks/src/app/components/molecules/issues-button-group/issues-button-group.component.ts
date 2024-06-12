import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TAB_VIEW } from 'src/models/navigation';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-issues-button-group',
  templateUrl: './issues-button-group.component.html',
  styleUrl: './issues-button-group.component.scss'
})
export class IssuesButtonGroupComponent implements OnInit {

  @Output() buttonClick: EventEmitter<void> = new EventEmitter<void>();
  @Input() showSearch?: boolean;

  openView: TAB_VIEW = TAB_VIEW.ISSUES;
  canEdit: boolean = false;
  ISSUE_VIEW = TAB_VIEW;

  activeTab: number = 1;

  constructor(
    private route: ActivatedRoute,
    private repoService: RepoService,
    private navigationService: NavigationService
  ) { }

  ngOnInit(): void {
    this.canEdit = this.repoService.getCanEditRepoItems()
    this.route.url.subscribe((segments) => {
      this.openView = segments.map((segment) => segment.path).join('/') as TAB_VIEW;
    });

    this.route.queryParams.subscribe((params) => {
      if (params['tab']) {
        this.activeTab = Number(params['tab']);
      }
    });
  }

  showView(viewName: TAB_VIEW) {
    this.openView = viewName;
    if (this.activeTab === 1) {
      this.navigationService.navigateToProjectViewFromIssueView(viewName);
    } else if (this.activeTab === 2) {
      this.navigationService.navigateToProjectViewFromPRView(viewName);
    }
  }
  handleAddNewClick() {
    this.buttonClick.emit();
  }

}
