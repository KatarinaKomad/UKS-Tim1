import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
})
export class HomePageComponent implements OnInit {

  allRepos: RepoBasicInfoDTO[] = []

  constructor(
    private repoService: RepoService,
  ) {
    this.setPublicRepos()
  }

  private setPublicRepos() {
    this.repoService.getAllPublic().subscribe({
      next: (response: RepoBasicInfoDTO[]) => {
        this.allRepos = response;
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      }
    });
  }

  ngOnInit(): void { }
}
