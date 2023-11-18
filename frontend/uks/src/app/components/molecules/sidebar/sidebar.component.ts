import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { SelectionOptions } from 'src/models/navigation';


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  @ViewChild('drawer') drawer: MatDrawer | undefined;
  SelectionOptions = SelectionOptions;

  constructor(private router: Router) {}

  ngOnInit(): void {
  }

  navigate(option: SelectionOptions) : void {
    console.log(option)
    this.router.navigate([option]);
  }
}
