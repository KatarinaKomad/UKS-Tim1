import { Component, ElementRef, EventEmitter, Input, Output, SimpleChanges, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { BranchDTO } from 'src/models/branch/branch';
import { NO_CODE_OPTIONS_CLONE_OVERLAY, OverlayPosition, SHOW_CODE_OPTIONS_CLONE_OVERLAY } from 'src/models/forms/position';
import { BranchService } from 'src/services/branch/branch.service';

@Component({
  selector: 'app-branch-filter-overlay',
  templateUrl: './branch-filter-overlay.component.html',
  styleUrl: './branch-filter-overlay.component.scss'
})
export class BranchFilterOverlayComponent {

  @Input() preSelected?: string;
  @Input() showCodeOptions: boolean = false;
  @Output() closeEvent: EventEmitter<string | null> = new EventEmitter<string | null>();
  @ViewChild('overlayContent') overlayContent?: ElementRef;


  fullList: BranchDTO[] = [];
  shownList: BranchDTO[] = [];
  selectedName?: string;

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

  ngAfterViewInit(): void {
    const position = this.showCodeOptions ? SHOW_CODE_OPTIONS_CLONE_OVERLAY : NO_CODE_OPTIONS_CLONE_OVERLAY;
    this.setOverlayPosition(position);
  }


  ngOnChanges(changes: SimpleChanges) {
    const position = changes['showCodeOptions'] ? SHOW_CODE_OPTIONS_CLONE_OVERLAY : NO_CODE_OPTIONS_CLONE_OVERLAY;
    this.setOverlayPosition(position);

    if (changes['preSelected'].currentValue) {
      this.selectedName = changes['preSelected'].currentValue;
    }
  }


  submitFilter() {
    const filterValue = this.filter.value as string;
    this.shownList = this.fullList.filter(el => el.name.includes(filterValue))
  }

  onSelectChange(selected: BranchDTO): void {
    if (this.selectedName == selected.name) {
      this.selectedName = undefined;
    } else {
      this.selectedName = selected.name;
    }

    this.closeForm();
  }

  closeForm() {
    if (this.preSelected !== this.selectedName) {
      this.closeEvent.emit(this.selectedName);
    } else {
      this.closeEvent.emit(null);
    }
  }

  private setOverlayPosition(positions: OverlayPosition) {
    if (this.overlayContent) {
      const overlayContentElement = this.overlayContent.nativeElement as HTMLElement;
      overlayContentElement.style.top = `${positions.top}%`;
      overlayContentElement.style.left = `${positions.left}%`;
    }
  }

}
