import { AfterViewInit, Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FILTER_AUTHOR_OVERLAY, OverlayPosition } from 'src/models/issue/issue';
import { UserBasicInfo } from 'src/models/user/user';
import { MemberService } from 'src/services/member/member.service';
import { RepoMemberDTO } from 'src/models/user/member';

@Component({
  selector: 'app-author-filter',
  templateUrl: './author-filter.component.html',
  styleUrl: './author-filter.component.scss'
})
export class AuthorFilterComponent implements AfterViewInit, OnChanges {

  @Input() preSelected?: UserBasicInfo;
  @Output() closeEvent: EventEmitter<UserBasicInfo | null> = new EventEmitter<UserBasicInfo | null>();
  @Input() isInput: boolean = false;
  @ViewChild('overlayContent') overlayContent?: ElementRef;


  fullList: UserBasicInfo[] = [];
  shownList: UserBasicInfo[] = [];
  selected?: UserBasicInfo;

  repoId: string = '';
  filter = new FormControl<string>("");

  constructor(
    private memberService: MemberService,
  ) {

    this.repoId = localStorage.getItem('repoId') as string;

    this.memberService.getRepoMembers(this.repoId).subscribe({
      next: (list: RepoMemberDTO[]) => {
        this.fullList = list;;
        this.shownList = list;
      }
    })
  }

  ngAfterViewInit(): void {
    this.setOverlayPosition(FILTER_AUTHOR_OVERLAY);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['preSelected'].currentValue) {
      this.selected = changes['preSelected'].currentValue;
    }
  }


  submitFilter() {
    const filterValue = this.filter.value as string;
    this.shownList = this.fullList.filter(el =>
    (el.firstName.includes(filterValue) ||
      el.lastName.includes(filterValue) ||
      el.username.includes(filterValue)
    ))
  }

  onSelectChange(selected: UserBasicInfo): void {
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
