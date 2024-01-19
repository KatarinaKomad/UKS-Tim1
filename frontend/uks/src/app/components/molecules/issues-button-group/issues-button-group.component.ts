import { Component, EventEmitter, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-issues-button-group',
  templateUrl: './issues-button-group.component.html',
  styleUrl: './issues-button-group.component.scss'
})
export class IssuesButtonGroupComponent {

  repoName: string = '';

  @Output() buttonClick: EventEmitter<void> = new EventEmitter<void>();

  openView: string = '';

  constructor(private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.repoName = this.route.snapshot.paramMap.get('repoName') as string;
    this.route.queryParams.subscribe(params => {
      if (params['view']) {
        this.openView = params['view'];
      }
    })
  }

  showView(viewName: string) {
    this.openView = viewName;

    this.router.navigate(
      [`repository/${this.repoName}`],
      {
        queryParams: { tab: 1, view: viewName }
      });
  }
  handleAddNewClick() {
    this.buttonClick.emit();
  }

}
