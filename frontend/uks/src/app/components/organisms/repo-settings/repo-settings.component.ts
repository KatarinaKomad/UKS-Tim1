import { ToastrService } from 'ngx-toastr';
import { BranchBasicInfoDTO } from 'src/models/branch/branch';
import { RepoBasicInfoDTO, RepoUpdateRequest, getEmptyRepo } from 'src/models/repo/repo';
import { BranchService } from 'src/services/branch/branch.service';
import { RepoService } from 'src/services/repo/repo.service';

import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { REPO_ROLE, } from 'src/models/user/user';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { UserSearchDialogComponent } from '../../molecules/dialogs/user-search-dialog/user-search-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { YesNoDialogComponent } from '../../molecules/dialogs/yes-no-dialog/yes-no-dialog.component';
import { MemberService } from 'src/services/member/member.service';
import { ChangeMemberRoleRequest, MEMBER_INVITE_STATUS, RepoMemberDTO } from 'src/models/user/member';

@Component({
  selector: 'app-repo-settings',
  templateUrl: './repo-settings.component.html',
  styleUrl: './repo-settings.component.scss',
})
export class RepoSettingsComponent implements OnInit {

  repoId: string = '';
  repository: RepoBasicInfoDTO = getEmptyRepo();
  repoNameControl = new FormControl('', [Validators.required]);
  repoDescriptionControl = new FormControl('');
  branches: BranchBasicInfoDTO[] = [];
  members: RepoMemberDTO[] = [];
  REPO_ROLE = REPO_ROLE;
  INVITE_STATUS = MEMBER_INVITE_STATUS;
  roles = Object.values(REPO_ROLE).filter(x => x !== REPO_ROLE.OWNER);

  constructor(
    private branchService: BranchService,
    private repoService: RepoService,
    private memberService: MemberService,
    private toastr: ToastrService,
    private navigationService: NavigationService,
    public dialog: MatDialog,
  ) {
    this.repoId = localStorage.getItem('repoId') as string;
    const repoName = localStorage.getItem('repoName') as string;
    this.repoNameControl.setValue(repoName);

    this.setRepo();
    this.setBranches();
    this.setRepoMembers();
  }
  private setBranches() {
    this.branchService.getRepoBranches(this.repoId).subscribe({
      next: (response: BranchBasicInfoDTO[]) => {
        this.branches = response;
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    });
  }

  private setRepo() {
    this.repoService.getById(this.repoId).subscribe({
      next: (response: RepoBasicInfoDTO | null) => {
        if (response) {
          this.repository = response;
          this.repoDescriptionControl.setValue(response.description)
        }
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    });
  }
  private setRepoMembers() {
    this.memberService.getRepoMembers(this.repoId).subscribe({
      next: (res: RepoMemberDTO[]) => {
        this.members = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  ngOnInit(): void { }

  handleSave(): void {
    this.repoService
      .updateRepo(this.repository.id, {
        name: this.repoNameControl.value ?? this.repository.name,
        description: this.repoDescriptionControl.value ?? this.repository.description,
        isPublic: this.repository.isPublic,
        defaultBranch: this.repository.defaultBranch,
      } as RepoUpdateRequest)
      .subscribe({
        next: (response: RepoBasicInfoDTO | null) => {
          this.repository = response as RepoBasicInfoDTO;
          this.toastr.success('Repository updated successfully.');
        },
      });
  }


  inviteUsers() {
    const dialogRef = this.dialog.open(UserSearchDialogComponent, {
      height: '75%',
      width: '60%',
      data: { members: this.members },
    });
    dialogRef.afterClosed().subscribe((userId: string) => {
      if (userId) {
        this.sendInvitation(userId);
      }
    });
  }
  private sendInvitation(userId: string) {
    this.memberService.inviteUser({ userId, repoId: this.repoId }).subscribe({
      next: (res: RepoMemberDTO) => {
        this.toastr.success(`Successfully sent invitation request to ${res.firstName} ${res.lastName}.`);
        this.members.push(res);
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  showUserProfile(user: RepoMemberDTO) {
    this.navigationService.navigateToUser(user.id);
  }

  removeUserFromRepoPrompt(member: RepoMemberDTO) {
    const dialogRef = this.dialog.open(YesNoDialogComponent, {
      height: '35%',
      width: '40%',
      data: { title: 'Remove user', prompt: `Are you sure you want to remove ${member.firstName} ${member.lastName} from ${this.repoNameControl.value}?` },
    });
    dialogRef.afterClosed().subscribe((shouldDelete: boolean) => {
      if (shouldDelete) {
        this.removeUser(member);
      }
    });
  }
  private removeUser(member: RepoMemberDTO) {
    this.memberService.removeMember({ userId: member.id, repoId: this.repoId }).subscribe({
      next: () => {
        this.toastr.success(`Successfully removed ${member.firstName} ${member.lastName} from repository.`);
        this.members = this.members.filter(m => m.id !== member.id);
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }


  deleteRepoPrompt() {
    const dialogRef = this.dialog.open(YesNoDialogComponent, {
      height: '35%',
      width: '40%',
      data: { title: 'Delete repository', prompt: `Are you sure you want to delete your repository '${this.repoNameControl.value}'?` },
    });
    dialogRef.afterClosed().subscribe((shouldDelete: boolean) => {
      if (shouldDelete) {
        this.deleteRepo();
      }
    });
  }

  private deleteRepo() {
    this.repoService.deleteRepo(this.repoId).subscribe({
      next: () => {
        localStorage.removeItem("repoId")
        localStorage.removeItem("repoName")
        localStorage.removeItem("canEditRepoItems")
        this.navigationService.navigateToHome();
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }


  onRepoRoleChange(event: any, member: RepoMemberDTO) {
    const role: REPO_ROLE = event.target.value;
    const request: ChangeMemberRoleRequest = { repoId: this.repoId, userId: member.id, repositoryRole: role }
    this.memberService.changeRole(request).subscribe({
      next: () => {
        const index = this.members.findIndex((m: RepoMemberDTO) => m.id === member.id);
        if (index !== -1) {
          this.members[index].repositoryRole = role;
        }
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

}
