import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MilestoneDTO } from 'src/models/milestone/milestone';
import { MilestoneService } from 'src/services/milestone/milestone.service';

@Component({
  selector: 'app-milestone-filter',
  templateUrl: './milestone-filter.component.html',
  styleUrl: './milestone-filter.component.scss'
})
export class MilestoneFilterComponent {

  @Input() preSelected?: MilestoneDTO;
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

  ngOnChanges(changes: SimpleChanges) {
    if (changes['preSelected'].currentValue) {
      this.selected = changes['preSelected'].currentValue;
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
}
