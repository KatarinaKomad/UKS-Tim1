import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { RepoBasicInfoDTO, WatchStarResponseDTO, getEmptyRepo } from 'src/models/repo/repo';
import { UserBasicInfo, getEmptyUser } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';
import { Toastr } from 'src/utils/toastr.service';

@Component({
  selector: 'app-repo-activities-button-group',
  templateUrl: './repo-activities-button-group.component.html',
  styleUrl: './repo-activities-button-group.component.scss'
})
export class RepoActivitiesButtonGroupComponent implements OnChanges, OnInit {

  @Input() repo: RepoBasicInfoDTO = getEmptyRepo();

  amIWatching: boolean = false;
  haveIStarred: boolean = false
  forkOptionOpen: boolean = false

  loggedUser: UserBasicInfo = getEmptyUser();

  constructor(
    private repoService: RepoService,
    private authService: AuthService,
    private toastr: Toastr
  ) {

  }
  ngOnInit(): void {
    this.authService.getLoggedUser().subscribe({
      next: (res?: UserBasicInfo) => {
        if (res) {
          this.loggedUser = res;
        }
      }
    })
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['repo'].currentValue) {
      this.repo = changes['repo'].currentValue;
      this.setAmIWatchingStargazing();
    }
  }
  private setAmIWatchingStargazing() {
    if (this.repo.id && this.loggedUser.id) {
      this.repoService.amIWatchingStargazing({ repoId: this.repo.id, userId: this.loggedUser.id }).subscribe({
        next: (res: WatchStarResponseDTO) => {
          console.log(res)
          this.amIWatching = res.watching;
          this.haveIStarred = res.stargazing;
        }, error: (e: any) => {
          console.log(e);
        }
      })
    }

  }

  onForkOptionsOpen() {
    this.forkOptionOpen = true
  }
  onForkOptionsClose() {
    this.forkOptionOpen = false;
  }

  onStarClick() {
    this.repoService.star({ userId: this.loggedUser.id, repoId: this.repo.id }).subscribe({
      next: () => {
        if (!this.haveIStarred) {
          this.repo.starCount += 1;
          this.haveIStarred = true;
          this.toastr.success('Successfully stargazing')
        } else {
          this.repo.starCount -= 1;
          this.haveIStarred = false;
          this.toastr.success('Successfully not stargazing anymore')
        }
      }, error: () => {
        this.toastr.error('Please try again later', 'Oops something went wrong')
      }
    })

  }
  onWatchClick() {
    this.repoService.watch({ userId: this.loggedUser.id, repoId: this.repo.id }).subscribe({
      next: () => {
        if (!this.amIWatching) {
          this.repo.watchCount += 1;
          this.amIWatching = true;
          this.toastr.success('Successfully watching')
        } else {
          this.repo.watchCount -= 1;
          this.amIWatching = false;
          this.toastr.success('Successfully not watching anymore')
        }
      }, error: () => {
        this.toastr.error('Please try again later', 'Oops something went wrong')
      }
    })
  }


}
