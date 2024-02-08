import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { RepoBasicInfoDTO, RepoForkRequest } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';
import { Toastr } from 'src/utils/toastr.service';

@Component({
  selector: 'app-new-fork-page',
  templateUrl: './new-fork-page.component.html',
  styleUrl: './new-fork-page.component.scss'
})
export class NewForkPageComponent implements AfterViewInit {

  forkRepoForm = this.formBuilder.group({
    owner: new FormControl(),
    name: new FormControl(""),
    description: new FormControl(""),
    isPublic: new FormControl(false),
  })

  originalRepo?: RepoBasicInfoDTO;
  loggedUser?: UserBasicInfo;
  sameOwner: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private repoService: RepoService,
    private navigationService: NavigationService,
    private toastr: Toastr
  ) {
    const originalRepoId = localStorage.getItem("repoId") as string;

    this.getRepo(originalRepoId);
    this.getLogged();
  }

  private getLogged() {
    this.authService.getLoggedUser().subscribe({
      next: (res?: UserBasicInfo) => {
        if (res) {
          this.loggedUser = res;
          this.forkRepoForm.controls.owner.setValue(`${this.loggedUser?.firstName} ${this.loggedUser?.lastName}`)

        }
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }
  private checkOwner(): boolean {
    if (this.loggedUser?.id === this.originalRepo?.owner.id) {
      this.sameOwner = true;
      this.toastr.warn('This repository already exists, cannot make a new one', 'Owner issue')
      return false;
    }
    return true;
  }

  private getRepo(repoId: string) {
    this.repoService.getById(repoId).subscribe({
      next: (res: RepoBasicInfoDTO | null) => {
        if (res) {
          this.originalRepo = res;
          this.forkRepoForm.controls.name.setValue(this.originalRepo?.name)
          this.forkRepoForm.controls.description.setValue(this.originalRepo?.description)
          this.forkRepoForm.controls.isPublic.setValue(this.originalRepo.isPublic)
        }
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  ngAfterViewInit(): void {
    this.checkOwner();
  }


  onCancelClick(): void {
    this.navigationService.navigateToRepo(this.originalRepo as RepoBasicInfoDTO)
  }

  onSubmitClick(): void {
    if (!this.checkOwner()) {
      return;
    }
    const forkRequest: RepoForkRequest = this.createForkRepoRequest();
    this.repoService.newFork(forkRequest).subscribe({
      next: (res: RepoBasicInfoDTO | null) => {
        if (res) {
          this.navigationService.navigateToRepo(res);
        }
      }, error: (e: any) => {
        this.toastr.error('Oops, something went wrong', 'Please try again!')
      }
    })

  }

  private createForkRepoRequest(): RepoForkRequest {
    return {
      ownerId: this.loggedUser?.id as string,
      isPublic: this.forkRepoForm.controls.isPublic.value as boolean,
      name: this.forkRepoForm.controls.name.value as string,
      description: this.forkRepoForm.controls.description.value as string,
      originalRepoId: this.originalRepo?.id as string
    }
  }
}
