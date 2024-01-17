import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { SelectionOptions } from 'src/models/navigation';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  @ViewChild('drawer') drawer: MatDrawer | undefined;
  SelectionOptions = SelectionOptions;

  loggedUser: UserBasicInfo | undefined;

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.authService.getLoggedUser().subscribe({
      next: (user: UserBasicInfo | undefined) => {
        this.loggedUser = user;
      },
      error: (e: any) => {
        console.log(e.error);
      }
    })
  }

  navigate(option: SelectionOptions): void {
    this.router.navigate([option]);
  }
}
