import { Component, Inject } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { BranchBasicInfoDTO, BranchDTO, OriginTargetBranchRequest, TargetBranchRequest } from 'src/models/branch/branch';

@Component({
  selector: 'app-new-branch-dialog',
  templateUrl: './new-branch-dialog.component.html',
  styleUrl: './new-branch-dialog.component.scss'
})
export class NewBranchDialogComponent {

  branch: BranchBasicInfoDTO | null;
  repoId: string;
  repoBranches: BranchDTO[] = [];

  newBranchForm = this.formBuilder.group({
    originBranch: new FormControl(),
    newBranchName: new FormControl(),
  })

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<NewBranchDialogComponent>,
    private formBuilder: FormBuilder,
  ) {
    this.repoBranches = data.repoBranches;
    this.repoId = data.repoId;
    this.branch = data.branchToEdit;
  }

  ngOnInit(): void {
    this.newBranchForm.controls.newBranchName.setValue(this.branch !== null ? this.branch?.name : "")
    // this.newBranchForm.controls.newBranch.setValue(this.milestone?.description ? this.milestone?.description : "")
  }

  onOriginChange(event: Event) {
    const selectedBranchName = (event.target as HTMLInputElement).value as string;
    this.newBranchForm.controls.originBranch.setValue(selectedBranchName);
  }


  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmitClick(): void {
    const milestoneRequest: OriginTargetBranchRequest = this.createOriginTargetBranchRequest();
    this.dialogRef.close(milestoneRequest);
  }

  private createOriginTargetBranchRequest(): OriginTargetBranchRequest {
    return {
      repoId: this.repoId,
      originName: this.branch !== null ? this.branch.name : this.newBranchForm.controls.originBranch.value as string,
      targetName: this.newBranchForm.controls.newBranchName.value as string,
    }
  }
}
