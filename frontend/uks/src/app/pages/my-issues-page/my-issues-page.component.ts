import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { IssueDTO, UserIssuesDTO } from 'src/models/issue/issue';
import { STATE } from 'src/models/state/state';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { IssueService } from 'src/services/issue/issue.service';

enum View {
  CREATED = "CREATED",
  ASSIGNED = "ASSIGNED"
}

@Component({
  selector: 'app-my-issues-page',
  templateUrl: './my-issues-page.component.html',
  styleUrl: './my-issues-page.component.scss'
})
export class MyIssuesPageComponent {

  View = View;
  allCreatedIssues: IssueDTO[] = [];
  allAssignedIssues: IssueDTO[] = [];

  shownIssues: IssueDTO[] = [];
  shownOpen: boolean = true;
  openView: View = View.CREATED;

  loggedUser?: UserBasicInfo;
  search = new FormControl('', []);

  constructor(
    private issueService: IssueService,
    private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        if (logged) {
          this.loggedUser = logged;
          this.issueService.getMyIssues(logged.id).subscribe({
            next: (res: UserIssuesDTO) => {

              this.allCreatedIssues = res.createdIssues;
              this.allAssignedIssues = res.assignedIssues;
              this.filterShownIssues();

            }, error: (e: any) => {
              console.log(e);
            }
          })
        }
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  changeShownIssues(shownOpen: boolean) {
    this.shownOpen = shownOpen;
    this.filterShownIssues();

  }

  showView(view: View) {
    this.openView = view;
    this.filterShownIssues();
  }

  onSearch() {
    const searchValue = (this.search.value as string).toLowerCase();
    if (searchValue) {
      let shownCopy = this.shownIssues.slice();
      this.shownIssues = shownCopy.filter((issue) => this.checkName(issue, searchValue) || this.checkCounter(issue, searchValue))
    } else {
      this.filterShownIssues();
    }

  }

  private checkName(issue: IssueDTO, searchValue: string) {
    return issue.name.toLowerCase().includes(searchValue);
  }

  private checkCounter(issue: IssueDTO, searchValue: string) {
    return String(issue.counter).includes(searchValue);
  }

  private filterShownIssues() {
    const allIssues = this.openView === View.CREATED ? this.allCreatedIssues : this.allAssignedIssues;
    this.shownIssues = allIssues.filter((elem: IssueDTO) =>
      this.shownOpen ? (elem.state === STATE.OPEN) : (elem.state === STATE.CLOSE));

    this.shownIssues = [...this.shownIssues];
  }

}
