import { Component, ElementRef, EventEmitter, Input, Output, SimpleChanges, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { BranchDTO } from 'src/models/branch/branch';
import { BranchService } from 'src/services/branch/branch.service';

@Component({
  selector: 'app-branch-filter-overlay',
  templateUrl: './branch-filter-overlay.component.html',
  styleUrl: './branch-filter-overlay.component.scss'
})
export class BranchFilterOverlayComponent {

  @Input() preSelected?: BranchDTO;
  @Output() closeEvent: EventEmitter<BranchDTO | null> = new EventEmitter<BranchDTO | null>();
  @ViewChild('overlayContent') overlayContent?: ElementRef;


  fullList: BranchDTO[] = [];
  shownList: BranchDTO[] = [];
  selected?: BranchDTO;

  repoId: string = '';
  filter = new FormControl<string>("");

  constructor(
    private branchService: BranchService,
  ) {

    this.repoId = localStorage.getItem('repoId') as string;

    this.branchService.getRepoBranches(this.repoId).subscribe({
      next: (list: BranchDTO[]) => {
        this.fullList = list;;
        this.shownList = list;
      }
    })
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['preSelected'].currentValue) {
      this.selected = changes['preSelected'].currentValue;
    }
  }


  submitFilter() {
    const filterValue = this.filter.value as string;
    this.shownList = this.fullList.filter(el => el.name.includes(filterValue))
  }

  onSelectChange(selected: BranchDTO): void {
    if (this.selected?.id == selected.id) {
      this.selected = undefined;
    } else {
      this.selected = selected;
    }

    this.closeForm();
  }

  closeForm() {
    if (this.preSelected?.id !== this.selected?.id) {
      this.closeEvent.emit(this.selected);
    } else {
      this.closeEvent.emit(null);
    }
  }


}
