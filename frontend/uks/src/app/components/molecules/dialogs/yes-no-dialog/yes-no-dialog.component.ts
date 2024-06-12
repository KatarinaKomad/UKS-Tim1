import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { YesNoPrompt } from 'src/models/navigation';

@Component({
  selector: 'app-yes-no-dialog',
  templateUrl: './yes-no-dialog.component.html',
  styleUrl: './yes-no-dialog.component.scss'
})
export class YesNoDialogComponent {

  title: string = '';
  prompt: string = '';

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: YesNoPrompt,
    public dialogRef: MatDialogRef<YesNoDialogComponent>,
  ) {
    this.title = data.title;
    this.prompt = data.prompt;
  }

  onSubmitClick() {
    this.dialogRef.close(true);
  }
  onCancelClick() {
    this.dialogRef.close(false);
  }

}
