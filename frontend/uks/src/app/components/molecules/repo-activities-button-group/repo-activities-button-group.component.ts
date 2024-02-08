import { Component, Input, SimpleChanges } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-repo-activities-button-group',
  templateUrl: './repo-activities-button-group.component.html',
  styleUrl: './repo-activities-button-group.component.scss'
})
export class RepoActivitiesButtonGroupComponent {

  @Input() repo?: RepoBasicInfoDTO;

  amIWatching: boolean = false;
  haveIStarred: boolean = false
  forkOptionOpen: boolean = false

  constructor(
  ) {

  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['repo'].currentValue) {
      this.repo = changes['repo'].currentValue;
    }
  }

  onForkOptionsOpen() {
    this.forkOptionOpen = true
  }
  onForkOptionsClose() {
    this.forkOptionOpen = false;
  }

  onStarClick() {
    throw new Error('Method not implemented.');
  }
  onWatchClick() {
    throw new Error('Method not implemented.');
  }

}
