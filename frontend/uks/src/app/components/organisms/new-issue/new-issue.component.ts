import { Component } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IssueDTO, IssueRequest } from 'src/models/issue/issue';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-new-issue',
  templateUrl: './new-issue.component.html',
  styleUrl: './new-issue.component.scss'
})
export class NewIssueComponent {

  repoId: string;
  loggedUser?: UserBasicInfo;

  issueRequest?: IssueRequest;
  issueId: string = '';
  issue?: IssueDTO;

  newIssueForm = this.formBuilder.group({
    name: new FormControl(""),
    description: new FormControl(""),
  })

  constructor(
    private formBuilder: FormBuilder,
    private navigationService: NavigationService,
    private authService: AuthService,
    private issueService: IssueService,
    private route: ActivatedRoute
  ) {
    this.repoId = localStorage.getItem("repoId") as string

    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      }
    })

    this.getIssueFromRoute();
  }

  ngOnInit(): void {
    this.newIssueForm.controls.name.setValue(this.issue?.name ? this.issue?.name : "")
    this.newIssueForm.controls.description.setValue(this.issue?.description ? this.issue?.description : "")
  }

  onCancelClick(): void {
    this.navigationService.navigateToProjectIssues();
  }

  onSubmitClick(): void {
    const issueRequest: IssueRequest = this.createIssueRequest();
    this.issueService.createNewIssue(issueRequest).subscribe({
      next: (res: IssueDTO | null) => {
        console.log(res);
        this.navigationService.navigateToProjectIssues();
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  private createIssueRequest(): IssueRequest {
    return {
      name: this.newIssueForm.controls.name.value as string,
      description: this.newIssueForm.controls.description.value as string,
      repoId: this.repoId,
      authorId: this.loggedUser?.id as string,
    }
  }

  private getIssueFromRoute() {
    if (this.route.params) {
      this.route.params.subscribe(params => {
        this.issueId = params['issueId'];
        this.issueService.getById(this.issueId).subscribe({
          next: (res: IssueDTO) => {
            this.issue = res;
          }, error: (e: any) => {
            console.log(e);
          }
        })
      });
    }
  }
}
