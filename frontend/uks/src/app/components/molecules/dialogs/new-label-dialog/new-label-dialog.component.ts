import { Component, Inject } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { LabelRequest } from 'src/models/label/label';

@Component({
  selector: 'app-new-label-dialog',
  templateUrl: './new-label-dialog.component.html',
  styleUrl: './new-label-dialog.component.scss'
})
export class NewLabelDialogComponent {

  label?: LabelRequest;
  repoId: string;
  randomColor: string = "";

  newLabelForm = this.formBuilder.group({
    name: new FormControl(""),
    description: new FormControl(""),
    color: new FormControl(""),
  })

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<NewLabelDialogComponent>,
    private formBuilder: FormBuilder,
  ) {
    this.label = data.label;
    this.repoId = data.repoId;
  }

  ngOnInit(): void {
    this.randomColor = this.label?.color ? this.label?.color : this.getRandomColor();
    this.newLabelForm.controls.name.setValue(this.label?.name ? this.label?.name : "")
    this.newLabelForm.controls.description.setValue(this.label?.description ? this.label?.description : "")
    this.newLabelForm.controls.color.setValue(this.label?.color ? this.label?.color : this.randomColor)
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmitClick(): void {
    const labelRequest: LabelRequest = this.createLabelRequest();
    this.dialogRef.close(labelRequest);
  }

  private createLabelRequest(): LabelRequest {
    return {
      id: this.label?.id,
      name: this.newLabelForm.controls.name.value as string,
      description: this.newLabelForm.controls.description.value as string,
      color: this.newLabelForm.controls.color.value as string,
      repoId: this.repoId
    }
  }

  generateRandomColor(): void {
    this.randomColor = this.getRandomColor();
    this.newLabelForm.controls.color.setValue(this.randomColor)
  }

  private getRandomColor(): string {
    return '#' + Math.floor(Math.random() * 16777215).toString(16);
  }

}
