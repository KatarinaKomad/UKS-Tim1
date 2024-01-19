import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewLabelDialogComponent } from '../../molecules/dialogs/new-label-dialog/new-label-dialog.component';

@Component({
  selector: 'app-project-labels',
  templateUrl: './project-labels.component.html',
  styleUrl: './project-labels.component.scss'
})
export class ProjectLabelsComponent {

  constructor(public dialog: MatDialog) {

  }
  addNewLabel() {
    const dialogRef = this.dialog.open(NewLabelDialogComponent, {
      maxWidth: '100vw',
      maxHeight: '100vh',
      height: '50%',
      width: '50%',
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // this.repoService.createNewRepo(result).subscribe({
        //   next: (newRepo: RepoBasicInfoDTO | null) => {
        //     if (newRepo) {
        //       this.myRepos.push(newRepo as RepoBasicInfoDTO);
        //     }
        //   }
        // });
      }
    });
  }

}
