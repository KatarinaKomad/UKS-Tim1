import { AfterViewInit, Component, ElementRef, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FILTER_LABELS_OVERLAY, INPUT_LABELS_OVERLAY, OverlayPosition } from 'src/models/forms/position';
import { LabelDTO } from 'src/models/label/label';
import { LabelService } from 'src/services/label/label.service';
import { areArraysEqual } from 'src/utils/custom-filters';

@Component({
  selector: 'app-label-filter',
  templateUrl: './label-filter.component.html',
  styleUrl: './label-filter.component.scss'
})
export class LabelFilterComponent implements AfterViewInit, OnChanges {

  @ViewChild('overlayContent') overlayContent?: ElementRef;
  @Input() preSelected: LabelDTO[] | undefined = [];
  @Input() isInput: boolean = false;
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

  ngAfterViewInit(): void {
    const position = !this.isInput ? FILTER_LABELS_OVERLAY : INPUT_LABELS_OVERLAY;
    this.setOverlayPosition(position);
  }


  ngOnChanges(changes: SimpleChanges) {
    const position = changes['isInput'] ? INPUT_LABELS_OVERLAY : FILTER_LABELS_OVERLAY;
    this.setOverlayPosition(position);
    if (changes['preSelected'].currentValue) {
      this.selected = [...changes['preSelected'].currentValue];
      this.setOverlayPosition(INPUT_LABELS_OVERLAY);
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

  private setOverlayPosition(positions: OverlayPosition) {
    if (this.overlayContent) {
      const overlayContentElement = this.overlayContent.nativeElement as HTMLElement;
      overlayContentElement.style.top = `${positions.top}%`;
      overlayContentElement.style.left = `${positions.left}%`;
    }
  }
}
