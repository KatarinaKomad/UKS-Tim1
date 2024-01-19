import { RepoBasicInfoDTO } from 'src/models/repo/repo';

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-repository-page',
  templateUrl: './repository-page.component.html',
  styleUrls: ['./repository-page.component.scss'],
})
export class RepositoryPageComponent implements OnInit {
  repository: RepoBasicInfoDTO;

  constructor(private router: Router) {
    this.repository = this.router.getCurrentNavigation()?.extras?.state?.['repository'];
  }

  ngOnInit(): void { }
}
