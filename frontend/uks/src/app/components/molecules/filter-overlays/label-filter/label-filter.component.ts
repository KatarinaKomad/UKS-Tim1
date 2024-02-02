import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { FormControl } from '@angular/forms';
import { LabelDTO } from 'src/models/label/label';
import { LabelService } from 'src/services/label/label.service';
import { areArraysEqual } from 'src/utils/custom-filters';

@Component({
  selector: 'app-label-filter',
  templateUrl: './label-filter.component.html',
  styleUrl: './label-filter.component.scss'
})
export class LabelFilterComponent {
  @Input() preSelected: LabelDTO[] | undefined = [];
  @Output() closeEvent: EventEmitter<LabelDTO[] | null> = new EventEmitter<LabelDTO[] | null>();

  fullList: LabelDTO[] = [];
  shownList: LabelDTO[] = [];
  selected: LabelDTO[] = [];

  repoId: string = '';
  filter = new FormControl<string>("");

  constructor(
    private labelService: LabelService,
  ) {

    this.repoId = localStorage.getItem('repoId') as string;

    this.labelService.getAllRepoLabels(this.repoId).subscribe({
      next: (list: LabelDTO[]) => {
        this.fullList = list;
        this.shownList = list;
      }
    })
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['preSelected'].currentValue) {
      this.selected = [...changes['preSelected'].currentValue];
    }
  }


  submitFilter() {
    const filterValue = this.filter.value as string;
    this.shownList = this.fullList.filter(el => (el.name.includes(filterValue)))
  }

  isSelected(selected: LabelDTO): boolean {
    return this.selected.some(s => s.id === selected.id);
  }

  onCheckboxChange(event: any, selected: LabelDTO): void {
    if (event.target.checked) {
      this.selected.push(selected);
    } else {
      this.selected = this.selected.filter(s => s.id !== selected.id);
    }
  }

  closeForm() {
    if (!areArraysEqual(this.preSelected as any[], this.selected)) {
      this.closeEvent.emit(this.selected);
    } else {
      this.closeEvent.emit(null);
    }
  }
}
