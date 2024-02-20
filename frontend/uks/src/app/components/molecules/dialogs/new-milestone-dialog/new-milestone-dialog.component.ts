import { Component, Inject } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MilestoneRequest } from 'src/models/milestone/milestone';

@Component({
  selector: 'app-new-milestone-dialog',
  templateUrl: './new-milestone-dialog.component.html',
  styleUrl: './new-milestone-dialog.component.scss'
})
export class NewMilestoneDialogComponent {

  milestone?: MilestoneRequest;
  repoId: string;

  newMilestoneForm = this.formBuilder.group({
    name: new FormControl(""),
    description: new FormControl(""),
    dueDate: new FormControl(new Date())
  })

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<NewMilestoneDialogComponent>,
    private formBuilder: FormBuilder,
  ) {
    this.milestone = data.milestone;
    this.repoId = data.repoId;
  }

  ngOnInit(): void {
    this.newMilestoneForm.controls.name.setValue(this.milestone?.name ? this.milestone?.name : "")
    this.newMilestoneForm.controls.description.setValue(this.milestone?.description ? this.milestone?.description : "")
    this.newMilestoneForm.controls.dueDate.setValue(this.milestone?.dueDate ? this.milestone?.dueDate : null)
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmitClick(): void {
    const milestoneRequest: MilestoneRequest = this.createMilestoneRequest();
    this.dialogRef.close(milestoneRequest);
  }

  private createMilestoneRequest(): MilestoneRequest {
    return {
      id: this.milestone?.id,
      name: this.newMilestoneForm.controls.name.value as string,
      description: this.newMilestoneForm.controls.description.value as string,
      dueDate: this.newMilestoneForm.controls.dueDate.value as Date,
      repoId: this.repoId,
    }
  }

}
