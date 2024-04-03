import { HttpErrorResponse } from '@angular/common/http';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
} from '@angular/core';
import { Comment } from 'src/models/comment/comment';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { CommentService } from 'src/services/comment/comment.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-item-comments-view',
  templateUrl: './item-comments-view.component.html',
  styleUrl: './item-comments-view.component.scss',
})
export class ItemCommentsViewComponent implements OnChanges {
  @Input() itemId?: string;
  @Output() noComments: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() commentDeleted: EventEmitter<void> = new EventEmitter<void>();
  @Output() editComment: EventEmitter<Comment> = new EventEmitter<Comment>();

  loggedUser?: UserBasicInfo;
  comments?: Comment[];

  constructor(
    private authService: AuthService,
    private commentService: CommentService,
    private toastr: ToastrService
  ) {
    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      },
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['itemId']?.currentValue) {
      this.getComments(changes['itemId'].currentValue);
    }
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

  isCurrentUserAuthor(commentAuthor: UserBasicInfo): boolean {
    return (
      !!this.loggedUser && this.loggedUser.username === commentAuthor.username
    );
  }

  deleteComment(comment: Comment) {
    this.commentService.deleteComment(comment.id).subscribe({
      next: () => {
        this.toastr.success('Comment deleted successfully.');
        this.commentDeleted.emit();
      },
      error: (e: HttpErrorResponse) => {
        console.error(e);
      },
    });
  }

  editCommentClicked(comment: Comment) {
    this.editComment.emit(comment);
  }
}
