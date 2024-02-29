import { Component, Input } from '@angular/core';
import { UserBasicInfo } from 'src/models/user/user';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-user-item',
  templateUrl: './user-item.component.html',
  styleUrl: './user-item.component.scss'
})
export class UserItemComponent {

  @Input() user?: UserBasicInfo;

  constructor(private navigationService: NavigationService) { }

  navigateToUser() {
    if (this.user) {
      this.navigationService.navigateToUser(this.user.id);
    }
  }
}
