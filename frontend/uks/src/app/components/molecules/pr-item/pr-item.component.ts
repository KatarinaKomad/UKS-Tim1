import { AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PullRequestDTO } from 'src/models/pull-request/pull-request';
import { STATE } from 'src/models/state/state';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-pr-item',
  templateUrl: './pr-item.component.html',
  styleUrl: './pr-item.component.scss'
})
export class PrItemComponent implements OnInit, OnChanges, AfterViewInit {

  STATE = STATE;

  repoId: string = '';

  completePercentage: number = 0;
  canEdit: boolean = false;

  @Input() pr: PullRequestDTO | undefined;
  @Input() showRepoName?: boolean;

  constructor(
    private navigationService: NavigationService,
    private repoService: RepoService,
    public dialog: MatDialog,
  ) { }


  ngOnInit(): void {
    this.repoId = localStorage.getItem("repoId") as string
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['pr'].currentValue) {
      this.pr = changes['pr'].currentValue;
    }
    console.log(this.pr)
  }

  ngAfterViewInit(): void {
    this.canEdit = this.repoService.getCanEditRepoItems()
  }

  viewPR() {
    this.navigationService.navigateToPROverview(this.pr as PullRequestDTO);
  }

}