import { HttpErrorResponse } from '@angular/common/http';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Comment, CommentRequest } from 'src/models/comment/comment';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { CommentService } from 'src/services/comment/comment.service';

@Component({
  selector: 'app-item-add-comments',
  templateUrl: './item-add-comments.component.html',
  styleUrl: './item-add-comments.component.scss',
})
export class ItemAddCommentsComponent implements OnChanges {
  @Input() itemId?: string;
  @Input() commentToEdit?: Comment;
  @Output() commentAdded: EventEmitter<void> = new EventEmitter<void>();

  loggedUser?: UserBasicInfo;
  comment?: Comment;

  editingMode: boolean = false;

  newCommentForm = this.formBuilder.group({
    name: new FormControl(''),
    message: new FormControl(''),
  });

  constructor(
    private formBuilder: FormBuilder,
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
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['commentToEdit'] && this.commentToEdit) {
      this.newCommentForm.patchValue({
        message: this.commentToEdit.message,
      });
      this.editingMode = true;
    }
  }

  onSubmitClick() {
    const message = this.newCommentForm.get('message')?.value as string;

    const request: CommentRequest = {
      message: message,
    };

    if (this.editingMode) {
      this.commentService
        .editComment(this.commentToEdit!.id, request)
        .subscribe({
          next: (comment: Comment | null) => {
            if (comment) {
              this.toastr.success('Comment updated successfully.');
              this.commentAdded.emit();
            }
          },
          error: (e: HttpErrorResponse) => {
            console.error(e);
          },
        });
    } else {
      this.commentService
        .createNewComment(this.loggedUser?.id as string, this.itemId!, request)
        .subscribe({
          next: (comment: Comment | null) => {
            if (comment) {
              this.toastr.success('Comment added successfully.');
              this.commentAdded.emit();
            }
          },
          error: (e: HttpErrorResponse) => {
            console.error(e);
          },
        });
    }

    this.newCommentForm.reset();
    this.editingMode = false;
  }
}
