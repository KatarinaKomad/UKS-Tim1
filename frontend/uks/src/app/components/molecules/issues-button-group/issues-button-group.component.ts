import { AfterViewInit, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EditRepoRequest } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-issues-button-group',
  templateUrl: './issues-button-group.component.html',
  styleUrl: './issues-button-group.component.scss'
})
export class IssuesButtonGroupComponent implements OnInit, AfterViewInit {

  repoName: string = '';

  @Output() buttonClick: EventEmitter<void> = new EventEmitter<void>();

  openView: string = '';
  canEdit: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private repoService: RepoService
  ) { }

  ngOnInit(): void {
    this.repoName = this.route.snapshot.paramMap.get('repoName') as string;
    this.route.queryParams.subscribe(params => {
      if (params['view']) {
        this.openView = params['view'];
      }
    })
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

    this.router.navigate(
      [`repository/${this.repoName}`],
      {
        queryParams: { tab: 1, view: viewName }
      });
  }
  handleAddNewClick() {
    this.buttonClick.emit();
  }

}
