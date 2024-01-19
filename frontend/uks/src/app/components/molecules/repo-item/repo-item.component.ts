import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';

@Component({
  selector: 'app-repo-item',
  templateUrl: './repo-item.component.html',
  styleUrl: './repo-item.component.scss'
})
export class RepoItemComponent {

  @Input() repository: RepoBasicInfoDTO | undefined;

  constructor(private router: Router) { }

  navigateToRepo() {
    const repository = this.repository;
    localStorage.setItem("repoId", repository?.id as string);
    const link = `/repository/${repository?.name}`
    this.router.navigate([link], { state: { repository } })
  }
}
