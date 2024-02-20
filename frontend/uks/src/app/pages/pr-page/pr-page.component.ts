import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { IssueDTO } from 'src/models/issue/issue';
import { PullRequestDTO, UserPullRequestDTO } from 'src/models/pull-request/pull-request';
import { STATE } from 'src/models/state/state';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { PullRequestService } from 'src/services/pull-request/pull-request.service';

enum View {
  CREATED = "CREATED",
  ASSIGNED = "ASSIGNED"
}

@Component({
  selector: 'app-pr-page',
  templateUrl: './pr-page.component.html',
  styleUrl: './pr-page.component.scss'
})
export class PrPageComponent implements OnInit {

  pullRequests: PullRequestDTO[] = [];
  state = "OPEN";
  repoId = "";

  View = View;
  allCreatedPRs: PullRequestDTO[] = [];
  allAssignedPRs: PullRequestDTO[] = [];

  shownPRs: PullRequestDTO[] = [];
  shownOpen: boolean = true;
  openView: View = View.CREATED;

  loggedUser?: UserBasicInfo;
  search = new FormControl('', []);

  constructor(private prService: PullRequestService) { }

  ngOnInit(): void {

    this.prService.getUserPullRequests('', '').subscribe({
      next: (res: UserPullRequestDTO) => {

        this.allCreatedPRs = res.createdPRs;
        this.allAssignedPRs = res.assignedPRs;
        this.filterShownPRs();

      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  changeShownPRs(shownOpen: boolean) {
    this.shownOpen = shownOpen;
    this.filterShownPRs();

  }

  showView(view: View) {
    this.openView = view;
    this.filterShownPRs();
  }

  onSearch() {
    const searchValue = (this.search.value as string).toLowerCase();
    if (searchValue) {
      let shownCopy = this.shownPRs.slice();
      this.shownPRs = shownCopy.filter((issue) => this.checkName(issue, searchValue) || this.checkCounter(issue, searchValue))
    } else {
      this.filterShownPRs();
    }

  }

  private checkName(pullRequest: PullRequestDTO, searchValue: string) {
    return pullRequest.name.toLowerCase().includes(searchValue);
  }

  private checkCounter(pullRequest: PullRequestDTO, searchValue: string) {
    return String(pullRequest.counter).includes(searchValue);
  }

  private filterShownPRs() {
    const allIssues = this.openView === View.CREATED ? this.allCreatedPRs : this.allAssignedPRs;
    this.shownPRs = allIssues.filter((elem: PullRequestDTO) =>
      this.shownOpen ? (elem.state === STATE.OPEN) : (elem.state === STATE.CLOSE));

    this.shownPRs = [...this.shownPRs];
  }

}
