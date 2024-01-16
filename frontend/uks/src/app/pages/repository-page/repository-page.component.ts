import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';

@Component({
  selector: 'app-repository-page',
  templateUrl: './repository-page.component.html',
  styleUrls: ['./repository-page.component.scss']
})
export class RepositoryPageComponent implements OnInit {

  repository: RepoBasicInfoDTO;

  constructor(private router: Router) {
    this.repository = this.router.getCurrentNavigation()?.extras?.state?.['repository'];
    console.log(this.repository)
  }

  ngOnInit(): void {
  }

}
