import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormControl } from '@angular/forms';
import { UserBasicInfo } from 'src/models/user/user';
import { RepoService } from 'src/services/repo/repo.service';
import { areArraysEqual } from 'src/utils/custom-filters';

@Component({
  selector: 'app-assignee-filter',
  templateUrl: './assignee-filter.component.html',
  styleUrl: './assignee-filter.component.scss'
})
export class AssigneeFilterComponent implements OnChanges {

  @Input() preSelected: UserBasicInfo[] | undefined = [];
  @Output() closeEvent: EventEmitter<UserBasicInfo[] | null> = new EventEmitter<UserBasicInfo[] | null>();

  fullList: UserBasicInfo[] = [];
  shownList: UserBasicInfo[] = [];
  selected: UserBasicInfo[] = [];

  repoId: string = '';
  filter = new FormControl<string>("");

  constructor(
    private repoService: RepoService,
  ) {

    this.repoId = localStorage.getItem('repoId') as string;

    this.repoService.getRepoMembers(this.repoId).subscribe({
      next: (list: UserBasicInfo[]) => {
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
    this.shownList = this.fullList.filter(m => (
      m.email.includes(filterValue) ||
      m.firstName.includes(filterValue) ||
      m.lastName.includes(filterValue)
    ))
  }

  isSelected(selected: UserBasicInfo): boolean {
    return this.selected.some(s => s.id === selected.id);
  }

  onCheckboxChange(event: any, selected: UserBasicInfo): void {
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
