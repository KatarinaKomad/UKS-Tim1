
import { CollectionViewer, SelectionChange, DataSource } from '@angular/cdk/collections';
import { FlatTreeControl, NestedTreeControl } from '@angular/cdk/tree';
import { Component, Injectable, Input, SimpleChanges } from '@angular/core';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { BehaviorSubject, merge, Observable, of } from 'rxjs';
import { delay, map } from 'rxjs/operators';
import { FileDTO, FileRequest } from 'src/models/files/files';
import { DynamicFlatNode, TreeNode } from 'src/models/forms/treeView';
import { RepoService } from 'src/services/repo/repo.service';


@Component({
  selector: 'app-folder-tree-overview',
  templateUrl: './folder-tree-overview.component.html',
  styleUrl: './folder-tree-overview.component.scss'
})
export class FolderTreeOverviewComponent {

  // treeControl = new NestedTreeControl<TreeNode>(node => node.children);
  // dataSource = new MatTreeNestedDataSource<TreeNode>();

  // constructor() { }

  // ngOnInit() {
  //   this.dataSource.data = [
  //     { item: 'Folder 1', children: [], isFolder: true, level: 1 },
  //     { item: 'Folder 2', children: [], isFolder: false, level: 1 }
  //   ];
  // }

  // hasChild = (_: number, node: TreeNode) => node.isFolder || (!!node.children && node.children.length > 0);


  // getLevel = (node: TreeNode) => node.level;
  // isExpandable = (node: TreeNode) => node.isFolder;

  // loadChildren(node: TreeNode) {
  //   if (!node.children || node.children.length === 0) {
  //     node.isLoading = true;
  //     this.fakeAsyncLoadData().subscribe(children => {
  //       console.log(children)
  //       console.log(node)
  //       node.children = children;
  //       node.isLoading = false; // Hide loading indicator
  //       this.treeControl.expand(node);
  //       this.dataSource.data = [...this.dataSource.data]
  //       console.log(node)
  //     });
  //   }
  // }

  // fakeAsyncLoadData(): Observable<TreeNode[]> {
  //   return of([
  //     { item: 'Subfolder 1', children: [], isFolder: true, level: 2 },
  //     { item: 'Subfolder 2', children: [], isFolder: false, level: 2 }
  //   ]).pipe(delay(1000)); // Simulate delay of 1 second
  // }


  @Input() files: FileDTO[] = [];
  database: DynamicDatabase;
  constructor(
    database: DynamicDatabase
  ) {
    this.treeControl = new FlatTreeControl<DynamicFlatNode>(this.getLevel, this.isExpandable);
    this.dataSource = new DynamicDataSource(this.treeControl, database);

    this.database = database;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['files'].currentValue) {
      this.files = [...changes['files'].currentValue];
      this.dataSource.data = this.database.initialData(this.files);
    }
  }

  treeControl: FlatTreeControl<DynamicFlatNode>;

  dataSource: DynamicDataSource;

  getLevel = (node: DynamicFlatNode) => node.level;

  isExpandable = (node: DynamicFlatNode) => node.item.isFolder;

  hasChild = (_: number, _nodeData: DynamicFlatNode) => _nodeData.item.isFolder;
}


/**
 * Database for dynamic data. When expanding a node in the tree, the data source will need to fetch
 * the descendants data from the database.
 */
@Injectable({ providedIn: 'root' })
export class DynamicDatabase {

  repoId: string = "";

  rootLevelNodes: FileDTO[] = [];
  branchName: string = "master";
  filePath: string = "";

  constructor(
    private repoService: RepoService
  ) {
    this.repoId = localStorage.getItem("repoId") as string;

  }
  private getFileRequest(file: FileDTO | null): FileRequest {
    return { repoId: this.repoId, branchName: this.branchName, filePath: file ? file.path : this.filePath };
  }

  // dataMap = new Map<string, string[]>([
  //   ['Fruits', ['Apple', 'Orange', 'Banana']],
  //   ['Vegetables', ['Tomato', 'Potato', 'Onion']],
  //   ['Apple', ['Fuji', 'Macintosh']],
  //   ['Onion', ['Yellow', 'White', 'Purple']],
  // ]);

  // rootLevelNodes: string[] = ['Fruits', 'Vegetables'];

  /** Initial data from database */

  initialData(files: FileDTO[]): DynamicFlatNode[] {
    return files.map(file => new DynamicFlatNode(file, 0, true));
  }

  getChildren(file: FileDTO): Observable<FileDTO[]> {
    // return this.dataMap.get(node);
    return this.repoService.getFiles(this.getFileRequest(file));
  }

  isExpandable(file: FileDTO): boolean {
    return file.isFolder;
  }
}
/**
 * File database, it can build a tree structured Json object from string.
 * Each node in Json object represents a file or a directory. For a file, it has filename and type.
 * For a directory, it has filename and children (a list of files or directories).
 * The input will be a json object string, and the output is a list of `FileNode` with nested
 * structure.
 */
export class DynamicDataSource implements DataSource<DynamicFlatNode> {
  dataChange = new BehaviorSubject<DynamicFlatNode[]>([]);

  get data(): DynamicFlatNode[] {
    return this.dataChange.value;
  }
  set data(value: DynamicFlatNode[]) {
    this._treeControl.dataNodes = value;
    this.dataChange.next(value);
  }

  constructor(
    private _treeControl: FlatTreeControl<DynamicFlatNode>,
    private _database: DynamicDatabase,
  ) { }

  connect(collectionViewer: CollectionViewer): Observable<DynamicFlatNode[]> {
    this._treeControl.expansionModel.changed.subscribe(change => {
      if (
        (change as SelectionChange<DynamicFlatNode>).added ||
        (change as SelectionChange<DynamicFlatNode>).removed
      ) {
        this.handleTreeControl(change as SelectionChange<DynamicFlatNode>);
      }
    });

    return merge(collectionViewer.viewChange, this.dataChange).pipe(map(() => this.data));
  }

  disconnect(collectionViewer: CollectionViewer): void { }

  /** Handle expand/collapse behaviors */
  handleTreeControl(change: SelectionChange<DynamicFlatNode>) {
    if (change.added) {
      change.added.forEach(node => this.expand(node));
    }
    if (change.removed) {
      change.removed
        .slice()
        .reverse()
        .forEach(node => this.fold(node));
    }
  }

  /**
   * Toggle the node, remove from display list
   */
  // toggleNode(node: DynamicFlatNode, expand: boolean) {
  //   this._database.getChildren(node.item).subscribe({
  //     next: (res: FileDTO[]) => {
  //       const children = res;

  //       const index = this.data.indexOf(node);
  //       if (!children || index < 0) {
  //         return;
  //       }
  //       node.isLoading = true;
  //       setTimeout(() => {
  //         if (expand) {
  //           const nodes = children.map(
  //             name => new DynamicFlatNode(name, node.level + 1, this._database.isExpandable(name)),
  //           );
  //           this.data.splice(index + 1, 0, ...nodes);
  //         } else {
  //           let count = 0;
  //           for (let i = index + 1; i < this.data.length && this.data[i].level > node.level; i++, count++) { }
  //           this.data.splice(index + 1, count);
  //         }
  //         // notify the change
  //         this.dataChange.next(this.data);
  //         node.isLoading = false;
  //       }, 1000);
  //     }

  //   });
  // }

  expand(node: DynamicFlatNode) {

    node.isLoading = true;

    this._database.getChildren(node.item).subscribe({
      next: (children: FileDTO[]) => {

        const index = this.data.indexOf(node);
        if (!children || index < 0) {
          return;
        }

        setTimeout(() => {
          this.handleExpand(children, node);
          this.dataChange.next(this.data);
          node.isLoading = false;
        }, 1000);
      }

    });
  }

  fold(node: DynamicFlatNode) {
    node.isLoading = true;

    setTimeout(() => {
      this.handleFold(node);
      this.dataChange.next(this.data);
      node.isLoading = false;
    }, 1000);
  }

  handleExpand(children: FileDTO[], node: DynamicFlatNode) {
    const index = this.data.indexOf(node);
    const nodes = children.map(
      name => new DynamicFlatNode(name, node.level + 1, this._database.isExpandable(name)),
    );
    this.data.splice(index + 1, 0, ...nodes);
  }

  handleFold(node: DynamicFlatNode) {
    const index = this.data.indexOf(node);
    let count = 0;
    for (let i = index + 1; i < this.data.length && this.data[i].level > node.level; i++, count++) { }
    this.data.splice(index + 1, count);
  }
}
