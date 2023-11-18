import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';

@Component({
  selector: 'app-page-template',
  templateUrl: './page-template.component.html',
  styleUrls: ['./page-template.component.scss']
})
export class PageTemplateComponent implements AfterViewInit, OnInit {

  @ViewChild('drawer') drawer: MatDrawer | undefined;

  constructor() { }

  ngAfterViewInit(): void {
    
  }
  ngOnInit(): void {
  }

}
