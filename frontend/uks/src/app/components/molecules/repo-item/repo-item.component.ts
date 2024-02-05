import { Component, Input } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-repo-item',
  templateUrl: './repo-item.component.html',
  styleUrl: './repo-item.component.scss'
})
export class RepoItemComponent {

  @Input() repository: RepoBasicInfoDTO | undefined;

  constructor(private navigationService: NavigationService) { }

  navigateToRepo() {
    if (this.repository) {
      this.navigationService.navigateToRepo(this.repository);
    }
  }
}
