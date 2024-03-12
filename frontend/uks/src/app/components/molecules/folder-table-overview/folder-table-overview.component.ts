import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource, MatTableDataSourcePaginator } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { FileDTO } from 'src/models/files/files';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-folder-table-overview',
  templateUrl: './folder-table-overview.component.html',
  styleUrl: './folder-table-overview.component.scss'
})
export class FolderTableOverviewComponent implements OnInit, OnChanges {

  displayedColumns: string[] = ['name', 'last commit message', 'date of last commit'];

  dataSource: MatTableDataSource<FileDTO, MatTableDataSourcePaginator> = new MatTableDataSource();

  @Input() files: FileDTO[] = [];

  repoId: string = "";
  branchName: string = "";
  filePath: string = "";
  @ViewChild(MatSort) sort: MatSort = new MatSort;

  constructor(
    private navigationService: NavigationService,
    private _liveAnnouncer: LiveAnnouncer,
    private route: ActivatedRoute,
  ) {
    this.repoId = localStorage.getItem("repoId") as string
  }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.branchName = params['branchName'] ? params['branchName'] : "master";
      this.filePath = params['filePath'] ? params['filePath'] : "";
    });

  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['files'].currentValue) {
      this.files = [...changes['files'].currentValue];
      this.dataSource = new MatTableDataSource(this.files);
      this.dataSource.sort = this.sort;
    }
  }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  navigateToFile(file: FileDTO) {
    this.navigationService.navigateToFile(this.branchName, file.path);
  }

}
