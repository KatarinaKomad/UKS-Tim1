import { ToastrService } from 'ngx-toastr';
import { BranchBasicInfoDTO } from 'src/models/branch/branch';
import { RepoBasicInfoDTO, RepoUpdateRequest } from 'src/models/repo/repo';
import { BranchService } from 'src/services/branch/branch.service';
import { RepoService } from 'src/services/repo/repo.service';

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-repo-settings',
  templateUrl: './repo-settings.component.html',
  styleUrl: './repo-settings.component.scss',
})
export class RepoSettingsComponent implements OnInit {
  repository: RepoBasicInfoDTO = {
    id: '', isPublic: false, name: '', owner: {
      id: '', email: '', firstName: '', lastName: ''
    }, forkCount: 0, starCount: 0, watchCount: 0, defaultBranch: 0
  }
  repoNameControl = new FormControl('', [Validators.required]);
  branches: BranchBasicInfoDTO[] = [];

  constructor(
    private branchService: BranchService,
    private repoService: RepoService,
    private toastr: ToastrService
  ) {
    const repoId = localStorage.getItem('repoId') as string;
    const repoName = localStorage.getItem('repoName') as string;
    this.repoNameControl.setValue(repoName);

    this.repoService.getById(repoId).subscribe({
      next: (response: RepoBasicInfoDTO | null) => {
        if (response) {
          this.repository = response;
        }
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    });
    this.branchService.getRepoBranches(repoId).subscribe({
      next: (response: BranchBasicInfoDTO[]) => {
        this.branches = response;
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    });
  }

  ngOnInit(): void { }

  handleSave(): void {
    this.repoService
      .updateRepo(this.repository.id, {
        name: this.repoNameControl.value ?? this.repository.name,
        isPublic: this.repository.isPublic,
        defaultBranch: this.repository.defaultBranch,
      } as RepoUpdateRequest)
      .subscribe({
        next: (response: RepoBasicInfoDTO | null) => {
          this.repository = response as RepoBasicInfoDTO;
          this.toastr.success('Repository updated successfully.');
        },
      });
  }
}
