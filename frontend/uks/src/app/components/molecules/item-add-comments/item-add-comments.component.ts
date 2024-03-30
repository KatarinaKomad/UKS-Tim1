import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
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
export class ItemAddCommentsComponent {
  @Input() itemId1?: string;

  loggedUser?: UserBasicInfo;
  comment?: Comment;
  itemId: string = '4822a7d1-5a79-4444-9065-256643c80ffc';

  newCommentForm = this.formBuilder.group({
    name: new FormControl(''),
    message: new FormControl(''),
  });

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private commentService: CommentService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      },
    });
  }

  onSubmitClick() {
    const message = this.newCommentForm.get('message')?.value as string;

    const request: CommentRequest = {
      message: message,
    };

    this.commentService
      .createNewComment(this.loggedUser?.id as string, this.itemId, request)
      .subscribe({
        next: (comment: Comment | null) => {
          if (comment) {
            this.toastr.success('Comment added successfully.');
          }
        },
        error: (e: HttpErrorResponse) => {
          console.error(e);
        },
      });
  }
}
