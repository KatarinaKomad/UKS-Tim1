import { AfterViewInit, Component, ElementRef, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FILTER_ASSIGNEE_OVERLAY, INPUT_ASSIGNEE_OVERLAY, OverlayPosition } from 'src/models/issue/issue';
import { UserBasicInfo } from 'src/models/user/user';
import { MemberService } from 'src/services/member/member.service';
import { RepoService } from 'src/services/repo/repo.service';
import { areArraysEqual } from 'src/utils/custom-filters';

@Component({
  selector: 'app-assignee-filter',
  templateUrl: './assignee-filter.component.html',
  styleUrl: './assignee-filter.component.scss'
})
export class AssigneeFilterComponent implements OnChanges, AfterViewInit {

  @ViewChild('overlayContent') overlayContent?: ElementRef;
  @Input() preSelected: UserBasicInfo[] | undefined = [];
  @Input() isInput: boolean = false;
  @Output() closeEvent: EventEmitter<UserBasicInfo[] | null> = new EventEmitter<UserBasicInfo[] | null>();

  fullList: UserBasicInfo[] = [];
  shownList: UserBasicInfo[] = [];
  selected: UserBasicInfo[] = [];


  repoId: string = '';
  filter = new FormControl<string>("");

  constructor(
    private memberService: MemberService,
  ) {

    this.repoId = localStorage.getItem('repoId') as string;

    this.memberService.getRepoMembers(this.repoId).subscribe({
      next: (list: UserBasicInfo[]) => {
        this.fullList = list;
        this.shownList = list;
      }
    })

  }

  ngAfterViewInit(): void {
    const position = !this.isInput ? FILTER_ASSIGNEE_OVERLAY : INPUT_ASSIGNEE_OVERLAY;
    this.setOverlayPosition(position);
  }

  ngOnChanges(changes: SimpleChanges) {
    const position = changes['isInput'] ? INPUT_ASSIGNEE_OVERLAY : FILTER_ASSIGNEE_OVERLAY;
    this.setOverlayPosition(position);

    if (changes['preSelected'].currentValue) {
      this.selected = [...changes['preSelected'].currentValue];
      this.setOverlayPosition(INPUT_ASSIGNEE_OVERLAY);
    }
  }


  submitFilter() {
    const filterValue = this.filter.value as string;
    this.shownList = this.fullList.filter(m => (
      m.email.includes(filterValue) ||
      m.username.includes(filterValue) ||
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

  private setOverlayPosition(positions: OverlayPosition) {
    if (this.overlayContent) {
      const overlayContentElement = this.overlayContent.nativeElement as HTMLElement;
      overlayContentElement.style.top = `${positions.top}%`;
      overlayContentElement.style.left = `${positions.left}%`;
    }
  }
}
