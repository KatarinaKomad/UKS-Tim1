import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RepoRequest } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';


@Component({
  selector: 'app-new-repo-dialog',
  templateUrl: './new-repo-dialog.component.html',
  styleUrl: './new-repo-dialog.component.scss'
})
export class NewRepoDialogComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<NewRepoDialogComponent>,
    private formBuilder: FormBuilder,
  ) { }

  newRepoForm = this.formBuilder.group({
    owner: new FormControl(""),
    name: new FormControl(""),
    description: new FormControl(""),
    isPublic: new FormControl(false),
  })

  ngOnInit(): void {
    this.newRepoForm.controls.owner.setValue(`${this.data.user?.firstName} ${this.data.user?.lastName}`)
  }


  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmitClick(): void {
    const repoRequest: RepoRequest = this.createRepoRequest();
    this.dialogRef.close(repoRequest);
  }

  private createRepoRequest(): RepoRequest {
    return {
      ownerId: this.data.user?.id,
      isPublic: this.newRepoForm.controls.isPublic.value as boolean,
      name: this.newRepoForm.controls.name.value as string,
      description: this.newRepoForm.controls.description.value as string
    }
  }
}


