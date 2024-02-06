import { Component, Inject } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Keyword, SignEnum } from 'src/models/search/search';

@Component({
  selector: 'app-search-filter-input',
  templateUrl: './search-filter-input.component.html',
  styleUrl: './search-filter-input.component.scss'
})
export class SearchFilterInputComponent {
  activeControl: FormControl<number | string | Date> = new FormControl();
  signControl: FormControl<SignEnum> = new FormControl();
  type: string = 'string';
  keyword: Keyword;
  signs = Object.values(SignEnum);

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<SearchFilterInputComponent>
  ) {
    this.type = data.type;
    this.keyword = data.keyword;
    this.signControl.setValue(SignEnum.EQUAL);
  }

  closeForm(input: number | string | Date | null) {
    input === null ?
      this.dialogRef.close(null) :
      this.dialogRef.close(this.signControl.value + String(input))
  }
}
