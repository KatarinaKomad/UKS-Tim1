import { Component, Input, OnInit } from '@angular/core';
import { SelectionOptions, titleMapper } from 'src/models/navigation';
import { Router } from '@angular/router';
import { AuthService } from 'src/services/auth/auth.service';
import { RepoService } from 'src/services/repo/repo.service';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';



@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {


  @Input() drawer: any;

  selected!: SelectionOptions;
  SelectionOptions = SelectionOptions;
  title: string = '';
  ownerName: string = '';

  constructor(
    private router: Router,
    private authService: AuthService,
    private repoService: RepoService,
  ) {
    this.getCurrentHref();
  }

  ngOnInit(): void {
  }

  navigate(option: SelectionOptions) {
    this.selected = option;
    this.router.navigate([option])
  }

  private getCurrentHref(): void {
    const href = this.router.url;
    this.selected = href as SelectionOptions;
    this.title = titleMapper(this.selected);
    if (this.title === '' && href.includes(SelectionOptions.PROFILE)) {
      this.title = titleMapper(SelectionOptions.PROFILE);
    } else if (this.title === '' && href.includes(SelectionOptions.REPOSITORY)) {
      this.getRepoTitle();
    }
  }
  private getRepoTitle() {
    const repoId = localStorage.getItem("repoId") as string;
    this.repoService.getById(repoId).subscribe({
      next: (res: RepoBasicInfoDTO | null) => {
        this.title = `${res?.name}`
        this.ownerName = `${res?.owner.username} / `;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }


  handleLogoutButtonClick(): void {
    this.authService.logout();
  }

  onSearch() {
    this.router.navigate(['search'])
  }

}
