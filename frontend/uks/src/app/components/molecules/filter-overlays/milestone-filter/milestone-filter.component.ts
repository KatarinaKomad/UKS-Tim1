import { Component, ElementRef, EventEmitter, Input, Output, SimpleChanges, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FILTER_MILESTONE_OVERLAY, INPUT_MILESTONE_OVERLAY, OverlayPosition } from 'src/models/forms/position';
import { MilestoneDTO } from 'src/models/milestone/milestone';
import { MilestoneService } from 'src/services/milestone/milestone.service';

@Component({
  selector: 'app-milestone-filter',
  templateUrl: './milestone-filter.component.html',
  styleUrl: './milestone-filter.component.scss'
})
export class MilestoneFilterComponent {

  @ViewChild('overlayContent') overlayContent?: ElementRef;
  @Input() preSelected?: MilestoneDTO;
  @Input() isInput: boolean = false;
  @Output() closeEvent: EventEmitter<MilestoneDTO | null> = new EventEmitter<MilestoneDTO | null>();

  fullList: MilestoneDTO[] = [];
  shownList: MilestoneDTO[] = [];
  selected?: MilestoneDTO;

  repoId: string = '';
  filter = new FormControl<string>("");

  constructor(
    private milestoneService: MilestoneService,
  ) {

    this.repoId = localStorage.getItem('repoId') as string;

    this.milestoneService.getAllRepoMilestones(this.repoId).subscribe({
      next: (list: MilestoneDTO[]) => {
        this.fullList = list;
        this.shownList = list;
      }
    })
  }

  ngAfterViewInit(): void {
    const position = !this.isInput ? FILTER_MILESTONE_OVERLAY : INPUT_MILESTONE_OVERLAY;

    this.setOverlayPosition(position);
  }

  ngOnChanges(changes: SimpleChanges) {
    const position = changes['isInput'] ? INPUT_MILESTONE_OVERLAY : FILTER_MILESTONE_OVERLAY;
    this.setOverlayPosition(position);

    if (changes['preSelected'].currentValue) {
      this.selected = changes['preSelected'].currentValue;
      this.setOverlayPosition(INPUT_MILESTONE_OVERLAY);
    }
  }

  submitFilter() {
    const filterValue = this.filter.value as string;
    this.shownList = this.fullList.filter(el => (el.name.includes(filterValue)))
  }

  onSelectChange(selected: MilestoneDTO): void {
    if (this.selected?.id == selected.id) {
      this.selected = undefined;
    } else {
      this.selected = selected;
    }
  }

  closeForm() {
    if (this.preSelected?.id !== this.selected?.id) {
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
