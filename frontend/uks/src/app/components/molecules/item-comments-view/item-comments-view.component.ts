import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Comment } from 'src/models/comment/comment';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { CommentService } from 'src/services/comment/comment.service';

@Component({
  selector: 'app-item-comments-view',
  templateUrl: './item-comments-view.component.html',
  styleUrl: './item-comments-view.component.scss',
})
export class ItemCommentsViewComponent implements OnInit {
  @Input() itemId1?: string;
  @Output() noComments: EventEmitter<boolean> = new EventEmitter<boolean>();

  loggedUser?: UserBasicInfo;
  comments?: Comment[];
  itemId: string = '4822a7d1-5a79-4444-9065-256643c80ffc';

  isDropdownOpen = false;

  constructor(
    private authService: AuthService,
    private commentService: CommentService
  ) {
    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      },
    });
  }

  ngOnInit(): void {
    this.getComments(this.itemId);
  }

  getComments(itemId: string) {
    this.commentService.getAllItemComments(itemId).subscribe({
      next: (response: Comment[]) => {
        this.comments = response.sort((a, b) => {
          return (
            new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
          );
        });
        if (!this.comments || this.comments.length === 0) {
          this.noComments.emit(true);
        }
      },

      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    });
  }
}
