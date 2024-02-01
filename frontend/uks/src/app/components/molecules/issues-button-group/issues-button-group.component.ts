import { AfterViewInit, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-issues-button-group',
  templateUrl: './issues-button-group.component.html',
  styleUrl: './issues-button-group.component.scss'
})
export class IssuesButtonGroupComponent implements OnInit, AfterViewInit {

  @Output() buttonClick: EventEmitter<void> = new EventEmitter<void>();

  openView: string = '';
  canEdit: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private repoService: RepoService,
    private navigationService: NavigationService
  ) { }

  ngOnInit(): void {
    this.route.url.subscribe((segments) => {
      this.openView = segments.map((segment) => segment.path).join('/');
    });
  }
  ngAfterViewInit(): void {
    this.repoService.getCanEditRepoItems().subscribe({
      next: (canEdit: boolean) => {
        this.canEdit = canEdit;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  showView(viewName: string) {
    this.openView = viewName;
    this.navigationService.navigateToProjectView(viewName);
  }
  handleAddNewClick() {
    this.buttonClick.emit();
  }

}
