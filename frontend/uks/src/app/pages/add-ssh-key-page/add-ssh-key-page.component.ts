import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { KeyRequest } from 'src/models/sshkey/sshkey';
import { SshkeyService } from 'src/services/sshkey/sshkey.service';

@Component({
  selector: 'app-add-ssh-key-page',
  templateUrl: './add-ssh-key-page.component.html',
  styleUrl: './add-ssh-key-page.component.scss',
})
export class AddSshKeyPageComponent implements OnInit {
  request!: KeyRequest;

  sshKeyControl = new FormControl('');

  constructor(
    private sshkeyService: SshkeyService,
    private toastr: ToastrService
  ) {
    this.request = { value: '' };
  }

  ngOnInit(): void {}

  handleSave(): void {
    this.request.value = this.sshKeyControl.value!;
    console.log(this.request);

    this.sshkeyService.createKey(this.request as KeyRequest).subscribe({
      next: () => {
        this.toastr.success('SSH Key added successfully.');
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    });
  }
}
