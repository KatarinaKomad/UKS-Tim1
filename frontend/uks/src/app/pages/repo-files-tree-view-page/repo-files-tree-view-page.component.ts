import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-repo-files-tree-view-page',
  templateUrl: './repo-files-tree-view-page.component.html',
  styleUrl: './repo-files-tree-view-page.component.scss'
})
export class RepoFilesTreeViewPageComponent {
  repoId: string;
  branchName?: string;
  filePath?: string;

  constructor(
    private repoService: RepoService,
    private navigationService: NavigationService,
    private _liveAnnouncer: LiveAnnouncer,
    private route: ActivatedRoute,
  ) {
    this.repoId = localStorage.getItem("repoId") as string
  }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.branchName = params['branchName'] ? params['branchName'] : "master";
      this.filePath = params['filePath'] ? params['filePath'] : "";
    });

  }
}
